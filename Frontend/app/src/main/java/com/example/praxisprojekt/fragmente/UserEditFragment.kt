package com.example.praxisprojekt.fragmente

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.praxisprojekt.R
import com.example.praxisprojekt.viewModels.UserEditViewModel

class UserEditFragment : Fragment() {

    companion object {
        fun newInstance() = UserEditFragment()
    }

    private lateinit var viewModel: UserEditViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.user_edit_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(UserEditViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
