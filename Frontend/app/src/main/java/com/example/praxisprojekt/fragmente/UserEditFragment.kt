package com.example.praxisprojekt.fragmente

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
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
import kotlinx.android.synthetic.main.user_edit_fragment.view.editUserButton
import kotlinx.android.synthetic.main.user_edit_fragment.view.editUserContact
import kotlinx.android.synthetic.main.user_edit_fragment.view.editUserDescrition
import kotlinx.android.synthetic.main.user_edit_fragment.view.editUserEMail
import kotlinx.android.synthetic.main.user_edit_fragment.view.editUserName
import kotlinx.android.synthetic.main.user_edit_fragment.view.editUserPassword
import kotlinx.android.synthetic.main.user_edit_fragment.view.editUserPassword2
import kotlinx.android.synthetic.main.user_edit_fragment.view.editUserTextView
import kotlinx.android.synthetic.main.user_edit_fragment2.view.*
import java.util.regex.Pattern

class UserEditFragment : Fragment() {

    private lateinit var rootView: View
    private lateinit var viewModel: UserEditViewModel
    private var functionTAG = " "
    private var update = false

    private val moduleList = mutableListOf(
        Mods.APMOD.title,
        Mods.MATH1INFMOD.title,
        Mods.MATH2INFMOD.title,
        Mods.MATHINFMOD.title,
        Mods.BWL1INFMOD.title
    )

    private val passwordPattern: Pattern = Pattern.compile(Constants.PATTERN.toString())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.user_edit_fragment2, container, false)
        viewModel = ViewModelProviders.of(this).get(UserEditViewModel::class.java)

        viewModel.currentUser.observe(this, Observer { setUser(viewModel.updateUser) })
        viewModel.currentModules.observe(this, Observer { setModuleList(viewModel.moduleList) })

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val userID = sharedPref.getInt(MainActivity.USER_ID, -1)

        if (userID != -1) setUpdate(userID)

        initUserButton(userID, sharedPref)
        initMultiAutoCompleteTextView()

        return rootView
    }

    private fun setUpdate(userID: Int) {
        update = true
        rootView.editUserTextView.setText(R.string.update_user_desc)
        rootView.editUserButton.setText(R.string.update_user)
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

    private fun initUserButton(userID: Int, sharedPreferences: SharedPreferences) {

        rootView.editUserButton.setOnClickListener {
            val editName = rootView.editUserName.text.toString()
            val editEmail = rootView.editUserEMail.text.toString()
            val editPass = rootView.editUserPassword.text.toString()
            val editDesc = rootView.editUserDescrition.text.toString()
            val editContact = rootView.editUserContact.text.toString()
            val list = showInput()

            validatePassword(editPass)
            validateEMail(editEmail)

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
                    setModuleList(list)
                )
            Log.d("CREATE USER: ", retroUser.toString())

            rootView.editUserPassword.setOnEditorActionListener { _, _, _ ->
                controlPassword()
            }
            rootView.editUserPassword2.setOnEditorActionListener { _, _, _ ->
                controlPassword()
            }

            if (update) {
                viewModel.updateUser(userID, retroUser, sharedPreferences)
                val fragTransaction1 = fragmentManager!!.beginTransaction()
                fragTransaction1.replace(R.id.fragment_container, UserFragment(1)).commit()
            } else {
                viewModel.createUser(retroUser, sharedPreferences)
                val fragTransaction2 = fragmentManager!!.beginTransaction()
                fragTransaction2.replace(R.id.fragment_container, LoginFragment()).commit()
            }


        }
    }

    // TODO: Add Functional Error in Option 1 and 2
    private fun validateEMail(email: String): Boolean {
        functionTAG = "VALIDATE E-MAIL - "

        val valemail = rootView.validateEmail

        return when {
            email.isEmpty() -> {
                Log.d(functionTAG + "INVALID", "E-Mail Needed")

                valemail.setTextColor(Color.RED)
                valemail.setText(getString(R.string.enterEmail))

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

    private fun initMultiAutoCompleteTextView() {
        val multi = rootView.multiAutoCompleteTextView

        Log.w("CONTEXT", "Context = $context")

        context?.let {
            val sAdapter = ArrayAdapter<String>(it, android.R.layout.simple_list_item_1, moduleList)
            val mToken = MultiAutoCompleteTextView.CommaTokenizer()

            multi.setAdapter(sAdapter)
            multi.setTokenizer(mToken)
        }
    }

    private fun showInput(): List<String> {
        val multi = rootView.multiAutoCompleteTextView
        val input = multi.text.toString().trim()
        val singleInput = input.split(",")
        val builder = StringBuilder()

        singleInput.forEach { builder.append("Item: $it \n") }

        Log.d(functionTAG, builder.toString())

        return singleInput
    }

    private fun setModuleList(list: List<String>): MutableList<Int> {
        val modules = mutableListOf<Int>()

        list.forEach {
            when (it) {
                Mods.APMOD.title -> modules.add(Mods.APMOD.id)
                Mods.MATH1INFMOD.title -> modules.add(Mods.MATH1INFMOD.id)
                Mods.MATH2INFMOD.title -> modules.add(Mods.MATH2INFMOD.id)
                Mods.MATHINFMOD.title -> modules.add(Mods.MATHINFMOD.id)
                Mods.BWL1INFMOD.title -> modules.add(Mods.BWL1INFMOD.id)
            }
        }
        return modules
    }

}
