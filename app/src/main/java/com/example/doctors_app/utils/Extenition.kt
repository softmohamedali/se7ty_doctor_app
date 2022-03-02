package com.example.doctors_app.utils

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.toast(context:Context,txt:String?)
{
    Toast.makeText(context,txt,Toast.LENGTH_SHORT).show()
}