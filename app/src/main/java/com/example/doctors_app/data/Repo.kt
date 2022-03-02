package com.example.doctors_app.data

import com.example.doctors_app.data.remote.FirebaseSource
import javax.inject.Inject

class Repo @Inject constructor(
    var firebaseSource: FirebaseSource
) {
}