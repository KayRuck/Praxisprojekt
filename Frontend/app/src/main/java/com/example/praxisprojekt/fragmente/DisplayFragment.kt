package com.example.praxisprojekt.fragmente

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.praxisprojekt.R
import com.example.praxisprojekt.viewModels.Display

class DisplayFragment : Fragment() {

    companion object {
        fun newInstance() = DisplayFragment()
    }

    private lateinit var viewModel: Display

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.display_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(Display::class.java)
        // TODO: Use the ViewModel
    }

}
