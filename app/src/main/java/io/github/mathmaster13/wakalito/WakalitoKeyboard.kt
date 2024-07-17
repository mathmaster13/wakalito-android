package io.github.mathmaster13.wakalito

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.inputmethodservice.InputMethodService
import android.os.Build
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.ExtractedTextRequest
import android.widget.ImageButton
import android.widget.TextView

// TODO is it safe to store currentInputConnection/editorinfo? is it any better to do so?

class WakalitoKeyboard : InputMethodService() {
    private val inputList = InputList()
    private var actionId: Int = EditorInfo.IME_ACTION_UNSPECIFIED // default is press enter
    private lateinit var enterKey: ImageButton // effectively val

    // TODO I do not know the behavior the application should have for onStartInput(restarting = true),
    // since I cannot think of a scenario where this happens.

    @SuppressLint("InflateParams")
    override fun onCreateInputView(): View {
        val view = layoutInflater.inflate(R.layout.keyboard, null)
        val textView = view.findViewById<TextView>(R.id.textView)
        inputList.textView = textView

        // Standard keys
        for (key in Key.entries) {
            view.findViewById<ImageButton>(key.buttonID).setOnClickListener {
                inputList.push(key)
            }
        }

        // Special keys
        view.findViewById<ImageButton>(R.id.space).setOnClickListener {
            if (inputList.isEmpty()) {
                currentInputConnection.commitText(" ", 1)
            } else {
                if (inputList.composeString.getOrNull(0)?.isLetter() == true
                    && prevChar()?.shouldHaveSpaceAfter() == true)
                    currentInputConnection.commitText(" ", 1)
                currentInputConnection.commitText(inputList.displayString(), 1)
                inputList.clear()
            }
        }

        enterKey = view.findViewById(R.id.ret)
        enterKey.setOnClickListener {
            // credit to toki pona keyboard's code for helping me figure this out
            // FIXME maybe this is wrong - there aren't good online resources
            when (val actionId = this.actionId) { // I promise it won't change!
                EditorInfo.IME_ACTION_UNSPECIFIED -> sendDownUpKeyEvents(KeyEvent.KEYCODE_ENTER)
                EditorInfo.IME_ACTION_NONE -> currentInputConnection.commitText("\n", 1)
                else -> currentInputConnection.performEditorAction(actionId) // bad custom IDs are your skill issue
            }
        }

        view.findViewById<ImageButton>(R.id.backspace).setOnClickListener {
            if (inputList.isEmpty()) {
                println("call prev")
                // studio doesn't like getSelectedText but I really don't care rn
                // NON-iOS BEHAVIOR: if the user is currently selecting text,
                // ignore the deleting rules and just delete what we're told to!
                if (currentInputConnection.getSelectedText(0).isNullOrEmpty()) {
                    println("no selection")
                    val prevChar = prevChar()
                    // let's do this the lazy way for now - directly porting from iOS
                    // premature optimization is bad
                    if (prevChar?.isLetter() == true) { // sanity check - we MUST delete at least one character
                        println("prev is letter")
                        // Credit to Toki Pona Keyboard for the "beforeCursorText" variable :)
                        val textLen = currentInputConnection
                            .getExtractedText(ExtractedTextRequest(), 0)
                            ?.text?.length ?: return@setOnClickListener deleteFallback()

                        val textBeforeCursor = textBeforeCursor(textLen) // should we just use .text directly?

                        if (textBeforeCursor.isNullOrEmpty()) // we clearly have a character here...
                            return@setOnClickListener deleteFallback()

                        val lastSpaceIndex = run {
                            var i = textBeforeCursor.lastIndex - prevChar.length // last character is a letter for sure
                            while (i >= 0) {
                                val char = codePointAtBack(textBeforeCursor, i)
                                if (char.isLetter()) i -= char.length
                                else break
                            }
                            if (i >= 0 && textBeforeCursor[i] == ' ') i else i + 1
                        }

                        currentInputConnection.deleteSurroundingText(textBeforeCursor.length - lastSpaceIndex, 0)
                    } else if (prevChar != null) delete(prevChar.length)
                } else {
                    currentInputConnection.commitText("", 1)
                }
            } else {
                inputList.pop()
            }
        }
        return view
    }

    override fun onStartInputView(editorInfo: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(editorInfo, restarting)
        // steal the enter key's functionality:

        // If the editor says don't customize the enter key, I will listen.
        if (editorInfo == null
            || editorInfo.imeOptions and EditorInfo.IME_FLAG_NO_ENTER_ACTION != 0) {
            this.actionId = EditorInfo.IME_ACTION_UNSPECIFIED
            enterKey.setImageResource(R.drawable.ret)
            return
        }
        // If the editor has a special action, use that.
        // Otherwise, do what we're told. What could go wrong?
        // If you decide to make your actionId zero, that is your problem.
        val actionId = if (editorInfo.actionLabel != null) editorInfo.actionId
            else editorInfo.imeOptions and EditorInfo.IME_MASK_ACTION

        this.actionId = actionId

        enterKey.setImageResource(when (actionId) {
            EditorInfo.IME_ACTION_SEND -> R.drawable.send
            EditorInfo.IME_ACTION_SEARCH -> R.drawable.search
            EditorInfo.IME_ACTION_DONE -> R.drawable.done
            EditorInfo.IME_ACTION_NEXT -> R.drawable.next
            EditorInfo.IME_ACTION_PREVIOUS -> R.drawable.prev
            EditorInfo.IME_ACTION_GO -> R.drawable.go
            else -> R.drawable.ret // sadly custom action labels on the main keyboard aren't supported.
        })
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        inputList.restoreTextView()
    }

    override fun onFinishInput() {
        inputList.clear()
    }

    private fun delete(length: Int = 1) {
        currentInputConnection.deleteSurroundingText(length, 0)
    }

    private fun deleteFallback() {
        // If we cannot get the text before the cursor, we will manually delete.
        // For debug purposes right now, we will crash.
        // FIXME this delete fallback is known to get the keyboard stuck in an infinite loop.
//        while (prevChar()?.isLetter() == true) delete()
//        if (prevChar() == ' ') delete()
        throw IllegalStateException("prevChar was a letter but there is no text before the cursor!")
    }

    // Use the way Studio recommends if it's supported, otherwise do not.
    // Ironically, Studio's recommendation does not work on my Android R Waydroid device.
    private fun prevChar(): SurrogateCharacter? = textBeforeCursor(2)?.let {
        if (it.isEmpty()) null else codePointAtBack(it, it.lastIndex)
    }

    private fun codePointAtBack(nonEmptyString: CharSequence, i: Int): SurrogateCharacter = nonEmptyString.let {
        when (it.length) {
            0 -> throw IllegalArgumentException("no.")
            1 -> SurrogateCharacter(Character.codePointAt(it, i), 1)
            else -> {
                val offset = if (it.hasSurrogatePairAt(i - 1)) 1 else 0
                SurrogateCharacter(Character.codePointAt(it, i - offset), offset + 1)
            }
        }
    }

    private fun textBeforeCursor(length: Int): CharSequence? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            println("new system")
            val text = dbg(currentInputEditorInfo.getInitialTextBeforeCursor(length, 0), "text: ")
            if (text?.isNotEmpty() == true) return dbg(text)
        }
        println("old system")
        return dbg(currentInputConnection.getTextBeforeCursor(length, 0), "old text: ")
    }

    private inline fun <reified T> dbg(obj: T, string: String = ""): T {
        println(string + obj)
        return obj
    }

    private class InputList {
        lateinit var textView: TextView
        val list: ArrayList<Key> = ArrayList(12) // 11-character sequences exist
        var composeString = ""
        val builder: StringBuilder = StringBuilder(24) // TODO redundant :(

        @SuppressLint("SetTextI18n") // silly android, this text is meant to be *not* translated.
        fun update() {
            if (isEmpty()) {
                composeString = "" // should never be accessed, but just in case
                textView.text = ""
            } else {
                // We WANT sequences.getOrDefault(input.toTypedArray(), "?"), but can't have it on API 21.
                composeString = sequences[list /*.toArray()*/] ?: "?"
                textView.text = "${builder}\u2009=\u2009${composeString}" // fairfax's spaces are too wide
            }
        }

        // If we redraw the view, put the composing text back!
        @SuppressLint("SetTextI18n")
        fun restoreTextView() {
            if (isEmpty()) {
                textView.text = ""
            } else {
                textView.text = "${builder}=${composeString}"
            }
        }

        fun push(key: Key) {
            list.add(key)
            builder.append('\uDB87')
            builder.append(key.surr)
            update()
        }
        // returns true if something was popped
        fun pop(): Boolean {
            val canPop = list.isNotEmpty()
            if (canPop) {
                list.removeAt(list.size - 1)
                builder.setLength(builder.length - 2)
                update()
            }
            return canPop
        }

        // set update to false to avoid updating the UI
        fun clear(update: Boolean = true) {
            if (list.isNotEmpty()) {
                list.clear()
                builder.clear()
                if (update) update()
            }
        }

        fun isEmpty() = list.isEmpty()

        fun displayString(): String = when(composeString) {
            "epiku1" -> "epiku"
            "soko1" -> "soko"
            "mute2" -> "mute"
            "sewi2" -> "sewi"
            "toki-pona" -> "toki pona"
            "aaa" -> "a a a"
            "misonaala" -> "mi sona ala"
            "alelipona" -> "ale li pona"
            else -> composeString
        }
    }
}

private data class SurrogateCharacter(val codePoint: Int, val length: Int) {
    fun isLetter() = Character.isLetter(codePoint)
    fun shouldHaveSpaceAfter() = isLetter() || when (codePoint) {
        ','.code, '.'.code, ':'.code, '?'.code, '!'.code -> true
        else -> false
    }
}
// TODO for the search function, use the inverse of displayString?
