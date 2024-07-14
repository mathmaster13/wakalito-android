package io.github.mathmaster13.wakalito

// TODO make the credits and table of symbols like iOS

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app)
        val glyphList: RecyclerView = findViewById(R.id.glyph_list)
        val adapter = SequencesAdapter(displaySequences)
        glyphList.adapter = adapter
        // TODO would be nice if this respected the margins, but decent as is
        val divider = DividerItemDecoration(glyphList.context, LinearLayoutManager.VERTICAL)
        glyphList.addItemDecoration(divider)

        // hack the EditText so I don't have to implement a search button
        val editText: EditText = findViewById(R.id.search_glyph)
        editText.setOnEditorActionListener { view: TextView, actionId: Int, keyEvent: KeyEvent? ->
            val consumed = (keyEvent?.keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_DOWN) ||
                    actionId == EditorInfo.IME_ACTION_SEARCH
            if (consumed) {
                // search logic!
                // TODO should we change this to a "fuzzier" match? like beginning of string
                adapter.data = when (val text = sanitizeInput(view.text.toString())) {
                    "" -> displaySequences
                    INVALID_INPUT -> listOf()
                    else -> displaySequences.filter {
                        it.text == text || it.text == when (text) {
                            // variants
                            "epiku" -> "epiku1"
                            "soko" -> "soko1"
                            "mute" -> "mute2"
                            "sewi" -> "sewi2"
                            else -> "" // impossible!
                        }
                    }
                }
                (view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(view.windowToken, 0)
            }
            consumed
        }

        // TODO get the EditText font working.
        // TODO if using a hard "enter" press, the list seems to get gray until we hit the text view again?
    }
}

// Inverse of displayString sorta, MUST be updated with displayString
fun sanitizeInput(string: String): String = when (val str = string.trim()) {
    // put the multi-word sequences into internal form
    "toki pona" -> "toki-pona"
    "a a a" -> "aaa"
    "mi sona ala" -> "misonaala"
    "ale li pona" -> "alelipona"

    // invalidate trying to explicitly write in an internal form - these are implememtation details!
    "epiku1", "soko1", "mute2", "sewi2", "toki-pona", "aaa", "misonaala", "alelipona" -> INVALID_INPUT
    else -> str
}

private const val INVALID_INPUT = "INVALID_INPUT"
