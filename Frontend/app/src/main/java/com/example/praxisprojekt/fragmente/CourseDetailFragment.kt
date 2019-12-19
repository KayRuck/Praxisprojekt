package com.example.praxisprojekt.fragmente

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.example.praxisprojekt.InReturns
import com.example.praxisprojekt.MainActivity
import com.example.praxisprojekt.Mods
import com.example.praxisprojekt.R
import com.example.praxisprojekt.retrofit.RetroCourse
import kotlinx.android.synthetic.main.course_detail_fragment.view.*

class CourseDetailFragment(
    private val retroCourse: RetroCourse,
    private val ownCourse: Boolean = false
) :
    Fragment() {

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.course_detail_fragment, container, false)

        setContent()
        return rootView
    }

    private fun setContent() {

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

        if (retroCourse.privateUsage)
            rootView.detailCoursePrivateTV.text = R.string.tutoring_p.toString()
        else
            rootView.detailCoursePrivateTV.text = R.string.tutoring_g.toString()


        if (retroCourse.fk_creator == userID) rootView.detailCourseCreator.text = userName
        else {
            val userWithString = R.string.userWithId.toString() + retroCourse.fk_creator
            rootView.detailCourseCreator.text = userWithString
        }

        if (ownCourse) {
            rootView.detailContactButton.text = R.string.update_course.toString()
            rootView.detailContactButton.setOnClickListener {
                (activity as MainActivity).loadFragment(CourseEditFragment(retroCourse))
            }
        } else {
            rootView.detailContactButton.setOnClickListener {

                val builder = AlertDialog.Builder(context!!, R.style.CustomAlertDialog)
                builder.setMessage("Art des Kontakts auswählen")
                builder.setCancelable(true)

                builder.setPositiveButton("Handynummer") { dialog, _ -> dialog.cancel() }
                builder.setNegativeButton("E-Mail") { dialog, _ -> dialog.cancel() }

                val alert = builder.create()
                alert.show()
            }
        }
    }

}
