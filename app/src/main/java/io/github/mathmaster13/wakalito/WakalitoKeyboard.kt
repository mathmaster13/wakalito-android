package io.github.mathmaster13.wakalito

import android.annotation.SuppressLint
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
    private lateinit var inputList: InputList

    @SuppressLint("InflateParams")
    override fun onCreateInputView(): View {
        val view = layoutInflater.inflate(R.layout.keyboard, null)
        val textView = view.findViewById<TextView>(R.id.textView)
        inputList = InputList(textView) // I totally  didn't forget this line...

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
                if (inputList.asString.getOrNull(0)?.isLetter() == true
                    && prevChar()?.shouldHaveSpaceAfter == true)
                    currentInputConnection.commitText(" ", 1)
                currentInputConnection.commitText(inputList.asString, 1)
                inputList.clear()
            }
        }

        view.findViewById<ImageButton>(R.id.ret).setOnClickListener {
//            currentInputConnection.commitText("\n", 1)
            // FIXME for max compatibility with browsers, we send a hard keyboard event
            // it's possible \n works usually and Waydroid's default browser just sucks - check Chrome and Silk
            sendDownUpKeyEvents(KeyEvent.KEYCODE_ENTER)
        }

        view.findViewById<ImageButton>(R.id.backspace).setOnClickListener {
            if (inputList.isEmpty()) {
                println("call prev")
                val prevChar = prevChar()
                // studio doesn't like getSelectedText but I really don't care rn
                // NON-iOS BEHAVIOR: if the user is currently selecting text,
                // ignore the deleting rules and just delete what we're told to!
                if (currentInputConnection.getSelectedText(0).isNullOrEmpty()) {
                    println("no selection")
                    // let's do this the lazy way for now - directly porting from iOS
                    // premature optimization is bad
                    if (prevChar?.isLetter() == true) { // sanity check - we MUST delete at least one character
                        println("prev is letter")
                        // Credit to Toki Pona Keyboard for the "beforeCursorText" variable :)
                        val textLen = currentInputConnection
                            .getExtractedText(ExtractedTextRequest(), 0)
                            ?.text?.length ?: return@setOnClickListener deleteFallback()

                        val textBeforeCursor = textBeforeCursor(textLen)

                        if (textBeforeCursor.isNullOrEmpty())
                            return@setOnClickListener deleteFallback()

                        val lastSpaceIndex = run {
                            var i = textBeforeCursor.lastIndex // verified to be a letter here!
                            while (i >= 0 && textBeforeCursor[i].isLetter()) i--
                            if (i >= 0 && textBeforeCursor[i] == ' ') i else i + 1
                        }

                        currentInputConnection.deleteSurroundingText(textBeforeCursor.length - lastSpaceIndex, 0)
                    } else if (prevChar != null) delete()
                } else {
                    println("we have a selection!")
                    currentInputConnection.commitText("", 1)
                }
            } else {
                inputList.pop()
            }
        }

        return view
    }

    private fun delete() {
        currentInputConnection.deleteSurroundingText(1, 0)
    }

    private fun deleteFallback() {
        // If we cannot get the text before the cursor, we will manually delete.
        // For debug purposes right now, we will crash.
        // FIXME this delete fallback is known to get the keyboard stuck in an infinite loop.
//        while (prevChar()?.isLetter() == true) delete()
//        if (prevChar() == ' ') delete()
        throw IllegalStateException("prevChar was a letter but there is no text before the cursor!")
    }

    override fun onStartInputView(editorInfo: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(editorInfo, restarting)
        if (!restarting) inputList.clear() // is this if block the right move?
    }

    // Use the way Studio recommends if it's supported, otherwise do not.
    // Ironically, Studio's recommendation does not work on my Android R Waydroid device.
    private fun prevChar(): Char? = textBeforeCursor(1)?.lastOrNull()

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

    private val Char.shouldHaveSpaceAfter get() =
        isLetter() || when(this) {
            ',', '.', ':', '?', '!' -> true
            else -> false
        }

    private class InputList(val textView: TextView) {
        val list: ArrayList<Key> = ArrayList(12) // 11-character sequences exist
        var asString = ""
        val builder: StringBuilder = StringBuilder(24) // TODO redundant :(

        fun update() {
            if (isEmpty()) {
                asString = "" // should never be accessed, but just in case
                textView.text = ""
            } else {
                // We WANT sequences.getOrDefault(input.toTypedArray(), "?"), but can't have it on API 21.
                asString = sequences[list /*.toArray()*/] ?: "?"
                textView.text = "${builder}=${asString}"
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
        fun clear() {
            if (list.isNotEmpty()) {
                list.clear()
                builder.clear()
                update()
            }
        }
        fun isEmpty() = list.isEmpty()
    }
}
