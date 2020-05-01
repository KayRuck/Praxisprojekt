package com.example.praxisprojekt.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.praxisprojekt.retrofit.RetroUser
import com.example.praxisprojekt.retrofit.RetrofitClient
import com.example.praxisprojekt.utility.dataAvailable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    var functionTAG = "LoginViewModel"
    val oldUser = MutableLiveData<Boolean>()
    val newUser = MutableLiveData<Boolean>()

    lateinit var retroOldUser: RetroUser
    lateinit var retroNewUser: RetroUser


    fun checkEmail(email: String){
//        functionTAG = "CHECK EMAIL - "

        Log.d(functionTAG, "check email")
        val call = (RetrofitClient.getRetroService()).getLoginData(email)

        call.enqueue(object : Callback<RetroUser> {
            override fun onResponse(call: Call<RetroUser>, response: Response<RetroUser>) {
                val body = response.body()

                if (!response.isSuccessful || body == null) {
                    Log.d(
                        functionTAG + "NOT SUCCESSFUL",
                        "Response Body: $body Code: ${response.code()}"
                    )
                    return
                }

                Log.d(
                    functionTAG + "SUCCESSFUL",
                    "Response Body: ${response.body()} Code: ${response.code()}"
                )

                oldUser.dataAvailable()
                retroOldUser = body


            }

            override fun onFailure(call: Call<RetroUser>, t: Throwable) {
                Log.d(
                    functionTAG + "FAIL",
                    "Message: ${t.message} / CAUSE: ${t.cause}"
                )
            }
        })
    }

    fun getUserData(userId: Int) {
        functionTAG = "GET USER DATA IN LOGIN - "


        val call = RetrofitClient.getRetroService().getUserById(userId)

        call.enqueue(object : Callback<RetroUser> {
            override fun onResponse(call: Call<RetroUser>, response: Response<RetroUser>) {
                val body = response.body()


                if (!response.isSuccessful || body == null) {
                    Log.d(
                        functionTAG + "NOT SUCCESSFUL",
                        "Response Body: $body Code: ${response.code()}"
                    )
                    return
                }

                Log.d(
                    functionTAG + "SUCCESSFUL",
                    "Response Body: ${response.body()} Code: ${response.code()}"
                )

                newUser.dataAvailable()
                retroNewUser = body

            }

            override fun onFailure(call: Call<RetroUser>, t: Throwable) {
                Log.d(
                    functionTAG + "FAIL",
                    " - Message: ${t.message} Cause: ${t.cause}"
                )
            }
        })
    }



}
