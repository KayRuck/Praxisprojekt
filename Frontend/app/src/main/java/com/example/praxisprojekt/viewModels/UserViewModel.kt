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

class UserViewModel : ViewModel() {

    var functionTAG = " "
    val user = MutableLiveData<Boolean>()
    val modules = MutableLiveData<Boolean>()

    // storage
    var moduleList = listOf<String>()
    lateinit var retroUser: RetroUser

    fun callData(id: Int) {
        getModulesFromUser(id)
        callUserByID(id)
    }

    private fun getModulesFromUser(id: Int) {

        functionTAG = "GET MODULES - "

        val call = (RetrofitClient.getRetroService()).getModulesFromUser(id)

        call.enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
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

                moduleList = body.toList()
                modules.dataAvailable()

            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Log.d(
                    functionTAG + "FAIL",
                    " - Message: ${t.message} Cause: ${t.cause}"
                )
            }
        })
    }


    private fun callUserByID(id: Int) {

        functionTAG = "GET USER BY ID - "

        val call = (RetrofitClient.getRetroService()).getUserById(id)

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

                user.dataAvailable()
                retroUser = body
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
