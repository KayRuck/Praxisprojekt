package com.example.praxisprojekt.fragmente

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.praxisprojekt.R
import com.example.praxisprojekt.retrofit.RetroCourse
import com.example.praxisprojekt.viewModels.CourseDetailViewModel
import kotlinx.android.synthetic.main.course_detail_fragment.view.*

class CourseDetailFragment (val retroCourse : RetroCourse) : Fragment() {

    companion object {
//        fun newInstance(retroCourse : RetroCourse): CourseDetailFragment {
//            val frag = CourseDetailFragment()
//            frag.retroCourse = retroCourse
//            return frag
//        }
        val TAG = CourseDetailFragment::class.java.canonicalName.toString()
    }

    private lateinit var viewModel: CourseDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.course_detail_fragment, container, false)

        // TODO: Andere Text Views zu weisen
        Log.d(TAG, "Fragment loaded")
        rootView.detailCourseTitleTV.text = retroCourse.title

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CourseDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
