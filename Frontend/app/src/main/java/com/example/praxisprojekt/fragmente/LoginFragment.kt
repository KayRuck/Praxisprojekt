package com.example.praxisprojekt.fragmente

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
    private lateinit var rootView: View
    private lateinit var signup: TextView
    private lateinit var forgot: TextView
    private lateinit var viewModel: LoginViewModel
    private var retroEmail: String = ""
    private var retroPass: String = ""
    private var retroID: Int? = null

    private var functionTAG = " "

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.login_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        //init viewModel observer
        viewModel.oldUser.observe(this, Observer { setUserData(viewModel.retroOldUser) })
        viewModel.newUser.observe(this, Observer { setUserData(viewModel.retroNewUser) })


        email = rootView.loginEMailTV
        password = rootView.loginPasswordTV
        forgot = rootView.loginForgot
        signup = rootView.loginRegister


        val btnLogin = rootView.findViewById<Button>(R.id.loginButton)
        btnLogin.setOnClickListener {

            if (loginDataCheck(email.text.toString(), password.text.toString())) {
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
            } else Toast.makeText(context, "Falsche Email oder Passwort", Toast.LENGTH_LONG).show()

        }

        signup.setOnClickListener {

            val fragTransaction1 = fragmentManager!!.beginTransaction()
            fragTransaction1.replace(R.id.fragment_container_login, UserEditFragment()).commit()

        }


        forgot.setOnClickListener {

            // TODO: Neues Fragment Forgot
            // TODO: Möglichkeit des Passwort ändern --> E-Mail an die E-Mail adresse
            // Überprüfen ob die Adresse da ist

        }


        return rootView
    }

    // TODO: Auslagern in das Viewmodel
    private fun loginDataCheck(email: String, password: String): Boolean {
        val checker: Boolean
        functionTAG = "LOGIN DATA CHECK - "

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val userId = sharedPref.getInt(MainActivity.USER_ID, -1)
        if (userId != -1) {
            viewModel.getUserData(userId)
            checker = when {
                email != retroEmail -> {
                    Log.d(
                        functionTAG + "E-Mail invalide",
                        "Wrong E-Mail $email / $retroEmail for the saved shared pref User"
                    )
                    false
                }
                password != retroPass -> {
                    Log.d(
                        functionTAG + "Password invalide",
                        "Wrong Password $password / $retroPass for the saved shared pref User"
                    )

                    false
                }
                else -> true
            }
            Log.d(
                functionTAG + "E-Mail and Password valid",
                "E-Mail and Password matches the saved shared pref User"
            )
        } else {
            viewModel.checkEmail(email)
            checker = when {
                retroID == null -> {
                    Log.d(
                        functionTAG + "Not Such E-Mail",
                        "There is no such E-Mail $retroEmail in the Database"
                    )
                    false
                }
                password != retroPass -> {
                    Log.d(
                        functionTAG + "Password invalide",
                        "Wrong Password $retroPass for the saved shared pref User"
                    )
                    false
                }
                else -> {
                    if (id != null) sharedPref.edit().putInt(MainActivity.USER_ID, id).apply()
                    Log.d(
                        functionTAG + "new ID in Shared Pref",
                        "Set the new ID ($id) from the Main Database in shared Pref"
                    )
                    true
                }
            }
        }

        return checker
    }

    private fun setUserData(retroUser: RetroUser) {
        functionTAG = "SET EMAIL"

        retroID = retroUser.id
        retroEmail = retroUser.email
        retroPass = retroUser.password
        Log.d(
            functionTAG, "Value of EMail Property $retroEmail \n" +
                    "Value of Password Property $retroPass"
        )
    }


}
