package com.example.doctors_app.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Appointeiment(
    var appointeimentId:String="",
    var timeSchedule: TimeSchedule?=TimeSchedule(),
    var timeBook:String?="",
    var clientId:String?="",
    var client:Client?=null,
    var doctorId:String?="",
    var doctor: Doctors?=null
): Parcelable {
}