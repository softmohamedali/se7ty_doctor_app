package com.example.doctors_app.ui.body

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.doctors_app.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyCreditFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_credit, container, false)
    }

}