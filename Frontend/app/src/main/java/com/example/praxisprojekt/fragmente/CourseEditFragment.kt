package com.example.praxisprojekt.fragmente

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.Toast.makeText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.example.praxisprojekt.*
import com.example.praxisprojekt.retrofit.RetroCourse
import com.example.praxisprojekt.viewModels.CourseEditViewModel
import kotlinx.android.synthetic.main.course_edit_fragment.view.editCourseButton
import kotlinx.android.synthetic.main.course_edit_fragment.view.editCourseDescrition
import kotlinx.android.synthetic.main.course_edit_fragment.view.editCourseSeekbar
import kotlinx.android.synthetic.main.course_edit_fragment.view.editCourseTitle
import kotlinx.android.synthetic.main.course_edit_fragment.view.inReturnSpinner
import kotlinx.android.synthetic.main.course_edit_fragment.view.moduleSpinner
import kotlinx.android.synthetic.main.course_edit_fragment.view.privateUsageSwitch
import kotlinx.android.synthetic.main.course_edit_fragment2.view.*

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

    private val moduleList = mutableListOf(
        " ",
        Mods.APMOD.title,
        Mods.MATH1INFMOD.title,
        Mods.MATH2INFMOD.title,
        Mods.MATHINFMOD.title,
        Mods.BWL1INFMOD.title
    )

    private val locList = mutableListOf(
        TeachLocations.TEACH.title,
        TeachLocations.STUD.title,
        TeachLocations.TH.title,
        TeachLocations.ONLINE.title
    )

    private lateinit var viewModel: CourseEditViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.course_edit_fragment2, container, false)
        viewModel = ViewModelProviders.of(this).get(CourseEditViewModel::class.java)

        // init viewModel observer
        viewModel.showCourses.observe(this, Observer { setCourse(viewModel.retroCourse) })
        viewModel.showLocationList.observe(
            this,
            Observer { parseLocationList(viewModel.locationList) })

        if (course?.id != null) {
            update = true
            rootView.editCourseButton.setText(R.string.update_course)
            viewModel.editData(course.id)
        }

        setSeekBar()
        initMSpinner()
        initIRSpinner(inReturnList)
        initSearchSwitch()
        initPrivateSwitch()
        initCourseButton()
        initMultiAutoCompleteTextView()

        return rootView
    }

    // TODO Wert anzeigen dem Nutzer richtig anzeigen
    // TODO Werte Spanne auf 10 bis 50 eingrenzen
    private fun setSeekBar(): Int {
        functionTAG = "Seekbar -"

        val seek = rootView.editCourseSeekbar

        var translatedProgress = 0
        val step = 5
        val min = 10

        seek.max = 50

        seek?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {


                translatedProgress = min + (progress * step)

                Log.d(
                    functionTAG + "Progress Changed",
                    "On Progress Changed - seekbar progress $translatedProgress"
                )

                rootView.currentValue.text = "Der aktuelle möchtest du $translatedProgress € haben"
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
            val editSearchUsage = rootView.searchBitSwitch.isChecked
            val value = setSeekBar()
            val list = showInput()

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
                parseLocationList(list)
            )

            if (update && course?.id != null) viewModel.updateCourse(course.id, retroCourse)
            else viewModel.createCourse(retroCourse)
        }
    }

    private fun initSearchSwitch() {
        val sSwitch = rootView.searchBitSwitch
        sSwitch.setOnCheckedChangeListener { _, isChecked ->

            val message: String =
                if (isChecked) {
                    sSwitch.text = getString(R.string.tutoring_bit)
                    "Checked $isChecked - selected: ${sSwitch.text}"
                } else {
                    sSwitch.text = getString(R.string.tutoring_search)
                    "Checked $isChecked - selected: ${sSwitch.text}"
                }

            makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initPrivateSwitch() {
        val nSwitch = rootView.privateUsageSwitch
        nSwitch.setOnCheckedChangeListener { _, isChecked ->

            val message: String =
                if (isChecked) {
                    locList.removeAt(2)
                    nSwitch.text = getString(R.string.tutoring_g)
                    "Checked $isChecked - selected: ${nSwitch.text}"
                } else {
                    locList.add(2, TeachLocations.TH.title)
                    nSwitch.text = getString(R.string.tutoring_p)
                    "Checked $isChecked - selected: ${nSwitch.text}"
                }

            makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }


    private fun initMultiAutoCompleteTextView() {
        val multi = rootView.multiAutoCompleteTextView2

        Log.w("CONTEXT", "Context = $context")

        context?.let {
            val sAdapter = ArrayAdapter(it, android.R.layout.simple_list_item_1, locList)
            val mToken = MultiAutoCompleteTextView.CommaTokenizer()

            multi.setAdapter(sAdapter)
            multi.setTokenizer(mToken)
        }
    }

    private fun showInput(): List<String> {
        val multi = rootView.multiAutoCompleteTextView2
        val input = multi.text.toString().trim()
        val singleInput: List<String> = input.split(",")
        val builder = StringBuilder()

        singleInput.forEach { builder.append("$it\n") }

        return singleInput
    }


    private fun parseLocationList(list: List<String>): MutableList<Int> {
        val locations = mutableListOf<Int>()

        list.forEach {
            when (it.trim()) {
                TeachLocations.TEACH.title -> locations.add(TeachLocations.TEACH.id)
                TeachLocations.STUD.title -> locations.add(TeachLocations.STUD.id)
                TeachLocations.TH.title -> locations.add(TeachLocations.TH.id)
                TeachLocations.ONLINE.title -> locations.add(TeachLocations.ONLINE.id)
            }
        }
        return locations
    }

}