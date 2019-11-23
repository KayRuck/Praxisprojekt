package com.example.praxisprojekt.fragmente

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
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
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.praxisprojekt.retrofit.RetroCourse
import com.example.praxisprojekt.retrofit.RetroService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CourseFragment : Fragment() {

    lateinit var serverCourseData : List<RetroCourse>

    private lateinit var viewModel: CourseViewModel
    private lateinit var rootView: View
    private lateinit var rec: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.course_fragment, container, false)
        rec = rootView.findViewById(R.id.recyclerView_display)

        buildRecyclerView(rootView)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CourseViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun buildRecyclerView(view: View) {

        val lin = LinearLayoutManager(context)
        rec.layoutManager = lin

        val reloadBtn: FloatingActionButton = view.findViewById(R.id.displayButton_l2)
        reloadBtn.setOnClickListener {
            val fragTransaction1 = fragmentManager!!.beginTransaction()
            fragTransaction1.replace(R.id.fragment_container, CourseFragment()).commit()
        }

        val editBtn : FloatingActionButton = view.findViewById(R.id.displayButton_l)
        editBtn.setOnClickListener {
            val fragTransition2 = fragmentManager!!.beginTransaction()
            fragTransition2.replace(R.id.fragment_container, CourseEditFragment()).commit()
        }


        callCourses()
    }

    private fun callCourses() {

        val API_BASE_URL = "http://192.168.0.185:5555"
        val API_BASE_URL2 = "http://10.0.2.2:5555"

        val retrofit = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retroService: RetroService = retrofit.create(RetroService::class.java)
        val call = retroService.getAllCourses()

        Log.d("CourseFragment", "callCourses")

        call.enqueue(object : Callback<List<RetroCourse>> {
            override fun onResponse(
                call: Call<List<RetroCourse>>,
                response: Response<List<RetroCourse>>
            ) {
                if (response.isSuccessful) {

                    Log.d("CourseFragment", "response successful")
                    Log.d("CourseFragment", "body: ${response.body().toString()}")
                    serverCourseData = response.body()!!
                    createAdapter()
                }
            }

            override fun onFailure(call: Call<List<RetroCourse>>, t: Throwable) {
                Log.d("CourseFragment", "response failed - Cause: " + t.cause + " Message: " + t.message )
            }
        })

    }



    private fun createAdapter() {

        val courseAdapter = CourseAdapter(serverCourseData)
        rec.adapter = courseAdapter
        rec.itemAnimator = DefaultItemAnimator()
    }


//    private fun dummyCourseData(): List<Course> {
//
//        return listOf(
//            Course(
//                "Super Tolle Nachhilfe", " ", true, 0.0, 0.0,
//                true, 0, InReturns.MENSA, Mods.APMOD
//            ),
//            Course(
//                "Nachhilfe Cool und Schnell", " ", true, 0.0, 0.0,
//                true, 0, InReturns.KAFFEE, Mods.MATH1INFMOD
//            )
//
//        )
//
//    }


}

