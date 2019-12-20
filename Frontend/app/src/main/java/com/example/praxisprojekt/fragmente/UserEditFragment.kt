package com.example.praxisprojekt.fragmente

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.example.praxisprojekt.Constants
import com.example.praxisprojekt.MainActivity
import com.example.praxisprojekt.Mods
import com.example.praxisprojekt.R
import com.example.praxisprojekt.retrofit.RetroUser
import com.example.praxisprojekt.retrofit.RetrofitClient
import com.example.praxisprojekt.viewModels.UserEditViewModel
import kotlinx.android.synthetic.main.user_edit_fragment.*
import kotlinx.android.synthetic.main.user_edit_fragment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class UserEditFragment : Fragment() {

    private lateinit var rootView: View
    private var functionTAG = " "


    private val passwordPattern: Pattern = Pattern.compile(
        Constants.PATTERN.toString()
    )

    private lateinit var viewModel: UserEditViewModel
    private var update = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.user_edit_fragment, container, false)

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val userID = sharedPref.getInt(MainActivity.USER_ID, -1)
        if (userID != -1) {
            update = true
            rootView.editUserTextView.text = R.string.update_user_desc.toString()
            rootView.editUserButton.text = R.string.update_user.toString()
            callUserDataByID(userID)
            getModuleDataFromUser(userID)
        }

        initUserButton(userID)

        return rootView
    }

    private fun setUser(user: RetroUser){
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

    private fun initUserButton(userID: Int) {

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

            if (update) updateUser(userID, retroUser)
            else createUser(retroUser)
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

    private fun updateUser(userID: Int, retroUser: RetroUser) {

        val call: Call<RetroUser> = (RetrofitClient.getRetroService()).updateUser(userID, retroUser)

        call.enqueue(object : Callback<RetroUser> {
            override fun onResponse(call: Call<RetroUser>, response: Response<RetroUser>) {
                if (!response.isSuccessful) {
                    Log.d(
                        "UPDATE USER - NOT SUCCESS",
                        "Body: $retroUser Code: ${response.code()} / ${response.body()} / ${response.message()} /  ${response.errorBody()} / ${response.headers()}"
                    )
                }

                val retroUserResponse: RetroUser? = response.body()
                makeText(context, response.code().toString(), Toast.LENGTH_SHORT).show()
                Log.d(
                    "UPDATE USER SUCCESS",
                    " - Response Body: " + retroUserResponse + " Code: " + response.code()
                )

                val name = response.body()!!.username
                val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
                sharedPref.edit().putString(MainActivity.USER_NAME, name).apply()
            }

            override fun onFailure(call: Call<RetroUser>, t: Throwable) {
                Log.d(
                    "UPDATE USER FAIL",
                    " - Message: ${t.message} Cause: ${t.cause} / ${t.localizedMessage} / ${t.stackTrace} "
                )
            }
        })

    }

    private fun createUser(retroUser: RetroUser) {

        val call: Call<RetroUser> = (RetrofitClient.getRetroService()).createUser(retroUser)

        call.enqueue(object : Callback<RetroUser> {
            override fun onResponse(call: Call<RetroUser>, response: Response<RetroUser>) {
                if (!response.isSuccessful) {
                    Log.d(
                        "CREATE USER - NOT SUCCESS",
                        "Body: $retroUser Code: ${response.code()} / ${response.body()} / ${response.message()} /  ${response.errorBody()} / ${response.headers()}"
                    )
                }

                val retroUserResponse: RetroUser? = response.body()
                makeText(context, response.code().toString(), Toast.LENGTH_SHORT).show()
                Log.d(
                    "CREATE USER SUCCESS",
                    " - Response Body: " + retroUserResponse + " Code: " + response.code()
                )

                val name = response.body()!!.username

                val userId = retroUserResponse?.id ?: return

                val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
                sharedPref.edit().putInt(MainActivity.USER_ID, userId).apply()
                sharedPref.edit().putString(MainActivity.USER_NAME, name).apply()

            }

            override fun onFailure(call: Call<RetroUser>, t: Throwable) {
                makeText(context, t.message, Toast.LENGTH_SHORT).show()
                Log.d(
                    "CREATE USER FAIL",
                    " - Message: ${t.message} Cause: ${t.cause} / ${t.localizedMessage} / ${t.stackTrace} "
                )
            }

        })
    }

    private fun callUserDataByID(userID: Int) {
        functionTAG = "CALL USER BY ID - "

        val call = (RetrofitClient.getRetroService()).getUserById(userID)

        Log.d("COURSE CALL User", "BEGINN")

        call.enqueue(object : Callback<RetroUser> {
            override fun onFailure(call: Call<RetroUser>, t: Throwable) {
                Log.d(
                    functionTAG + "FAIL",
                    "Message: ${t.message} / CAUSE: ${t.cause}"
                )
            }

            override fun onResponse(call: Call<RetroUser>, response: Response<RetroUser>) {
                val body = response.body()

                if (!response.isSuccessful || body == null) {
                    Log.d(
                        functionTAG + "NOT SUCCESSFUL",
                        "UserID: $userID \n Response Body: $body Code: ${response.code()}"
                    )
                    return
                }
                Log.d(
                    functionTAG + "SUCCESSFUL",
                    "UserID: $userID Response Body: $body Code: ${response.code()}"
                )
                setUser(body)

            }
        })
    }

    private fun getModuleDataFromUser(id: Int) {

        val call = (RetrofitClient.getRetroService()).getModulesFromUser(id)

        call.enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (!response.isSuccessful) {
                    Log.d(
                        "GET MODULES - NOT SUCCESS",
                        "Body: ${response.body()} Code: ${response.code()} / ${response.message()} /  ${response.errorBody()}"
                    )
                    return
                }

                Log.d(
                    "GET MODULES - SUCCESS",
                    "Body: ${response.body()} Code: ${response.code()} /  ${response.message()} /  ${response.errorBody()}"
                )

                val modulListe = response.body()!!.toList()
                modulListe.forEach {
                    when (it) {
                        Mods.APMOD.title -> checkboxAP.isChecked = true
                        Mods.MATH1INFMOD.title -> checkboxMath1.isChecked = true
                        Mods.MATH2INFMOD.title -> checkboxMath2.isChecked = true
                        Mods.BWL1INFMOD.title -> checkboxBWL.isChecked = true
                    }
                }

            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Log.d(
                    "GET MODULES - Fail",
                    "Message: ${t.message} / CAUSE: ${t.cause}"
                )

            }
        })
    }

}
