package io.github.mathmaster13.wakalito

// TODO make the credits and table of symbols like iOS

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.TypefaceSpan
import android.text.style.URLSpan
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import androidx.core.text.method.LinkMovementMethodCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

const val DEBUG: Boolean = false
const val DEBUG_PATH = "debug.txt"

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

        // create a custom typeface span without the too-recent-API constructor
        val hint = SpannableString(resources.getString(R.string.glyph_search_hint))
        // https://stackoverflow.com/a/35322532, but with more hardcoding
        hint.setSpan(object : TypefaceSpan("") {
            override fun updateDrawState(ds: TextPaint) {
                ds.typeface = ResourcesCompat.getFont(applicationContext, R.font.compose_glyph_font)
            }
            override fun updateMeasureState(paint: TextPaint) = updateDrawState(paint)
        }, 0, hint.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        editText.hint = hint

        // remove the underline from the link, because it looks ugly
        // we must use the deprecated fromHtml because we are on min API 21.
        val topText = SpannableString(HtmlCompat.fromHtml(resources.getString(R.string.app_top_text), HtmlCompat.FROM_HTML_MODE_LEGACY))
        // https://stackoverflow.com/a/72840682
        for (u in topText.getSpans(0, topText.length, URLSpan::class.java)) {
            topText.setSpan(object : URLSpan(u.url) {
                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                }
            }, topText.getSpanStart(u), topText.getSpanEnd(u), 0)
        }
        findViewById<TextView>(R.id.app_info).run {
            text = topText
            movementMethod = LinkMovementMethodCompat.getInstance()
        }
        // TODO if using a hard "enter" press, the list seems to get gray until we hit the text view again?


        // debug setup. should be commented out for DEBUG = false.
//        if (DEBUG) {
//            findViewById<Button>(R.id.debug_copy).setOnClickListener {
//                (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
//                    .setPrimaryClip(ClipData.newPlainText("wakalito debug file", File(filesDir, DEBUG_PATH).readText()))
//            }
//            findViewById<Button>(R.id.debug_clear).setOnClickListener {
//                File(filesDir, DEBUG_PATH).writeBytes(byteArrayOf())
//            }
//        }
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
