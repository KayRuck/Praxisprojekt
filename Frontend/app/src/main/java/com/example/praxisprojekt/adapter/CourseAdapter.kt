package com.example.praxisprojekt.adapter

import androidx.recyclerview.widget.RecyclerView
import datastructure.Course
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.praxisprojekt.R

class CourseAdapter(item: List<Course>) : RecyclerView.Adapter<CourseAdapter.ViewHolder>() {

    private val items = item

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.course_item, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.title.text = items[position].title
        viewHolder.description.text = items[position].description
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemLayoutView: View) : RecyclerView.ViewHolder(itemLayoutView) {
        var title: TextView = itemLayoutView.findViewById(R.id.item_title)
        var description: TextView = itemLayoutView.findViewById(R.id.item_tags)

    }

}