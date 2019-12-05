package com.example.praxisprojekt.fragmente

import android.annotation.SuppressLint
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
import com.example.praxisprojekt.retrofit.RetroService
import com.example.praxisprojekt.retrofit.RetroUser
import com.example.praxisprojekt.viewModels.UserViewModel
import kotlinx.android.synthetic.main.user_fragment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class UserFragment : Fragment() {

    var modulListe = listOf<String>()

    companion object {
        fun newInstance() = UserFragment()
    }

    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.user_fragment, container, false)

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val userID = sharedPref.getInt(MainActivity.USER_ID, -1)

        // TODO check if the id is valid > 0

        getModulesFromUser(userID)

        rootView.userModuleList.text = modulListe.toString()

        callUserByID(userID, rootView)
        val contactBtn = rootView.userContactButton

        contactBtn.setOnClickListener {

            val builder = AlertDialog.Builder(context!!, R.style.CustomAlertDialog)
            builder.setMessage("Kontaktmöglichkeit auswählen")
            builder.setCancelable(true)

            builder.setPositiveButton("Handynummer") { dialog, id -> dialog.cancel() }
            builder.setNegativeButton("E-Mailadresse") { dialog, id -> dialog.cancel() }

            val alert = builder.create()
            alert.show()
        }

        return rootView
    }

    private fun getModulesFromUser(id: Int) {
        val retroClient = Retrofit.Builder()
            .baseUrl("http://192.168.0.185:5555/")
            .addConverterFactory(GsonConverterFactory.create())
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
                }

                Log.d(
                    "GET MODULES - SUCCESS",
                    "Body: ${response.body()} Code: ${response.code()} /  ${response.message()} /  ${response.errorBody()}"
                )
                modulListe = response.body()!!.toList()

            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {


            }

        })


    }


    private fun callUserByID(id: Int, rootView: View) {

        val retroClient = Retrofit.Builder()
            .baseUrl("http://192.168.0.185:5555/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retroService = retroClient.create(RetroService::class.java)
        val call = retroService.getUserById(id)


        Log.d("COURSE CALL User", "BEGINN")

        call.enqueue(object : Callback<RetroUser> {
            override fun onFailure(call: Call<RetroUser>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<RetroUser>, response: Response<RetroUser>) {
                val desc = response.body()!!.description


                rootView.userUsername.text = response.body()!!.username
                if (desc == " " || desc == "") rootView.userDescription.text =
                    "Keine Beschreibung vorhanden"
                else rootView.userDescription.text = response.body()!!.description
//                rootView.userModuleList.text = listOf(
//                    Mods.APMOD.title,
//                    Mods.MATH1INFMOD.title,
//                    Mods.MATH2INFMOD.title
//                ).toString()

            }
        })
    }


}

