package io.github.mathmaster13.wakalito

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.inputmethodservice.InputMethodService
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.ExtractedTextRequest
import android.view.inputmethod.InputConnection
import android.widget.ImageButton
import android.widget.TextView
import java.io.File

// TODO is it safe to store currentInputConnection/editorinfo? is it any better to do so?

class WakalitoKeyboard : InputMethodService() {
    private var inputList: InputList = StandardInputList()
    private var actionId: Int = EditorInfo.IME_ACTION_UNSPECIFIED // default is press enter
    private lateinit var enterKey: ImageButton // effectively val

    private val file by lazy { if (DEBUG) {
        println("opening file")
        File(filesDir, DEBUG_PATH).apply {
            println(path)
            writeText("---NEW FILE OPEN---\n")
        }
    } else null }

    // there are two possible ways spaces could be omitted. you either have no connection, or can't access the previous text. we check both.
    override fun getCurrentInputConnection(): InputConnection {
        if (DEBUG && super.getCurrentInputConnection() == null) dbg("INPUT CONNECTION IS NULL")
        return super.getCurrentInputConnection()
    }

    // TODO I do not know the behavior the application should have for onStartInput(restarting = true),
    // since I cannot think of a scenario where this happens.

    @SuppressLint("InflateParams")
    override fun onCreateInputView(): View {
        dbg("onCreateInputView")

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
            dbg("space pressed")
            if (inputList.hasNoInput()) {
                currentInputConnection.commitText(" ", 1)
            } else {
                inputList.writeWord()
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
            // TODO do we want a better behvaior? inserting \n when inputList has content seems weird
        }

        view.findViewById<ImageButton>(R.id.backspace).setOnClickListener {
            dbg("backspace")
            if (inputList.list.isEmpty()) {
                dbg("input list empty")
                // studio doesn't like getSelectedText but I really don't care rn
                if (currentInputConnection.getSelectedText(0).isNullOrEmpty()) {
                    dbg("no selection")
                    inputList.deleteLastWord()
                } else {
                    currentInputConnection.commitText("", 1)
                }
            } else {
                inputList.pop()
            }
        }

        view.findViewById<ImageButton>(R.id.name_mode).setOnClickListener {
            // no switching modes while typing!
            if (inputList.list.isNotEmpty()) return@setOnClickListener
            inputList = inputList.switchTo(when (inputList) {
                is StandardInputList -> NameInputList()
                is NameInputList -> StandardInputList()
            })
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
        inputList.update()
    }

    override fun onFinishInput() {
        val view = inputList.textView
        inputList = StandardInputList() // no writing/updating anything!!
        inputList.textView = view
    }

    private fun delete(length: Int = 1) {
        dbg("deleting $length characters")
        currentInputConnection.deleteSurroundingText(length, 0)
    }

    private fun deleteFallback() {
        // If we cannot get the text before the cursor, we will manually delete.
        // For debug purposes right now, we will crash.
        // FIXME this delete fallback is known to get the keyboard stuck in an infinite loop.
//        while (prevChar()?.isLetter() == true) delete()
//        if (prevChar() == ' ') delete()
        dbg("deleteFallback!!")
        throw IllegalStateException("prevChar was a letter but there is no text before the cursor!")
    }

    // Use the way Studio recommends if it's supported, otherwise do not.
    // Ironically, Studio's recommendation does not work on my Android R Waydroid device.
    private fun prevChar(): SurrogateCharacter? = textBeforeCursor(2)?.let {
        dbg("prevChar")
        if (it.isEmpty()) null else codePointAtBack(it, it.lastIndex)
    }

    private fun codePointAtBack(nonEmptyString: CharSequence, i: Int): SurrogateCharacter = nonEmptyString.let {
        dbg("call codePointAtBack with index $i")
        dbg(
        when (dbg(it.length, "length: ")) {
            0 -> throw IllegalArgumentException("no.")
            1 -> SurrogateCharacter(Character.codePointAt(it, i), 1)
            else -> {
                val offset = if (it.hasSurrogatePairAt(i - 1)) 1 else 0
                SurrogateCharacter(Character.codePointAt(it, i - offset), offset + 1)
            }
        }, "codePointAtBack return: ")
    }

    private fun textBeforeCursor(length: Int): CharSequence? {
        // causing some bizarre bugs for my tester
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            val text = dbg(currentInputEditorInfo.getInitialTextBeforeCursor(length, 0), "getInitialTextBeforeCursor: ")
//            if (text?.isNotEmpty() == true) return text
//        }
        return dbg(currentInputConnection.getTextBeforeCursor(length, 0), "getTextBeforeCursor: ")
    }

    private inline fun <reified T> dbg(obj: T, caption: String = ""): T {
        if (DEBUG) file!!.appendText("$caption$obj\n")
        return obj
    }

    private inner class StandardInputList : InputList() {
        @SuppressLint("SetTextI18n")
        override fun update() { // silly android, this text is meant to be *not* translated.
            if (list.isEmpty()) {
                composeString = "" // should never be accessed, but just in case
                textView.text = ""
            } else {
                // We WANT sequences.getOrDefault(input.toTypedArray(), "?"), but can't have it on API 21.
                composeString = sequences[list /*.toArray()*/] ?: "?"
                textView.text = "${builder}\u2009=\u2009${composeString}" // fairfax's spaces are too wide
            }
        }

        override fun hasNoInput() = list.isEmpty()

        override fun writeWord() {
            // text should never be empty here since we checked for empty input
            val text = dbg(this, "inputList: ").composeString
            if (text == "?") return // unknown inputs should not be allowed!
            if (text[0].isLetter() && prevChar()?.shouldHaveSpaceAfter() == true)
                currentInputConnection.commitText(" ", 1)
            currentInputConnection.commitText(displayString(), 1)
            clear()
        }

        override fun deleteLastWord() {
            require(list.isEmpty())
            val prevChar = prevChar()
            // let's do this the lazy way for now - directly porting from iOS
            // premature optimization is bad
            if (prevChar?.isLetter() == true) { // sanity check - we MUST delete at least one character
                dbg("prev is letter")
                // Credit to Toki Pona Keyboard for the "beforeCursorText" variable :)
                // TODO this sucks
                val textLen = currentInputConnection
                    .getExtractedText(ExtractedTextRequest(), 0)
                    ?.text?.length ?: return deleteFallback()

                dbg(textLen, "textLen: ")

                val textBeforeCursor = dbg(textBeforeCursor(textLen), "textBeforeCursor: ") // should we just use .text directly?

                if (textBeforeCursor.isNullOrEmpty()) // we clearly have a character here...
                    return deleteFallback()

                val lastSpaceIndex = run {
                    var i = textBeforeCursor.lastIndex - prevChar.length // last character is a letter for sure
                    while (i >= 0) {
                        val char = codePointAtBack(textBeforeCursor, i)
                        if (char.isLetter()) i -= char.length
                        else break
                    }
                    dbg(if (i >= 0 && textBeforeCursor[i] == ' ') i else i + 1, "final index: ")
                }

                delete(textBeforeCursor.length - lastSpaceIndex)
            } else if (prevChar != null) delete(prevChar.length)
        }
    }

    private inner class NameInputList : InputList() {
        private val cartoucheBuilder = StringBuilder(CARTOUCHE_STRING)
        private val nameBuilder = StringBuilder()

        @SuppressLint("SetTextI18n")
        override fun update() { // silly android, this text is meant to be *not* translated.
            if (list.isEmpty()) {
                composeString = "" // should never be accessed, but just in case
                textView.text = cartoucheBuilder.toString()
            } else {
                composeString = sequences[list]?.takeIf { it[0].isLetter() } ?: "?" // never empty!
                textView.text = "${builder}\u2009=\u2009${composeString}" // fairfax's spaces are too wide
            }
        }

        override fun hasNoInput() = list.isEmpty()

        override fun writeWord() {
            // text should never be empty here since we checked for empty input
            val text = dbg(this, "inputList: ").composeString
            if (text == "?") return // unknown inputs should not be allowed!
            cartoucheBuilder.insert(cartoucheBuilder.length - 2, " $text") // offset 2 because <end cartouche> is 2 characters
            // first letter of name is uppercase, rest are lowercase
            nameBuilder.apply {
                if (isEmpty()) append(text[0].uppercaseChar())
                else append(text[0].lowercaseChar())
            }
            clear()
        }

        override fun deleteLastWord() {
            require(list.isEmpty())
            if (nameBuilder.isEmpty()) return
            nameBuilder.deleteAt(nameBuilder.lastIndex)
            // cartouche end character is 2 long
            println(cartoucheBuilder)
            cartoucheBuilder.delete(cartoucheBuilder.lastIndexOf(' '), cartoucheBuilder.length - 2)
            update()
        }

        override fun switchTo(newList: InputList): InputList = super.switchTo(newList).also {
            if (nameBuilder.isEmpty()) return@also
            if (prevChar()?.shouldHaveSpaceAfter() == true)
                currentInputConnection.commitText(" ", 1)
            currentInputConnection.commitText(nameBuilder.toString(), 1)
        }
    }
}

private data class SurrogateCharacter(val codePoint: Int, val length: Int) {
    fun isLetter() = Character.isLetter(codePoint)
    fun shouldHaveSpaceAfter() = isLetter() || when (codePoint) {
        // TODO experimental: include ) and ]? maybe even include numbers?
        ','.code, '.'.code, ':'.code, '?'.code, '!'.code -> true
        else -> false
    }
}

private const val CARTOUCHE_STRING = "\uDB86\uDD90\uDB86\uDD91"
