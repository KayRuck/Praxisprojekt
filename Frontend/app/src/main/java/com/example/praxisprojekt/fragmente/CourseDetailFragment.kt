package com.example.praxisprojekt.fragmente

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.example.praxisprojekt.InReturns
import com.example.praxisprojekt.MainActivity
import com.example.praxisprojekt.Mods
import com.example.praxisprojekt.R
import com.example.praxisprojekt.retrofit.RetroCourse
import com.example.praxisprojekt.viewModels.CourseDetailViewModel
import kotlinx.android.synthetic.main.course_detail_fragment.view.*

class CourseDetailFragment(val retroCourse: RetroCourse, val ownCourse: Boolean = false) :
    Fragment() {

    companion object {
        val TAG = CourseDetailFragment::class.java.canonicalName.toString()
    }

    private lateinit var viewModel: CourseDetailViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.course_detail_fragment, container, false)

        // TODO: Andere Text Views zu weisen
        Log.d(TAG, "Fragment loaded")

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val userID = sharedPref.getInt(MainActivity.USER_ID, -1)
        val userName = sharedPref.getString(MainActivity.USER_NAME, "User No.1")


        rootView.detailCourseTitleTV.text = retroCourse.title
        rootView.detailCourseDescriptionTV.text = retroCourse.description

        when (retroCourse.fk_return) {
            1 -> rootView.detailCourseInReturnTV.text = InReturns.MONEY.title
            2 -> rootView.detailCourseInReturnTV.text = InReturns.HELP.title
            3 -> rootView.detailCourseInReturnTV.text = InReturns.MENSA.title
            4 -> rootView.detailCourseInReturnTV.text = InReturns.COFFEE.title
        }
        when (retroCourse.fk_modules) {
            1 -> rootView.detailCourseModuleTV.text = Mods.APMOD.title
            2 -> rootView.detailCourseModuleTV.text = Mods.MATH1INFMOD.title
            3 -> rootView.detailCourseModuleTV.text = Mods.MATH2INFMOD.title
            4 -> rootView.detailCourseModuleTV.text = Mods.MATHINFMOD.title
            5 -> rootView.detailCourseModuleTV.text = Mods.BWL1INFMOD.title
        }

        if (retroCourse.privateUsage) rootView.detailCoursePrivateTV.text = "Private Nachhilfe"
        else rootView.detailCoursePrivateTV.text = "Gewerbliche Nachhilfe"

        if (retroCourse.fk_creator == userID) rootView.detailCourseCreator.text = userName
        else rootView.detailCourseCreator.text = "User mit der ID ${retroCourse.fk_creator}"

        if (ownCourse) {
            rootView.detailContactButton.text = "Kurs bearbeiten"
            rootView.detailContactButton.setOnClickListener {
                (activity as MainActivity).loadFragment(CourseEditFragment(retroCourse))
            }
        } else {
            rootView.detailContactButton.setOnClickListener {

                val builder = AlertDialog.Builder(context!!, R.style.CustomAlertDialog)
                builder.setMessage("Kontaktmöglichkeit auswählen")
                builder.setCancelable(true)

                builder.setPositiveButton("Handynummer") { dialog, id -> dialog.cancel() }
                builder.setNegativeButton("E-Mailadresse") { dialog, id -> dialog.cancel() }

                val alert = builder.create()
                alert.show()
            }
        }

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CourseDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
