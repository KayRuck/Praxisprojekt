package com.example.praxisprojekt.fragmente

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.praxisprojekt.MainActivity
import com.example.praxisprojekt.R
import com.example.praxisprojekt.adapter.CourseAdapter
import com.example.praxisprojekt.retrofit.RetroCourse
import com.example.praxisprojekt.viewModels.CourseViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CourseFragment(private var course: Int) : Fragment() {

    private lateinit var viewModel: CourseViewModel
    private lateinit var rootView: View
    private lateinit var rec: RecyclerView
    private lateinit var mainActivity: MainActivity
    private var ownCourse: Boolean = false

    companion object {
        val TAG = CourseFragment::class.java.canonicalName.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.course_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(CourseViewModel::class.java)

        // init viewModel observer
        viewModel.showUserCourses.observe(this, Observer { createAdapter(viewModel.userRetroCourse) })
        viewModel.showAllCourses.observe(this, Observer { createAdapter(viewModel.allRetroCourse) } )
        viewModel.showMatchedCourses.observe(this, Observer { createAdapter(viewModel.matchedRetroCourse) } )

        rec = rootView.findViewById(R.id.recyclerView_display)
        mainActivity = activity as MainActivity

        buildRecyclerView(rootView)

        return rootView
    }

    private fun buildRecyclerView(view: View) {

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val userID = sharedPref.getInt(MainActivity.USER_ID, 1)

        val lin = LinearLayoutManager(context)
        rec.layoutManager = lin

        when (course) {
            1 -> {
                viewModel.callAllCourses()
                ownCourse = false
            }
            2 -> {
                viewModel.callMatchedCourses(userID)
                ownCourse = false
            }
            3 -> {
                viewModel.callUserCourses(userID)
                ownCourse = true
            }
        }

        val reloadBtn: FloatingActionButton = view.findViewById(R.id.displayButton_l2)
        reloadBtn.setOnClickListener {
            val fragTransaction1 = fragmentManager!!.beginTransaction()
            fragTransaction1.replace(R.id.fragment_container, CourseFragment(course)).commit()
        }

        val editBtn: FloatingActionButton = view.findViewById(R.id.displayButton_l)
        editBtn.setOnClickListener {
            val fragTransition2 = fragmentManager!!.beginTransaction()
            fragTransition2.replace(R.id.fragment_container, CourseEditFragment()).commit()
        }

    }

    private fun createAdapter(listData: List<RetroCourse>) {
        Log.d(TAG, "Adapter created.")

        val courseAdapter = CourseAdapter(listData) {
            val detailData = listData[it]
            Log.d("$TAG - CREATE ADAPTER", "Course $it clicked")
            mainActivity.loadFragment(CourseDetailFragment(detailData, ownCourse))
        }
        rec.adapter = courseAdapter
        rec.itemAnimator = DefaultItemAnimator()
    }

}