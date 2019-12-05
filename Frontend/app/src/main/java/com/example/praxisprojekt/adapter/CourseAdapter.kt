package com.example.praxisprojekt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.praxisprojekt.Mods
import com.example.praxisprojekt.R
import com.example.praxisprojekt.retrofit.RetroCourse
import kotlinx.android.synthetic.main.course_item.view.*

class CourseAdapter(item: List<RetroCourse>, val listener : (Int) -> Unit) : RecyclerView.Adapter<CourseAdapter.ViewHolder>() {


    private val items = item

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.course_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.title.text = items[position].title

        when (items[position].fk_modules) {
            1 -> viewHolder.description.text = Mods.APMOD.title
            2 -> viewHolder.description.text = Mods.MATH1INFMOD.title
            3 -> viewHolder.description.text = Mods.MATH2INFMOD.title
            4 -> viewHolder.description.text = Mods.MATHINFMOD.title
            5 -> viewHolder.description.text = Mods.BWL1INFMOD.title
        }

        viewHolder.bind(items[position], position, listener)
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemLayoutView: View) : RecyclerView.ViewHolder(itemLayoutView) {
        var title: TextView = itemLayoutView.findViewById(R.id.item_title)
        var description: TextView = itemLayoutView.findViewById(R.id.item_tags)


        fun bind(item : RetroCourse, pos : Int, listener: (Int) -> Unit) = with(itemView){
            val cardView = courseCardView

            cardView.setOnClickListener {
                listener(pos)
            }
        }

    }


}