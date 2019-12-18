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


class CourseEditFragment(private val course: RetroCourse? = null) : Fragment() {

    private lateinit var rootView: View
    private var currentReturn: Int = -1
    private var currentModule: Int = -1
    private var update = false

    private val inReturnList = mutableListOf(
        " ",
        InReturns.MONEY.title,
        InReturns.HELP.title,
        InReturns.MENSA.title,
        InReturns.COFFEE.title
    )

    private val inReturnListTH = mutableListOf(
        " ",
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

    private lateinit var viewModel: CourseEditViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.course_edit_fragment, container, false)

        if (course?.id != null) {
            update = true
            rootView.editCourseButton.text = "Bearbeiten"
            callCourseDataByID(course.id)
            getLocationDataFormCourse(course.id)
        }

        setSeekBar()
        initMSpinner()
        initIRSpinner(inReturnList)

        val nSwitch = rootView.privateUsageSwitch
        nSwitch.setOnCheckedChangeListener { _, isChecked ->

            val message: String =
                if (isChecked) "Checked $isChecked - Ausgewählt: Gewerblich"
                else "Checked $isChecked - Ausgewählt: Privat"

            makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val userID = sharedPref.getInt(MainActivity.USER_ID, -1)

        rootView.checkboxTHKoeln.setOnCheckedChangeListener { _, isChecked ->
            val selectedItem = rootView.inReturnSpinner.selectedItem as String

            if (isChecked) {
                // gewerblich deaktivieren
                rootView.privateUsageSwitch.isChecked = false
                rootView.privateUsageSwitch.isEnabled = false

                rootView.editCourseSeekbar.isEnabled = false

                initIRSpinner(inReturnListTH)

            } else {
                // gewerbliche Nutzung aktivieren
                rootView.privateUsageSwitch.isClickable = true
                rootView.privateUsageSwitch.isEnabled = true

                rootView.editCourseSeekbar.isEnabled = true

                initIRSpinner(inReturnList)
            }
        }

        rootView.editCourseButton.setOnClickListener {
            val editTitle = rootView.editCourseTitle.text.toString()
            val editDescr = rootView.editCourseDescrition.text.toString()
            val editState = true
            val editPrivateUsage = rootView.privateUsageSwitch.isChecked
            val value = setSeekBar()

            val retroCourse = RetroCourse(
                null,
                editTitle,
                editDescr,
                editState,
                0.0,
                0.0,
                editPrivateUsage,
                value,
                userID,
                currentReturn,
                currentModule,
                //locationList
                getLocationList()
            )

            if (update && course?.id != null) updateCourse(course.id, retroCourse)
            else createCourse(retroCourse)

            Log.d("CREATE COURSE", retroCourse.toString())


        }

        return rootView
    }

    private fun getLocationDataFormCourse(courseID: Int) {

        val gson: Gson = GsonBuilder().setLenient().create()

        val retroClient = Retrofit.Builder()
            .baseUrl(Constants.API_BASE_URL.string)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val retroService = retroClient.create(RetroService::class.java)
        val call = retroService.getLocationFromCourse(courseID)

        call.enqueue(object : Callback<List<String>> {

            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (!response.isSuccessful) {
                    Log.d(
                        "GET LOCATION - NOT SUCCESS",
                        "Body: ${response.body()} Code: ${response.code()} / ${response.message()} /  ${response.errorBody()}"
                    )
                    return
                }
                val locListe = response.body()

                if (locListe != null) {
                    Log.d(
                        "GET LOCATION - SUCCESS",
                        "Body: ${response.body()} Code: ${response.code()} /  ${response.message()} /  ${response.errorBody()}"
                    )

                    locListe.forEach {
                        when (it) {
                            TeachLocs.TEACH.title -> checkboxTeacher.isChecked = true
                            TeachLocs.STUD.title -> checkboxStudent.isChecked = true
                            TeachLocs.TH.title -> checkboxTHKoeln.isChecked = true
                            TeachLocs.ONLINE.title -> checkboxOnline.isChecked = true
                        }
                    }
                } else
                    Log.d(
                        "GET LOCATION - SUCCESS",
                        "Body: ${response.body()}"
                    )
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Log.d(
                    "GET MODULES - Fail",
                    "Message: ${t.message} / CAUSE: ${t.cause}"
                )
            }
        })
    }

    private fun getLocationList(): List<Int> {
        val locations = mutableListOf<Int>()
        if (checkboxTeacher.isChecked) locations.add(TeachLocs.TEACH.id)
        if (checkboxStudent.isChecked) locations.add(TeachLocs.STUD.id)
        if (checkboxTHKoeln.isChecked) locations.add(TeachLocs.TH.id)
        if (checkboxOnline.isChecked) locations.add(TeachLocs.ONLINE.id)
        return locations
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
            override fun onResponse(call: Call<RetroCourse>, response: Response<RetroCourse>) {

                if (!response.isSuccessful) {
                    Log.d(
                        "CREATE COURSE - NOT SUCCESS",
                        "Course Body: $retroCourse Code: ${response.code()} / Response Body: ${response.body()} "
                    )
                }

                val retroCourseResponse: RetroCourse? = response.body()
                makeText(context, response.code().toString(), Toast.LENGTH_SHORT).show()
                Log.d(
                    "CREATE COURSE SUCCESS",
                    " - Response Body: $retroCourseResponse Code: " + response.code()
                )

            }

            override fun onFailure(call: Call<RetroCourse>, t: Throwable) {
                Log.d(
                    "CREATE COURSE FAIL",
                    " - Message: ${t.message} Cause: ${t.cause} / ${t.localizedMessage} / ${t.stackTrace} "
                )

            }
        })
    }

    private fun initIRSpinner(modListe: MutableList<String>) {
        val spinnerIR = rootView.inReturnSpinner

        val adapter = ArrayAdapter(
            context!!,
            android.R.layout.simple_spinner_item,
            modListe
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

    private fun initMSpinner() {
        val spinnerM = rootView.moduleSpinner

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

    private fun setSeekBar(): Int {
        val seek = rootView.editCourseSeekbar
        var translatedProgress: Int = 0

        seek?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                translatedProgress = progress + 10
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
        return translatedProgress
    }

    private fun updateCourse(courseID: Int, retroCourse: RetroCourse) {
        val gson: Gson = GsonBuilder().setLenient().create()

        val retroClient = Retrofit.Builder().baseUrl(Constants.API_BASE_URL.string)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()

        val retroService = retroClient.create(RetroService::class.java)
        val call: Call<RetroCourse> = retroService.updateCourse(courseID, retroCourse)

        call.enqueue(object : Callback<RetroCourse> {
            override fun onResponse(call: Call<RetroCourse>, response: Response<RetroCourse>) {
                if (!response.isSuccessful) {
                    Log.d(
                        "UPDATE COURSE - NOT SUCCESS",
                        "Course Body: $retroCourse Code: ${response.code()} / Response Body: ${response.body()} "
                    )
                }

                Log.d(
                    "UPDATE COURSE SUCCESS",
                    " - Response Body: ${response.body()} Code: ${response.code()}"
                )
            }

            override fun onFailure(call: Call<RetroCourse>, t: Throwable) {
                Log.d(
                    "UPDATE COURSE FAIL",
                    " - Message: ${t.message} Cause: ${t.cause}"
                )
            }
        })

    }

    private fun callCourseDataByID(courseID: Int) {
        val gson: Gson = GsonBuilder().setLenient().create()

        val retroClient = Retrofit.Builder()
            .baseUrl(Constants.API_BASE_URL.string)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val retroService = retroClient.create(RetroService::class.java)
        val call: Call<RetroCourse> = retroService.getCourseById(courseID)

        call.enqueue(object : Callback<RetroCourse> {

            override fun onResponse(call: Call<RetroCourse>, response: Response<RetroCourse>) {
                val body = response.body()

                if (body != null) {

                    rootView.editCourseTitle.setText(body.title)

                    if (body.description.isBlank())
                        rootView.editCourseDescrition.setText("Keine Beschreibung vorhanden")
                    else rootView.editCourseDescrition.setText(body.description)

                    rootView.privateUsageSwitch.isChecked = !body.privateUsage

                    currentModule = body.fk_modules
                    currentReturn = body.fk_return

                }

                val retroCourseResponse: RetroCourse? = response.body()
                makeText(context, response.code().toString(), Toast.LENGTH_SHORT).show()
                Log.d(
                    "CALL COURSE BY ID SUCCESS",
                    " - Response Body: $retroCourseResponse Code: ${response.code()}"
                )

            }

            override fun onFailure(call: Call<RetroCourse>, t: Throwable) {
                Log.d(
                    "CALL COURSE BY ID FAIL",
                    " - Message: ${t.message} Cause: ${t.cause}"
                )

            }
        })
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CourseEditViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
