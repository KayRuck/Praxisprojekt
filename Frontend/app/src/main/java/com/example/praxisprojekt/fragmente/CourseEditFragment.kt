package com.example.praxisprojekt.fragmente

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.praxisprojekt.R

class CourseEditFragment : Fragment() {

    companion object {
        fun newInstance() = CourseEditFragment()
    }

    private lateinit var viewModel: CourseEditViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.course_edit_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CourseEditViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
