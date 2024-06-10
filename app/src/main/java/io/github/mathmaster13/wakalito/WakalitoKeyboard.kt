package io.github.mathmaster13.wakalito

import android.annotation.SuppressLint
import android.inputmethodservice.InputMethodService
import android.os.Build
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.Button
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
        view.findViewById<Button>(R.id.space).setOnClickListener {
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
//            currentInputConnection.commitText("\n", 1) FIXME works, but makes stuff like browsers annoying to use
            // FIXME for compatibility with browsers, we send a hard keyboard event (bad)
            currentInputConnection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
            currentInputConnection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER))
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
                    if (prevChar?.isLetter() == true) {
                        println("prev is letter")
                        while (prevChar()?.isLetter() == true) delete()
                        if (prevChar() == ' ') delete()
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
//        currentInputConnection.deleteSurroundingText(1, 0) - FIXME gets the code stuck in the while loop; nothing appears to be deleted; could be a character encoding issue.
        // FIXME for testing purposes, we emulate a hard keyboard (bad).
        //  This hack causes a *noticeable* amount of lag, plus problems with long strings.
        currentInputConnection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
        currentInputConnection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL))
    }

    override fun onStartInputView(editorInfo: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(editorInfo, restarting)
        inputList.clear()
    }

    // Use the way Studio recommends if it's supported, otherwise do not.
    // Ironically, Studio's recommendation does not work on my Android R Waydroid device.
    @SuppressLint("NewApi") // DEBUG
    private fun prevChar(): Char? {
        // for some incomprehensible reason, the run block is necessary to make it behave right.
        val char = run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                println("new system")
                val text = dbg(currentInputEditorInfo.getInitialTextBeforeCursor(1, 0), "text: ")
                if (text?.isNotEmpty() == true) return@run dbg(text.lastOrNull())
            }
            println("old system")
            return@run dbg(currentInputConnection.getTextBeforeCursor(1, 0)?.lastOrNull()?.apply { println(code) }, "old text: ")
        }
        return char
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

        fun update() {
            // We WANT sequences.getOrDefault(input.toTypedArray(), "?"), but can't have it on API 21.
            asString = if (isEmpty()) "" else (sequences[list /*.toArray()*/] ?: "?")
            textView.text = "${list} = ${asString}"
        }
        fun push(key: Key) {
            list.add(key)
            update()
        }
        // returns true if something was popped
        fun pop(): Boolean {
            val canPop = list.isNotEmpty()
            if (canPop) {
                list.removeAt(list.size - 1)
                update()
            }
            return canPop
        }
        fun clear() {
            if (list.isNotEmpty()) {
                list.clear()
                update()
            }
        }
        fun isEmpty() = list.isEmpty()
    }
}