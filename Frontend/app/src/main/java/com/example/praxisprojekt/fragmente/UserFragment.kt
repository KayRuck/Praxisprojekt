package com.example.praxisprojekt.fragmente

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.example.praxisprojekt.MainActivity
import com.example.praxisprojekt.R
import com.example.praxisprojekt.retrofit.RetroUser
import com.example.praxisprojekt.retrofit.RetrofitClient
import com.example.praxisprojekt.viewModels.UserViewModel
import kotlinx.android.synthetic.main.user_fragment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserFragment : Fragment() {

    private lateinit var rootView: View
    var modulListe = listOf<String>()

    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.user_fragment, container, false)

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val userID = sharedPref.getInt(MainActivity.USER_ID, -1)

        getModulesFromUser(userID)

        callUserByID(userID, rootView)
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

        return rootView
    }

    private fun getModulesFromUser(id: Int) {

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

                modulListe = response.body()!!.toList()
                rootView.userModuleList.text = modulListe.toString()

            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {

            }
        })
    }


    private fun callUserByID(id: Int, rootView: View) {

        val call = (RetrofitClient.getRetroService()).getUserById(id)


        call.enqueue(object : Callback<RetroUser> {
            override fun onFailure(call: Call<RetroUser>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<RetroUser>, response: Response<RetroUser>) {
                val desc = response.body()!!.description


                rootView.userUsername.text = response.body()!!.username
                if (desc == " " || desc == "") rootView.userDescription.text = R.string.noSuchDescription.toString()
                else rootView.userDescription.text = response.body()!!.description

            }
        })
    }


}

