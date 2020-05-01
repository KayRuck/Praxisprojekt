package com.example.praxisprojekt.fragmente

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.example.praxisprojekt.*
import com.example.praxisprojekt.retrofit.RetroCourse
import kotlinx.android.synthetic.main.course_detail_fragment.view.detailContactButton
import kotlinx.android.synthetic.main.course_detail_fragment.view.detailCourseCreator
import kotlinx.android.synthetic.main.course_detail_fragment.view.detailCourseDescriptionTV
import kotlinx.android.synthetic.main.course_detail_fragment.view.detailCourseInReturnTV
import kotlinx.android.synthetic.main.course_detail_fragment.view.detailCourseModuleTV
import kotlinx.android.synthetic.main.course_detail_fragment.view.detailCoursePrivateTV
import kotlinx.android.synthetic.main.course_detail_fragment.view.detailCourseTitleTV
import kotlinx.android.synthetic.main.course_detail_fragment2.view.*

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
        rootView = inflater.inflate(R.layout.course_detail_fragment2, container, false)

        setContent()
        return rootView
    }

    private fun setContent() {

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val userID = sharedPref.getInt(MainActivity.USER_ID, -1)
        val userName = sharedPref.getString(MainActivity.USER_NAME, "User No.1")

        rootView.detailCourseTitleTV.text = retroCourse.title

        if (retroCourse.description.isBlank() ||
            retroCourse.description == getString(R.string.noSuchDescription) )
            rootView.detailCourseDescriptionTV.text = getString(R.string.noSuchDescription)
        else rootView.detailCourseDescriptionTV.text = retroCourse.description

        val locList = mutableListOf<String>()
        Log.d("-- SET Content in Detail", "Location Liste ${retroCourse.locations}")
        retroCourse.locations.forEach {
            when (it){
                1 -> locList.add(TeachLocations.TEACH.title)
                2 -> locList.add(TeachLocations.STUD.title)
                3 -> locList.add(TeachLocations.TH.title)
                4 -> locList.add(TeachLocations.ONLINE.title)
            }
        }
        rootView.courseDetailTeachLocations.text = locList.toString()


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

        if (retroCourse.privateUsage) rootView.detailCoursePrivateTV.setText(R.string.tutoring_p)
        else rootView.detailCoursePrivateTV.setText(R.string.tutoring_g)


        if (retroCourse.fk_creator == userID) rootView.detailCourseCreator.text = "User: $userName"
        else {
            val userWithString = getString(R.string.userWithId) + retroCourse.fk_creator
            rootView.detailCourseCreator.text = userWithString
        }


        if (ownCourse) {
            rootView.detailContactButton.setText(R.string.update_course)
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
