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

class CourseEditViewModel : ViewModel() {

    var functionTAG = " "
    val showCourses = MutableLiveData<Boolean>()
    val showLocationList = MutableLiveData<Boolean>()

    // storage
    var retroCourse: RetroCourse? = null
    var locationList: List<String> = listOf()

    fun editData(id: Int) {
        callCourseDataByID(id)
        getLocationDataFormCourse(id)
    }

    private fun callCourseDataByID(courseID: Int) {
        functionTAG = "CALL COURSE BY ID - "

        val call: Call<RetroCourse> = (RetrofitClient.getRetroService())
            .getCourseById(courseID)

        call.enqueue(object : Callback<RetroCourse> {

            override fun onResponse(call: Call<RetroCourse>, response: Response<RetroCourse>) {
                val body = response.body()

                if (!response.isSuccessful || body == null) {
                    Log.d(
                        functionTAG + "NOT SUCCESSFUL",
                        "Course ID: $courseID \n Response Body: $body Code: ${response.code()}"
                    )
                    return
                }

                retroCourse = body
                showCourses.dataAvailable()

                Log.d(
                    functionTAG + "SUCCESS",
                    "Response Body: $body Code: ${response.code()}"
                )
            }

            override fun onFailure(call: Call<RetroCourse>, t: Throwable) {
                Log.d(
                    functionTAG + "FAIL",
                    " - Message: ${t.message} Cause: ${t.cause}"
                )
            }
        })
    }

    private fun getLocationDataFormCourse(courseID: Int) {
        functionTAG = "CALL LOCATION DATA -  "

        val call = (RetrofitClient.getRetroService()).getLocationFromCourse(courseID)

        call.enqueue(object : Callback<List<String>> {

            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                val body = response.body()

                if (!response.isSuccessful || body == null) {
                    Log.d(
                        functionTAG + "NOT SUCCESSFUL",
                        "Course ID: $courseID \n Response Body: $body Code: ${response.code()}"
                    )
                    return
                }

                locationList = body
                showLocationList.dataAvailable()

                Log.d(
                    functionTAG + "SUCCESS",
                    "Response Body: $body Code: ${response.code()}"
                )
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Log.d(
                    functionTAG + "FAIL",
                    "Message: ${t.message} / CAUSE: ${t.cause}"
                )
            }
        })
    }

    fun updateCourse(courseID: Int, retroCourse: RetroCourse) {
        functionTAG = "UPDATE COURSE - "

        val call: Call<RetroCourse> = (RetrofitClient.getRetroService())
            .updateCourse(courseID, retroCourse)

        call.enqueue(object : Callback<RetroCourse> {
            override fun onResponse(call: Call<RetroCourse>, response: Response<RetroCourse>) {
                if (!response.isSuccessful || response.body() == null) {
                    Log.d(
                        functionTAG + "NOT SUCCESS",
                        "Course Body: $retroCourse \n Response Body: ${response.body()} Code: ${response.code()}"
                    )
                    return
                }

                Log.d(
                    functionTAG + "SUCCESSFUL",
                    "Response Body: ${response.body()} Code: ${response.code()}"
                )
            }

            override fun onFailure(call: Call<RetroCourse>, t: Throwable) {
                Log.d(
                    functionTAG + "FAIL",
                    " - Message: ${t.message} Cause: ${t.cause}"
                )
            }
        })
    }


    fun createCourse(retroCourse: RetroCourse) {
        functionTAG = "CREATE COURSE - "

        val call: Call<RetroCourse> = (RetrofitClient.getRetroService())
            .createCourse(retroCourse)

        call.enqueue(object : Callback<RetroCourse> {
            override fun onResponse(call: Call<RetroCourse>, response: Response<RetroCourse>) {
                val body = response.body()

                if (!response.isSuccessful) {
                    Log.d(
                        functionTAG + "NOT SUCCESSFUL",
                        "Course Body: $retroCourse \n Response Body: $body Code: ${response.code()}"
                    )
                }

                Log.d(
                    functionTAG + "SUCCESSFUL",
                    "Response Body: $body Code: ${response.code()}"
                )

            }

            override fun onFailure(call: Call<RetroCourse>, t: Throwable) {
                Log.d(
                    functionTAG + "FAIL",
                    "Message: ${t.message} Cause: ${t.cause}"
                )

            }
        })
    }

}

