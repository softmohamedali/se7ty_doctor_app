package com.example.doctors_app.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TimeSchedule(
    var id: String? = "",
    var fromTime: String? = "",
    var toTime: String? = "",
    var date: String? = "",
    var user: Client? = Client()
) : Parcelable {
}