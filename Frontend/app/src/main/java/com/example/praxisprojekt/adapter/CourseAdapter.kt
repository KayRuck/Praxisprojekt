package com.example.praxisprojekt.adapter

import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.praxisprojekt.R
import com.example.praxisprojekt.retrofit.RetroCourse
import com.example.praxisprojekt.datastructure.Mods

class CourseAdapter(item: List<RetroCourse>) : RecyclerView.Adapter<CourseAdapter.ViewHolder>() {

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

        when(items[position].fk_modules){
            1 -> viewHolder.description.text = Mods.APMOD.title
            2 -> viewHolder.description.text = Mods.MATH1INFMOD.title
            3 -> viewHolder.description.text = Mods.MATH2INFMOD.title
            4 -> viewHolder.description.text = Mods.MATHINFMOD.title
            5 -> viewHolder.description.text = Mods.BWL1INFMOD.title
        }

    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemLayoutView: View) : RecyclerView.ViewHolder(itemLayoutView) {
        var title: TextView = itemLayoutView.findViewById(R.id.item_title)
        var description: TextView = itemLayoutView.findViewById(R.id.item_tags)

    }

}