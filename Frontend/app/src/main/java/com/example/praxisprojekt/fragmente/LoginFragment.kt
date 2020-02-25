package com.example.praxisprojekt.fragmente

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.example.praxisprojekt.MainActivity
import com.example.praxisprojekt.R
import com.example.praxisprojekt.retrofit.RetroUser
import com.example.praxisprojekt.viewModels.LoginViewModel
import kotlinx.android.synthetic.main.login_fragment.view.*

class LoginFragment : Fragment() {

    private lateinit var email: EditText
    private lateinit var password: EditText

    private lateinit var emailValid: TextView
    private lateinit var passValid: TextView

    private lateinit var rootView: View
    private lateinit var signup: TextView
    private lateinit var forgot: TextView
    private lateinit var viewModel: LoginViewModel

    var functionTAG = "LoginFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.login_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        Log.d(functionTAG, "Fragment loaded")

        //init viewModel observer
        viewModel.oldUser.observe(
            this,
            Observer { validateEMailInput(viewModel.retroOldUser) })
        viewModel.newUser.observe(
            this,
            Observer { validateUserInput(viewModel.retroNewUser) })


        email = rootView.loginEMailTV
        password = rootView.loginPasswordTV
        forgot = rootView.loginForgot
        signup = rootView.loginRegister
        emailValid = rootView.valemail
        passValid = rootView.valpassword


        val btnLogin = rootView.loginButton
        btnLogin.setOnClickListener {
            Log.d(functionTAG, "SignUp Button clicked.")
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
            val userId = sharedPref.getInt(MainActivity.USER_ID, -1)

            if (userId != -1) viewModel.getUserData(userId)
            else viewModel.checkEmail(email.text.toString())

        }

        signup.setOnClickListener {
            Log.d(functionTAG, "SignUp Button clicked.")
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
            val userId = sharedPref.getInt(MainActivity.USER_ID, -1)
            Log.d(functionTAG, "USER_ID: $userId")

            if (userId != -1) viewModel.getUserData(userId)
            else viewModel.checkEmail(email.text.toString())
        }


        forgot.setOnClickListener {

            // TODO: Neues Fragment Forgot
            // TODO: Möglichkeit des Passwort ändern --> E-Mail an die E-Mail adresse
            // Überprüfen ob die Adresse da ist

        }


        return rootView
    }

    private fun validateEMailInput(retroUser: RetroUser) {

        when {
            retroUser.isNotValid() -> {
                Log.d(
                    functionTAG + "Not Such E-Mail",
                    "There is no such E-Mail ${email.text} in the Database"
                )
                emailValid.setText("Keine passende E-Mail gefunden")
                passValid.setText(" ")

            }
            password.text.toString() != retroUser.password -> {
                Log.d(
                    functionTAG + "Password invalid",
                    "Wrong Password ${password.text} for User with this E-Mail ${email.text}"
                )
                emailValid.setText("E-Mail vorhanden")
                passValid.setText("Passwort ist falsch")

            }
            else -> {
                val id: Int = retroUser.id ?: 1
                val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
                sharedPref.edit().putInt(MainActivity.USER_ID, id).apply()
                sharedPref.edit().putString(MainActivity.USER_NAME, retroUser.username).apply()

                Log.d(
                    functionTAG + "new ID in Shared Pref",
                    "Set the new ID ($id) from the Main Database in shared Pref"
                )
                startMainActivity()
            }
        }
    }

    private fun validateUserInput(retroUser: RetroUser) {
        when {
            email.text.toString() != retroUser.email -> {
                Log.d(
                    functionTAG + "E-Mail invalid",
                    "Wrong E-Mail ${email.text} / ${retroUser.email} for the saved shared pref User"
                )
            }
            password.text.toString() != retroUser.password -> {
                Log.d(
                    functionTAG + "Password invalid",
                    "Wrong Password ${password.text} / ${retroUser.password} for the saved shared pref User"
                )
            }
            else -> startMainActivity()
        }
        Log.d(
            functionTAG + "E-Mail and Password valid",
            "E-Mail and Password matches the saved shared pref User"
        )
    }

    private fun startMainActivity() {
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
    }
}
