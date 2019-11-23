package com.example.praxisprojekt.fragmente

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.Toast.makeText
import com.example.praxisprojekt.R
import com.example.praxisprojekt.datastructure.Course
import com.example.praxisprojekt.viewModels.CourseEditViewModel
import com.example.praxisprojekt.datastructure.InReturns
import com.example.praxisprojekt.datastructure.Mods
import com.example.praxisprojekt.datastructure.TeachLocs
import kotlinx.android.synthetic.main.course_edit_fragment.view.*


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

    private val locationList = mutableListOf(
        " ",
        TeachLocs.TEACH.title,
        TeachLocs.STUD.title,
        TeachLocs.TH.title,
        TeachLocs.ONLINE.title
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


        rootView.editCourseButton.setOnClickListener {



        }

        //val editTitle = rootView.editCourseTitle.text.toString()
        //val editDescr = rootView.editCourseDescrition.text.toString()


        return rootView
    }


    private fun createCourse(): Course {

        val currentCourse = Course(
            rootView.editCourseTitle.text.toString(),
            rootView.editCourseDescrition.text.toString(),
            true,
            0.0,
            0.0,
            rootView.privateUsageSwitch.isChecked,
            0,
            currentReturn,
            currentModule
        )



        makeText(
            context,
            "Title: " + currentCourse.title + " Beschreibung: " + currentCourse.description +
                    " Privat: " + currentCourse.privateUsage + " Gegenleistung: " + currentReturn +
                    " Module: " + currentModule,
            Toast.LENGTH_LONG
        ).show()


        return currentCourse
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
        val spinnerL = rootView.locationSpinner


        // TODO: CHANGE von Spinner zu einer Mehr auswahl (Mehrere Können ausgewählt werden)
        if (spinnerL != null) {
            val adapter = ArrayAdapter(
                context!!,
                android.R.layout.simple_spinner_item,
                locationList
            )
            spinnerL.adapter = adapter

            spinnerL.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    makeText(
                        context, "Ausgewählt: " + locationList[position], Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    makeText(
                        context, "Bitte eine Gegenleistung auswählen", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

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


        // TODO: Wenn Ort Uni gewählt ist muss der InReturn Punkt Money und die Seekbar ausgeblendet werden
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


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CourseEditViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
