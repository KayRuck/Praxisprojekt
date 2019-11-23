package com.example.praxisprojekt.fragmente

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.makeText

import com.example.praxisprojekt.R
import com.example.praxisprojekt.viewModels.UserEditViewModel
import kotlinx.android.synthetic.main.user_edit_fragment.view.*
import kotlinx.android.synthetic.main.user_fragment.view.*

class UserEditFragment : Fragment() {

    companion object {
        fun newInstance() = UserEditFragment()
    }

    private lateinit var viewModel: UserEditViewModel

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.user_edit_fragment, container, false)

        rootView.editUserButton.setOnClickListener {
            val editName = rootView.editUserName.text.toString()
            val editEMAIL = rootView.editUserEMail.text.toString()
            val editDesc = rootView.editUserDescrition.text.toString()
            val endResult = rootView.editUserTV
            endResult.text = ("Name: $editName E-Mail: $editEMAIL Beschreibung: $editDesc")
        }


        // TODO Farben noch richtig machen :<
        if (rootView.editUserPassword == rootView.editUserPassword2) {
            makeText(context, "Super Passwort Gleich", Toast.LENGTH_SHORT).show()
            rootView.editUserPassword.setBackgroundColor(R.color.green)
        } else {
            makeText(context, "Schlecht Passwort unterschiedlich", Toast.LENGTH_SHORT).show()
            rootView.editUserPassword.setBackgroundColor(R.color.red)
        }


        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(UserEditViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
