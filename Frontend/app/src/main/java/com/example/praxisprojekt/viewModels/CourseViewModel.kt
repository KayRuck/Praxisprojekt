package com.example.praxisprojekt.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.praxisprojekt.retrofit.RetroCourse
import com.example.praxisprojekt.retrofit.RetrofitClient
import com.example.praxisprojekt.utility.dataAvailable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CourseViewModel : ViewModel() {

    var functionTAG = " "
    val showAllCourses = MutableLiveData<Boolean>()
    val showUserCourses = MutableLiveData<Boolean>()
    val showMatchedCourses = MutableLiveData<Boolean>()

    // storage
    var allRetroCourse: List<RetroCourse> = listOf()
    var userRetroCourse: List<RetroCourse> = listOf()
    var matchedRetroCourse: List<RetroCourse> = listOf()

    fun callUserCourses(userID: Int) {
        functionTAG = "CALL USER COURSES - "

        val call = RetrofitClient.getRetroService().getAllUserCourses(userID)

        call.enqueue(object : Callback<List<RetroCourse>> {
            override fun onResponse(
                call: Call<List<RetroCourse>>,
                response: Response<List<RetroCourse>>
            ) {
                val body = response.body()

                if (!response.isSuccessful || body == null) {
                    Log.d(
                        functionTAG + "NOT SUCCESSFUL",
                        "UserID: $userID \n Response Body: $body Code: ${response.code()}"
                    )
                    return
                }

                userRetroCourse = body
                showUserCourses.dataAvailable()

                Log.d(
                    functionTAG + "SUCCESSFUL",
                    "Response Body: $body Code: ${response.code()}"
                )
            }

            override fun onFailure(call: Call<List<RetroCourse>>, t: Throwable) {
                Log.d(
                    functionTAG + "FAIL",
                    "Message: ${t.message} / CAUSE: ${t.cause}"
                )
            }
        })
    }

    fun callAllCourses() {
        functionTAG = "CALL ALL COURSES - "

        val call = (RetrofitClient.getRetroService()).getAllCourses()

        call.enqueue(object : Callback<List<RetroCourse>> {
            override fun onResponse(
                call: Call<List<RetroCourse>>,
                response: Response<List<RetroCourse>>
            ) {
                val body = response.body()

                if (!response.isSuccessful || body == null) {
                    Log.d(
                        functionTAG + "NOT SUCCESSFUL",
                        "Response Body: $body Code: ${response.code()}"
                    )
                    return
                }
                allRetroCourse = body
                showAllCourses.dataAvailable()

                Log.d(
                    functionTAG + "SUCCESSFUL",
                    "Response Body: $body Code: ${response.code()}"
                )
            }

            override fun onFailure(call: Call<List<RetroCourse>>, t: Throwable) {
                Log.d(
                    functionTAG + "FAIL",
                    "Message: ${t.message} / CAUSE: ${t.cause}"
                )
            }
        })
    }

    fun callMatchedCourses(userID: Int) {
        functionTAG = "CALL MATCHED COURSES - "


        val call = (RetrofitClient.getRetroService()).getAllMatchedCourses(userID)

        call.enqueue(object : Callback<List<RetroCourse>> {
            override fun onResponse(
                call: Call<List<RetroCourse>>,
                response: Response<List<RetroCourse>>
            ) {
                val body = response.body()

                if (!response.isSuccessful || body == null) {
                    Log.d(
                        functionTAG + "NOT SUCCESSFUL",
                        "UserID: $userID Response Body: $body Code: ${response.code()}"
                    )
                    return
                }

                matchedRetroCourse = body
                showMatchedCourses.dataAvailable()

                Log.d(
                    functionTAG + "SUCCESSFUL",
                    "Response Body: $body Code: ${response.code()}"
                )
            }

            override fun onFailure(call: Call<List<RetroCourse>>, t: Throwable) {
                Log.d(
                    functionTAG + "FAIL",
                    "Message: ${t.message} / CAUSE: ${t.cause}"
                )
            }
        })
    }

}
