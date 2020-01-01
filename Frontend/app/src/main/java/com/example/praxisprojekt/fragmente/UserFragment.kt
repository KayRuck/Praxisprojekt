package com.example.praxisprojekt.fragmente

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.example.praxisprojekt.MainActivity
import com.example.praxisprojekt.R
import com.example.praxisprojekt.retrofit.RetroUser
import com.example.praxisprojekt.viewModels.UserViewModel
import kotlinx.android.synthetic.main.user_fragment.view.*

class UserFragment : Fragment() {

    private lateinit var rootView: View

    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.user_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        viewModel.modules.observe(this, Observer { setModules(viewModel.moduleList) })
        viewModel.user.observe(this, Observer { setDescription(viewModel.retroUser) })

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val userID = sharedPref.getInt(MainActivity.USER_ID, -1)

        viewModel.callData(userID)

        initContactButton()

        return rootView
    }

    private fun initContactButton() {
        val contactBtn = rootView.userContactButton

        contactBtn.setOnClickListener {

            val builder = AlertDialog.Builder(context!!, R.style.CustomAlertDialog)
            builder.setMessage("Kontaktmöglichkeit auswählen")
            builder.setCancelable(true)

            builder.setPositiveButton("Handynummer") { dialog, _ -> dialog.cancel() }
            builder.setNegativeButton("E-Mailadresse") { dialog, _ -> dialog.cancel() }

            val alert = builder.create()
            alert.show()
        }
    }

    private fun setModules(modules: List<String>) {
        rootView.userModuleList.text = modules.toString()
    }

    private fun setDescription(retroUser: RetroUser) {
        val name = retroUser.username
        val description = retroUser.description

        rootView.userUsername.text = name
        if (description == " " || description == "") rootView.userDescription.text =
            R.string.noSuchDescription.toString()
        else rootView.userDescription.text = description
    }

}

