package com.example.doctors_app.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PersonalInfo(
    var height:String?="",
    var weight:String?="",
    var smoker:String?="",
    var elchole:String?="",
    var blodeType:String?="",
    var materialStatus:String?=""
): Parcelable
