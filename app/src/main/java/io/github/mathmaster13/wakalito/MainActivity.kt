package io.github.mathmaster13.wakalito

// TODO make the credits and table of symbols like iOS

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app)
        val glyphList: RecyclerView = findViewById(R.id.glyph_list)
        glyphList.adapter = SequencesAdapter(displaySequences)
        // TODO would be nice if this respected the margins, but decent as is
        val divider = DividerItemDecoration(glyphList.context, LinearLayoutManager.VERTICAL)
        glyphList.addItemDecoration(divider)
    }
}
