package com.example.praxisprojekt.fragmente

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.praxisprojekt.R
import com.example.praxisprojekt.adapter.CourseAdapter
import com.example.praxisprojekt.retrofit.RetroCourse
import com.example.praxisprojekt.retrofit.RetroService
import com.example.praxisprojekt.viewModels.CourseViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CourseFragment : Fragment() {

    lateinit var serverCourseData: List<RetroCourse>
    private lateinit var viewModel: CourseViewModel
    private lateinit var rootView: View
    private lateinit var rec: RecyclerView
//    private lateinit var retrofitClient: RetrofitClient


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

        val editBtn: FloatingActionButton = view.findViewById(R.id.displayButton_l)
        editBtn.setOnClickListener {
            val fragTransition2 = fragmentManager!!.beginTransaction()
            fragTransition2.replace(R.id.fragment_container, CourseEditFragment()).commit()
        }


        callCourses()
    }

    private fun callCourses() {

        val retroClient = Retrofit.Builder()
            .baseUrl("http://192.168.0.185:5555/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retroService = retroClient.create(RetroService::class.java)
        val call = retroService.getAllCourses()

        Log.d("COURSE CALL COURSE", "BEGINN")

        call.enqueue(object : Callback<List<RetroCourse>> {
            override fun onResponse(
                call: Call<List<RetroCourse>>,
                response: Response<List<RetroCourse>>
            ) {
                if (response.isSuccessful) {

                    Log.d("COURSE CALL COURSE", "response successful")
                    Log.d("COURSE CALL COURSE", "body: ${response.body().toString()}")
                    serverCourseData = response.body()!!
                    createAdapter()
                } else Log.d("COURSE CALL COURSE", " response not Successful: ${response.code()}")
            }

            override fun onFailure(call: Call<List<RetroCourse>>, t: Throwable) {
                Log.d(
                    "COURSE CALL COURSE",
                    "response failed - Cause: " + t.cause + " Message: " + t.message + " Locallized Message " + t.localizedMessage +
                            " StackTrace: " + t.stackTrace + " Suppressed: " + t.suppressed
                )
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

