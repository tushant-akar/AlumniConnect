// StudentHomeFragment.kt
package com.example.studentportal.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentportal.R  // Replace with the actual package name of your R class
import com.example.studentportal.data.Posts  // Replace with the actual package name of your Post class
import com.example.studentportal.adapter.PostAdapter  // Replace with the actual package name of your PostAdapter class

class StudentHomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_student_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val posts = listOf(
            Posts("xxddhjghj", "Nicholas White", "1w","Coding for work vs Coding for fun \n Join the Discord - https://lnkd.in/gyCstjdk \n #programmer #coding #softwareengineer\n" +
                    "#work"),
            Posts("bdfjhsgfj", "Abhishek Pandey","2w","Agree \uD83D\uDCAF \n \n Working smart means working hard on what truly \n #like #succes #time #result #postive #hard\n" +
                    "#work"),
            Posts("xxddhjghj", "Nicholas White", "1w","Coding for work vs Coding for fun \n Join the Discord - https://lnkd.in/gyCstjdk \n #programmer #coding #softwareengineer\n" +
                    "#work"),
            Posts("bdfjhsgfj", "Abhishek Pandey","2w","Agree \uD83D\uDCAF \n \n Working smart means working hard on what truly \n #like #succes #time #result #postive #hard\n" +
                    "#work"),
            Posts("xxddhjghj", "Nicholas White", "1w","Coding for work vs Coding for fun \n Join the Discord - https://lnkd.in/gyCstjdk \n #programmer #coding #softwareengineer\n" +
                    "#work"),
            Posts("bdfjhsgfj", "Abhishek Pandey","2w","Agree \uD83D\uDCAF \n \n Working smart means working hard on what truly \n #like #succes #time #result #postive #hard\n" +
                    "#work"),
            Posts("xxddhjghj", "Nicholas White", "1w","Coding for work vs Coding for fun \n Join the Discord - https://lnkd.in/gyCstjdk \n #programmer #coding #softwareengineer\n" +
                    "#work"),
            Posts("bdfjhsgfj", "Abhishek Pandey","2w","Agree \uD83D\uDCAF \n \n Working smart means working hard on what truly \n #like #succes #time #result #postive #hard\n" +
                    "#work")
        )

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = PostAdapter(posts)
    }
}
