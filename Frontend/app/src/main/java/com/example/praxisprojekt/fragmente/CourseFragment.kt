package com.example.praxisprojekt.fragmente

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.praxisprojekt.R
import com.example.praxisprojekt.adapter.CourseAdapter
import com.example.praxisprojekt.viewModels.CourseViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import datastructure.Course
import datastructure.InReturns
import datastructure.Mods
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.praxisprojekt.retrofit.RetroService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory




class CourseFragment : Fragment() {


    private lateinit var viewModel: CourseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.course_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CourseViewModel::class.java)
        // TODO: Use the ViewModel
    }

    fun buildRecyclerView(view: View) {

        val rec: RecyclerView = view.findViewById(R.id.recyclerView_display)
        val lin = LinearLayoutManager(context)
        rec.layoutManager = lin

        val editBtn: FloatingActionButton = view.findViewById(R.id.displayButton_l)
        editBtn.setOnClickListener {
            val fragmentTransaction = fragmentManager!!.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, CourseFragment()).commit()
        }


        val courseAdapter = CourseAdapter(dummyCourseData())
        rec.adapter = courseAdapter
        rec.itemAnimator = DefaultItemAnimator()


    }

    fun getCourses(){
        val retrofit = Retrofit.Builder()
            .baseUrl("localhost:5555")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        ///val retro : RetroService = retrofit.create(RetroService.class)





    }


/*
    private fun dummyCourseData(): List<Course> {

        return listOf(
            Course(
                "Super Tolle Nachhilfe", " ", true, 0.0, 0.0,
                true, 0, InReturns.MENSA, Mods.APMOD
            ),
            Course(
                "Nachhilfe Cool und Schnell", " ", true, 0.0, 0.0,
                true, 0, InReturns.KAFFEE, Mods.MATH1INFMOD
            )

        )

    }

*/
}
