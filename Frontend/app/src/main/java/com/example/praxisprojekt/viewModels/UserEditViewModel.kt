package com.example.praxisprojekt.viewModels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.praxisprojekt.MainActivity
import com.example.praxisprojekt.retrofit.RetroUser
import com.example.praxisprojekt.retrofit.RetrofitClient
import com.example.praxisprojekt.utility.dataAvailable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserEditViewModel : ViewModel() {

    var functionTAG = " "
    val currentUser = MutableLiveData<Boolean>()
    val currentModules = MutableLiveData<Boolean>()

    // storage
    var moduleList = listOf<String>()
    lateinit var updateUser: RetroUser

    fun editData(id: Int) {
        callUserDataByID(id)
        getModuleDataFromUser(id)
    }


    fun createUser(retroUser: RetroUser, sharedPreferences: SharedPreferences) {
        functionTAG = "CREATE USER - "

        val call: Call<RetroUser> = (RetrofitClient.getRetroService()).createUser(retroUser)

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


                val userId: Int = body.id!!
                val name: String = body.username

                sharedPreferences.edit().putInt(MainActivity.USER_ID, userId).apply()
                sharedPreferences.edit().putString(MainActivity.USER_NAME, name).apply()

            }

            override fun onFailure(call: Call<RetroUser>, t: Throwable) {
                Log.d(
                    functionTAG + "FAIL",
                    " - Message: ${t.message} Cause: ${t.cause}"
                )
            }

        })
    }

    fun updateUser(userID: Int, retroUser: RetroUser, sharedPreferences: SharedPreferences) {
        functionTAG = "UPDATE USER - "

        val call: Call<RetroUser> = (RetrofitClient.getRetroService()).updateUser(userID, retroUser)

        call.enqueue(object : Callback<RetroUser> {
            override fun onResponse(call: Call<RetroUser>, response: Response<RetroUser>) {
                val body = response.body()

                if (!response.isSuccessful || body == null) {
                    Log.d(
                        functionTAG + "NOT SUCCESSFUL",
                        "UserID: $userID Response Body: $body Code: ${response.code()}"
                    )
                    return
                }

                Log.d(
                    functionTAG + "SUCCESSFUL",
                    "Response Body: ${response.body()} Code: ${response.code()}"
                )

                val name = body.username
                sharedPreferences.edit().putString(MainActivity.USER_NAME, name).apply()

            }

            override fun onFailure(call: Call<RetroUser>, t: Throwable) {
                Log.d(
                    functionTAG + "FAIL",
                    " - Message: ${t.message} Cause: ${t.cause}"
                )
            }
        })

    }

    private fun callUserDataByID(userID: Int) {
        functionTAG = "CALL USER BY ID - "

        val call = (RetrofitClient.getRetroService()).getUserById(userID)

        call.enqueue(object : Callback<RetroUser> {

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

                updateUser = body
                currentUser.dataAvailable()

            }

            override fun onFailure(call: Call<RetroUser>, t: Throwable) {
                Log.d(
                    functionTAG + "FAIL",
                    "Message: ${t.message} / CAUSE: ${t.cause}"
                )
            }

        })
    }

    private fun getModuleDataFromUser(userID: Int) {


        val call = (RetrofitClient.getRetroService()).getModulesFromUser(userID)

        call.enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
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

                moduleList = body
                currentModules.dataAvailable()

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
