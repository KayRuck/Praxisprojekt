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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.example.praxisprojekt.*
import com.example.praxisprojekt.retrofit.RetroCourse
import com.example.praxisprojekt.viewModels.CourseEditViewModel
import kotlinx.android.synthetic.main.course_edit_fragment.*
import kotlinx.android.synthetic.main.course_edit_fragment.view.*

class CourseEditFragment(private val course: RetroCourse? = null) : Fragment() {

    private lateinit var rootView: View
    private var currentReturn: Int = -1
    private var currentModule: Int = -1
    private var update = false
    private var functionTAG = " "

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
        viewModel = ViewModelProviders.of(this).get(CourseEditViewModel::class.java)

        // init viewModel observer
        viewModel.showCourses.observe(this, Observer { setCourse(viewModel.retroCourse) })
        viewModel.showLocationList.observe(
            this,
            Observer { setLocationList(viewModel.locationList) })

        if (course?.id != null) {
            update = true
            rootView.editCourseButton.text = R.string.update_course.toString()
            viewModel.editData(course.id)
        }

        setSeekBar()
        initMSpinner()
        initIRSpinner(inReturnList)
        initSwitch()
        checkboxCheck()
        initCourseButton()

        return rootView
    }

    // TODO Wert anzeigen dem Nutzer richtig anzeigen
    // TODO Werte Spanne auf 10 bis 50 eingrenzen
    private fun setSeekBar(): Int {
        functionTAG = "Seekbar -"

        val seek = rootView.editCourseSeekbar
        var translatedProgress = 0

        seek?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                translatedProgress = progress + 10

                Log.d(
                    functionTAG + "Progress Changed",
                    "On Progress Changed - seekbar progress $translatedProgress"
                )
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                Log.d(functionTAG + "Start", "seekbar touch started")

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                Log.d(functionTAG + "Stopped", "seekbar touch stopped")

            }

        })
        return translatedProgress
    }

    private fun setCourse(course: RetroCourse?) {
        if (course == null) return
        rootView.editCourseTitle.setText(course.title)

        if (course.description.isBlank()) rootView.editCourseDescrition.setText(R.string.noSuchDescription)
        else rootView.editCourseDescrition.setText(course.description)

        rootView.privateUsageSwitch.isChecked = !course.privateUsage

        currentModule = course.fk_modules
        currentReturn = course.fk_return
    }

    private fun setLocationList(list: List<String>) {
        list.forEach {
            when (it) {
                TeachLocations.TEACH.title -> checkboxTeacher.isChecked = true
                TeachLocations.STUD.title -> checkboxStudent.isChecked = true
                TeachLocations.TH.title -> checkboxTHKoeln.isChecked = true
                TeachLocations.ONLINE.title -> checkboxOnline.isChecked = true
            }
        }
    }

    private fun getLocationList(): List<Int> {
        val locations = mutableListOf<Int>()
        if (checkboxTeacher.isChecked) locations.add(TeachLocations.TEACH.id)
        if (checkboxStudent.isChecked) locations.add(TeachLocations.STUD.id)
        if (checkboxTHKoeln.isChecked) locations.add(TeachLocations.TH.id)
        if (checkboxOnline.isChecked) locations.add(TeachLocations.ONLINE.id)
        return locations
    }

    private fun initIRSpinner(modListe: MutableList<String>) {
        functionTAG = "In Return Spinner - "

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
                Log.d(functionTAG + "Selected", "Ausgewählt: ${inReturnList[position]}")
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Log.d(functionTAG + "Nothing Selected", "Bitte eine Gegenleistung auswählen")
                // TODO: Es MUSS etwas Selected sein sonst Fehlermeldung
            }
        }
    }

    private fun initMSpinner() {
        functionTAG = "Module Spinner - "

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
                    Log.d(functionTAG + "Selected", "Ausgewählt: ${inReturnList[position]}")

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    Log.d(functionTAG + "Nothing Selected", "Bitte eine Gegenleistung auswählen")
                    // TODO: Es MUSS etwas Selected sein sonst Fehlermeldung
                }
            }
        }
    }

    private fun initCourseButton() {

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val userID = sharedPref.getInt(MainActivity.USER_ID, -1)

        rootView.editCourseButton.setOnClickListener {
            val editTitle = rootView.editCourseTitle.text.toString()
            val editDescription = rootView.editCourseDescrition.text.toString()
            val editState = true
            val editPrivateUsage = rootView.privateUsageSwitch.isChecked
            val value = setSeekBar()

            val retroCourse = RetroCourse(
                null,
                editTitle,
                editDescription,
                editState,
                0.0,
                0.0,
                editPrivateUsage,
                value,
                userID,
                currentReturn,
                currentModule,
                getLocationList()
            )

            if (update && course?.id != null) viewModel.updateCourse(course.id, retroCourse)
            else viewModel.createCourse(retroCourse)
        }
    }

    private fun initSwitch() {
        val nSwitch = rootView.privateUsageSwitch
        nSwitch.setOnCheckedChangeListener { _, isChecked ->

            val message: String =
                if (isChecked) "Checked $isChecked - Ausgewählt: Gewerblich"
                else "Checked $isChecked - Ausgewählt: Privat"

            makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkboxCheck() {
        rootView.checkboxTHKoeln.setOnCheckedChangeListener { _, isChecked ->

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
    }

}
