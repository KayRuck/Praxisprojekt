package com.example.praxisprojekt.fragmente

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.example.praxisprojekt.*
import com.example.praxisprojekt.retrofit.RetroCourse
import com.example.praxisprojekt.retrofit.RetroService
import com.example.praxisprojekt.viewModels.CourseEditViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.course_edit_fragment.*
import kotlinx.android.synthetic.main.course_edit_fragment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CourseEditFragment : Fragment() {

    private lateinit var rootView: View
    private var currentReturn: Int = -1
    private var currentModule: Int = -1

    private val inReturnList = mutableListOf(
        " ",
        InReturns.MONEY.title,
        InReturns.HELP.title,
        InReturns.MENSA.title,
        InReturns.COFFEE.title
    )

    private val moduleList = mutableListOf(
        " ",
        Mods.APMOD.title,
        Mods.MATH1INFMOD.title,
        Mods.MATH2INFMOD.title,
        Mods.MATHINFMOD.title,
        Mods.BWL1INFMOD.title
    )

    companion object {
        fun newInstance() = CourseEditFragment()
    }

    private lateinit var viewModel: CourseEditViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.course_edit_fragment, container, false)

        setSeekBar()
        fillSpinners()

        val nSwitch = rootView.privateUsageSwitch
        nSwitch.setOnCheckedChangeListener { _, isChecked ->

            val message: String =
                if (isChecked)
                    "Checked $isChecked - Ausgewählt: Gewerblich"
                else
                    "Checked $isChecked - Ausgewählt: Privat"


            makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val userID = sharedPref.getInt(MainActivity.USER_ID, -1)



        rootView.editCourseButton.setOnClickListener {
            val editTitle = rootView.editCourseTitle.text.toString()
            val editDescr = rootView.editCourseDescrition.text.toString()
            val editState = true
            val editPrivateUsage = rootView.privateUsageSwitch.isChecked

            val retroCourse = RetroCourse(
                null,
                editTitle,
                editDescr,
                editState,
                0.0,
                0.0,
                editPrivateUsage,
                userID,
                currentReturn,
                currentModule
            )
            createCourse(retroCourse)

            Log.d("CREATE COURSE", retroCourse.toString())
        }

        return rootView
    }

    private fun createCourse(retroCourse: RetroCourse) {

        val gson: Gson = GsonBuilder().setLenient().create()

        val retroClient = Retrofit.Builder()
            .baseUrl(Constants.API_BASE_URL.string)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val retroService = retroClient.create(RetroService::class.java)
        val call: Call<RetroCourse> = retroService.createCourse(retroCourse)

        call.enqueue(object : Callback<RetroCourse> {
            override fun onFailure(call: Call<RetroCourse>, t: Throwable) {
                Log.d(
                    "CREATE USER FAIL",
                    " - Message: ${t.message} Cause: ${t.cause} / ${t.localizedMessage} / ${t.stackTrace} "
                )

            }

            override fun onResponse(call: Call<RetroCourse>, response: Response<RetroCourse>) {

                if (!response.isSuccessful) {
                    Log.d(
                        "CREATE USER - NOT SUCCESS",
                        "Body: $retroCourse Code: ${response.code()} / ${response.body()} / ${response.message()} /  ${response.errorBody()} / ${response.headers()}"
                    )
                }

                val retroCourseResponse: RetroCourse? = response.body()
                makeText(context, response.code().toString(), Toast.LENGTH_SHORT).show()
                Log.d(
                    "CREATE USER SUCCESS",
                    " - Response Body: " + retroCourseResponse + " Code: " + response.code()
                )


                Log.d(
                    "CREATE USER SUCCESS",
                    " - Response Body: " + retroCourseResponse + " Code: " + response.code()
                )

            }

        })

    }



    /**
     *  TODO: Comment Code
     *  Used Tutorial: https://www.geeksforgeeks.org/spinner-in-kotlin/
     *
     *
     */

    private fun fillSpinners() {
        val spinnerIR = rootView.inReturnSpinner
        val spinnerM = rootView.moduleSpinner

        if (spinnerIR != null) {
            val adapter = ArrayAdapter(
                context!!,
                android.R.layout.simple_spinner_item,
                inReturnList
            )
            spinnerIR.adapter = adapter

            spinnerIR.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    currentReturn = position
                    makeText(
                        context, "Ausgewählt: " + inReturnList[position], Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    makeText(
                        context, "Bitte eine Gegenleistung auswählen", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        if (spinnerM != null) {
            val adapter = ArrayAdapter(
                context!!,
                android.R.layout.simple_spinner_item,
                moduleList
            )
            spinnerM.adapter = adapter

            spinnerM.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    currentModule = position
                    makeText(
                        context, "Ausgewählt: " + moduleList[position], Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    makeText(
                        context, "Bitte eine Gegenleistung auswählen", Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }



    }

    /**
     *  TODO: Comment Code
     *  Used Tutorial: https://www.geeksforgeeks.org/seekbar-in-kotlin/
     *
     *
     */

    private fun setSeekBar() {
        val seek = rootView.editCourseSeekbar

        seek?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                val translatedProgress = progress + 10
                makeText(
                    context,
                    "On Progress Changed - seekbar progress $translatedProgress",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                makeText(context, "seekbar touch started", Toast.LENGTH_SHORT).show()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                makeText(context, "seekbar touch stopped", Toast.LENGTH_SHORT).show()
            }


        })

    }


    // TODO: Wenn Ort Uni gewählt ist muss der InReturn Punkt Money und die Seekbar ausgeblendet werden
    // TODO: Bin Checkboxes to Course ID
    private fun setCheckBoxes(){
        val checkedLocations = mutableListOf<Boolean>()

        checkedLocations.add(checkboxTeacher.isChecked)
        checkedLocations.add(checkboxStudent.isChecked)
        checkedLocations.add(checkboxTHKoeln.isChecked)
        checkedLocations.add(checkboxOnline.isChecked)

        val removeIR = InReturns.MONEY.title

        if (checkboxTHKoeln.isChecked){
                inReturnList.remove(removeIR)

        }



    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CourseEditViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
