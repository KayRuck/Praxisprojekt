package com.example.praxisprojekt.fragmente

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.example.praxisprojekt.Constants
import com.example.praxisprojekt.MainActivity
import com.example.praxisprojekt.Mods
import com.example.praxisprojekt.R
import com.example.praxisprojekt.retrofit.RetroService
import com.example.praxisprojekt.retrofit.RetroUser
import com.example.praxisprojekt.viewModels.UserEditViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.user_edit_fragment.*
import kotlinx.android.synthetic.main.user_edit_fragment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.regex.Pattern

class UserEditFragment : Fragment() {

    private lateinit var rootView: View

    //TODO: Auslagern
    private val PASSWORD_PATTERN: Pattern = Pattern.compile(
        "^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$"
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
            rootView.editUserTextView.text = "Bearbeite dein Profil"
            rootView.editUserButton.text = "Bearbeiten"
            callUserDataByID(userID, rootView)
            getModuleDataFromUser(userID)

        }

        rootView.editUserButton.setOnClickListener {
            val editName = rootView.editUserName.text.toString()
            val editEmail = rootView.editUserEMail.text.toString()
            val editPass = rootView.editUserPassword.text.toString()
            val editDesc = rootView.editUserDescrition.text.toString()
            val editContact = rootView.editUserContact.text.toString()


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
                    getModulelist()
                )
            Log.d("CREATE USER: ", retroUser.toString())

            rootView.editUserPassword.setOnEditorActionListener { v, actionId, event ->
                controlPassword(rootView)
            }
            rootView.editUserPassword2.setOnEditorActionListener { v, actionId, event ->
                controlPassword(rootView)
            }

            if (update) updateUser(userID, retroUser)
            else createUser(retroUser)
        }
        return rootView
    }


    //TODO: Add Functional Error in Option 1 and 2
    private fun validateEMail(email: String): Boolean {

        return when {
            email.isEmpty() -> {
                Log.d("E-MAIL INVALID", "Change E-Mail - is null")
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                Log.d("E-MAIL INVALID", "Change E-Mail - A . oder @ is Missing")
                false
            }
            else -> {
                Log.d("E-MAIL VALID", "Everything is Good")
                true
            }
        }

    }

    // TODO: Add Functional ERROR in Option 1 and 2
    // OR https://android.jlelse.eu/quickly-easily-validating-your-text-with-easy-validation-498d7eb54b0b
    private fun validatePasswort(password: String): Boolean {

        return when {
            password.isEmpty() -> {
                Log.d("PASSWORD INVALID", "Change Password - is null")
                false
            }
            !PASSWORD_PATTERN.matcher(password).matches() -> {
                Log.d(
                    "E-MAIL INVALID",
                    "Change Password - A Upper or Lower Case, Special character or Number is Missing"
                )
                false
            }
            else -> {
                Log.d("PASSWORD VALID", "Everything is Good")
                true
            }
        }

    }


    @SuppressLint("ResourceAsColor")
    private fun controlPassword(rootView: View): Boolean {
        if (rootView.editUserPassword == rootView.editUserPassword2) {
            makeText(context, "Super Passwort Gleich", Toast.LENGTH_SHORT).show()
            rootView.editUserPassword2.setBackgroundColor(R.color.light_green)
        } else {
            makeText(context, "Schlecht Passwort unterschiedlich", Toast.LENGTH_SHORT).show()
            rootView.editUserPassword2.setBackgroundColor(R.color.red)
        }
        return true
    }

    private fun updateUser(userID: Int, retroUser: RetroUser) {
        val gson: Gson = GsonBuilder().setLenient().create()

        val retroClient = Retrofit.Builder()
            .baseUrl(Constants.API_BASE_URL.string)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val retroService = retroClient.create(RetroService::class.java)
        val call: Call<RetroUser> = retroService.updateUser(userID, retroUser)

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
        val gson: Gson = GsonBuilder().setLenient().create()


        val retroClient = Retrofit.Builder()
            .baseUrl(Constants.API_BASE_URL.string)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()


        val retroService = retroClient.create(RetroService::class.java)
        val call: Call<RetroUser> = retroService.createUser(retroUser)

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

    private fun getModulelist(): List<Int> {
        val modules = mutableListOf<Int>()
        if (checkboxAP.isChecked) modules.add(Mods.APMOD.id)
        if (checkboxMath1.isChecked) modules.add(Mods.MATH1INFMOD.id)
        if (checkboxMath2.isChecked) modules.add(Mods.MATH2INFMOD.id)
        if (checkboxBWL.isChecked) modules.add(Mods.BWL1INFMOD.id)
        return modules
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(UserEditViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun callUserDataByID(id: Int, rootView: View) {


        val gson: Gson = GsonBuilder().setLenient().create()

        val retroClient = Retrofit.Builder()
            .baseUrl(Constants.API_BASE_URL.string)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val retroService = retroClient.create(RetroService::class.java)
        val call = retroService.getUserById(id)


        Log.d("COURSE CALL User", "BEGINN")

        call.enqueue(object : Callback<RetroUser> {
            override fun onFailure(call: Call<RetroUser>, t: Throwable) {
                Log.d("COURSE CALL User", "FAIL Message ${t.message} / ${t.cause} ")

            }

            override fun onResponse(call: Call<RetroUser>, response: Response<RetroUser>) {
                val body = response.body()!!

                rootView.editUserName.setText(body.username)
                rootView.editUserEMail.setText(body.email)
                rootView.editUserContact.setText(body.contact)
                rootView.editUserPassword.setText(body.password)
                rootView.editUserPassword2.setText(body.password)

                if (body.description.isBlank())
                    rootView.editUserDescrition.setText("Keine Beschreibung vorhanden")
                else rootView.editUserDescrition.setText(body.description)
            }
        })
    }

    private fun getModuleDataFromUser(id: Int) {

        val gson: Gson = GsonBuilder().setLenient().create()

        val retroClient = Retrofit.Builder()
            .baseUrl(Constants.API_BASE_URL.string)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val retroService = retroClient.create(RetroService::class.java)
        val call = retroService.getModulesFromUser(id)

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
                modulListe.forEach{
                    when(it) {
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
