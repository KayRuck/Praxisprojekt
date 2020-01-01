package com.example.praxisprojekt.fragmente

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.example.praxisprojekt.Constants
import com.example.praxisprojekt.MainActivity
import com.example.praxisprojekt.Mods
import com.example.praxisprojekt.R
import com.example.praxisprojekt.retrofit.RetroUser
import com.example.praxisprojekt.viewModels.UserEditViewModel
import kotlinx.android.synthetic.main.user_edit_fragment.*
import kotlinx.android.synthetic.main.user_edit_fragment.view.*
import java.util.regex.Pattern

class UserEditFragment : Fragment() {

    private lateinit var rootView: View
    private lateinit var viewModel: UserEditViewModel
    private var functionTAG = " "
    private var update = false


    private val passwordPattern: Pattern = Pattern.compile(
        Constants.PATTERN.toString()
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.user_edit_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(UserEditViewModel::class.java)

        viewModel.currentUser.observe(this, Observer { setUser(viewModel.updateUser) })
        viewModel.currentModules.observe(this, Observer { setModules(viewModel.moduleList) })

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val userID = sharedPref.getInt(MainActivity.USER_ID, -1)
        if (userID != -1) setUpdate(userID)
        initUserButton(userID, sharedPref)

        return rootView
    }

    private fun setUpdate(userID: Int) {
        update = true
        rootView.editUserTextView.text = R.string.update_user_desc.toString()
        rootView.editUserButton.text = R.string.update_user.toString()
        viewModel.editData(userID)
    }

    private fun setUser(user: RetroUser) {
        rootView.editUserName.setText(user.username)
        rootView.editUserEMail.setText(user.email)
        rootView.editUserContact.setText(user.contact)
        rootView.editUserPassword.setText(user.password)
        rootView.editUserPassword2.setText(user.password)

        if (user.description.isBlank())
            rootView.editUserDescrition.setText(R.string.noSuchDescription)
        else rootView.editUserDescrition.setText(user.description)
    }

    private fun getModuleList(): List<Int> {
        val modules = mutableListOf<Int>()
        if (checkboxAP.isChecked) modules.add(Mods.APMOD.id)
        if (checkboxMath1.isChecked) modules.add(Mods.MATH1INFMOD.id)
        if (checkboxMath2.isChecked) modules.add(Mods.MATH2INFMOD.id)
        if (checkboxBWL.isChecked) modules.add(Mods.BWL1INFMOD.id)
        return modules
    }

    private fun initUserButton(userID: Int, sharedPreferences: SharedPreferences) {

        rootView.editUserButton.setOnClickListener {
            val editName = rootView.editUserName.text.toString()
            val editEmail = rootView.editUserEMail.text.toString()
            val editPass = rootView.editUserPassword.text.toString()
            val editDesc = rootView.editUserDescrition.text.toString()
            val editContact = rootView.editUserContact.text.toString()

            validatePassword(editPass)
            validateEMail(editEmail)

            rootView.editUserTV.text = ("Name: $editName E-Mail: $editEmail " +
                    "Beschreibung: $editDesc Kontakt: $editContact " +
                    "Password: $editPass ")

            val retroUser =
                RetroUser(
                    null,
                    editName,
                    editDesc,
                    editPass,
                    editEmail,
                    editContact,
                    0.0,
                    0.0,
                    getModuleList()
                )
            Log.d("CREATE USER: ", retroUser.toString())

            rootView.editUserPassword.setOnEditorActionListener { _, _, _ ->
                controlPassword()
            }
            rootView.editUserPassword2.setOnEditorActionListener { _, _, _ ->
                controlPassword()
            }

            if (update) viewModel.updateUser(userID, retroUser, sharedPreferences)
            else viewModel.createUser(retroUser, sharedPreferences)


        }
    }

    // TODO: Add Functional Error in Option 1 and 2
    private fun validateEMail(email: String): Boolean {
        functionTAG = "VALIDATE E-MAIL - "

        return when {
            email.isEmpty() -> {
                Log.d(functionTAG + "INVALID", "E-Mail Needed")
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                Log.d(functionTAG + "INVALID", "Change E-Mail - A . oder @ is Missing")
                false
            }
            else -> {
                Log.d(functionTAG + "VALID", "Everything is Good")
                true
            }
        }

    }

    // TODO: Add Functional ERROR in Option 1 and 2
    private fun validatePassword(password: String): Boolean {
        functionTAG = "VALIDATE PASSWORD - "

        return when {
            password.isEmpty() -> {
                Log.d(functionTAG + "INVALID", "Password Needed")
                false
            }
            !passwordPattern.matcher(password).matches() -> {
                Log.d(
                    functionTAG + "INVALID",
                    "Change Password - A Upper or Lower Case, Special character or Number is Missing"
                )
                false
            }
            else -> {
                Log.d(functionTAG + "VALID", "Everything is Good")
                true
            }
        }

    }

    private fun controlPassword(): Boolean {
        functionTAG = "CONTROL PASSWORD - "

        if (rootView.editUserPassword == rootView.editUserPassword2) {
            Log.d(
                functionTAG + "VALID",
                "Password first and second equal"
            )
            rootView.editUserPassword2.setBackgroundColor(Color.parseColor("#CFE2CF"))
        } else {
            Log.d(
                functionTAG + "INVALID",
                "Password first and second different"
            )
            rootView.editUserPassword2.setBackgroundColor(Color.parseColor("#E2A6A1"))
        }
        return true
    }

    private fun setModules(moduleList: List<String>) {
        moduleList.forEach {
            when (it) {
                Mods.APMOD.title -> checkboxAP.isChecked = true
                Mods.MATH1INFMOD.title -> checkboxMath1.isChecked = true
                Mods.MATH2INFMOD.title -> checkboxMath2.isChecked = true
                Mods.BWL1INFMOD.title -> checkboxBWL.isChecked = true
            }
        }

    }




}
