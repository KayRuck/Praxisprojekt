package com.example.praxisprojekt.fragmente

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.praxisprojekt.Constants
import com.example.praxisprojekt.R
import com.example.praxisprojekt.retrofit.RetroService
import com.example.praxisprojekt.retrofit.RetroUser
import com.example.praxisprojekt.viewModels.UserEditViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.user_edit_fragment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
            val editEmail = rootView.editUserEMail.text.toString()
            val editPass = rootView.editUserPassword.text.toString()
            val editDesc = rootView.editUserDescrition.text.toString()
            val editContact = rootView.editUserContact.text.toString()
            rootView.editUserTV.text = ("Name: $editName E-Mail: $editEmail " +
                    "Beschreibung: $editDesc Kontakt: $editContact " +
                    "Password: $editPass ")


            val retroUser = RetroUser(editName, " ",editPass, editEmail, editContact, 0.0, 0.0)
            Log.d("CREATE USER: ", retroUser.toString())
            createUser(retroUser)
        }

/*
        // TODO Farben noch richtig machen :<
        if (rootView.editUserPassword == rootView.editUserPassword2) {
            makeText(context, "Super Passwort Gleich", Toast.LENGTH_SHORT).show()
            rootView.editUserPassword2.setBackgroundColor(R.color.green)
        } else {
            makeText(context, "Schlecht Passwort unterschiedlich", Toast.LENGTH_SHORT).show()
            rootView.editUserPassword2.setBackgroundColor(R.color.red)
        }
*/

        return rootView
    }

    private fun createUser(retroUser: RetroUser) {
        val gson : Gson = GsonBuilder().setLenient().create()


        val retroClient = Retrofit.Builder()
            .baseUrl(Constants.API_BASE_URL.string)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()


        val retroService = retroClient.create(RetroService::class.java)
        val call: Call<RetroUser> = retroService.createUser(retroUser)

        call.enqueue(object : Callback<RetroUser> {
            override fun onResponse(call: Call<RetroUser>, response: Response<RetroUser>) {
                if (!response.isSuccessful) {
//                    makeText(context, response.code(), Toast.LENGTH_SHORT).show()
                    Log.d(
                        "CREATE USER - NOT SUCCESS",
                        " Body: " + retroUser.toString() + " Code: " + response.code()
                    )
                    return
                }

                val retroUserResponse: RetroUser = response.body()!!
                makeText(context, response.code(), Toast.LENGTH_SHORT).show()
                Log.d(
                    "CREATE USER SUCCESS",
                    " - Response Body: " + retroUserResponse + " Code: " + response.code()
                )
            }

            override fun onFailure(call: Call<RetroUser>, t: Throwable) {
                makeText(context, t.message, Toast.LENGTH_SHORT).show()
                Log.d("CREATE USER FAIL", " - Message: ${t.message} Cause: ${t.cause}")
            }

        })


    }





    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(UserEditViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
