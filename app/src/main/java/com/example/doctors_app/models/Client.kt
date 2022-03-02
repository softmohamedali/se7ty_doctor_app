package com.example.doctors_app.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Client(
    var name:String?="",
    var phone:String?="",
    var email:String?="",
    var emailToContact:String?="",
    var age:String?="",
    var photoUrl:String?="",
    var position:String?="",
    var desease:String?="",
    var gender:String?="",
    var clientId:String?="",
    var info:PersonalInfo?=PersonalInfo(),
    var img:String?=""
):Parcelable {
}