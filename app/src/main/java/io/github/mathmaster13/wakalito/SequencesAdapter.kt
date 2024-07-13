package io.github.mathmaster13.wakalito

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SequencesAdapter(list: List<SequenceMapping>)
    : RecyclerView.Adapter<SequencesAdapter.ViewHolder>() {
    internal var data = list
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged() // TODO for now this is fine. premature optimization is bad.
        }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val glyphText: TextView = view.findViewById(R.id.glyph)
        val radicalsText: TextView = view.findViewById(R.id.radicals)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = data[position]
        holder.glyphText.text = entry.text
        holder.radicalsText.text = entry.radicals
    }
}