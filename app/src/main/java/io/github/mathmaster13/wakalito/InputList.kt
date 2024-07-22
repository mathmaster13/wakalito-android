package io.github.mathmaster13.wakalito

import android.widget.TextView

/**
 * Handles much of the "dirty work" of manipulating the list that the user input goes into.
 */
sealed class InputList {
    lateinit var textView: TextView
    val list: ArrayList<Key> = ArrayList(12) // 11-character sequences exist
    var composeString = ""
    val builder: StringBuilder = StringBuilder(24) // TODO redundant :(

    override fun toString() = list.toString()

    abstract fun update()

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

    protected fun clear() {
        list.clear()
        builder.clear()
        update()
    }

    abstract fun hasNoInput(): Boolean

    abstract fun writeWord()

    // list is already assumed to be empty when this is called
    abstract fun deleteLastWord()

    open fun switchTo(newList: InputList): InputList {
        newList.textView = textView
        newList.update()
        return newList
    }

    fun displayString(): String = when (composeString) {
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