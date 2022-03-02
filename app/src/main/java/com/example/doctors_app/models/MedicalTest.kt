package com.example.doctors_app.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MedicalTest(
    var id:String?="",
    var userid:String?="",
    var testName:String?="",
    var labName:String?="",
    var testdate:String?="",
    var testResult:String?="",
    var img:String?=""
): Parcelable
