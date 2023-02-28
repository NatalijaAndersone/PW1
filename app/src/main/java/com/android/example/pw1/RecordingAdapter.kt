package com.android.example.pw1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.io.File

class RecordingAdapter(private val context: Context, private val recordings: List<File>) : BaseAdapter() {

    private val inflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return recordings.size
    }

    override fun getItem(position: Int): Any {
        return recordings[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: inflater.inflate(android.R.layout.simple_list_item_1, parent, false)
        val recording = recordings[position]
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = recording.name
        return view
    }

}