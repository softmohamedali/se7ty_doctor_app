package com.example.doctors_app.data.remote

import com.example.doctors_app.models.Doctors
import com.example.doctors_app.models.TimeSchedule
import com.example.doctors_app.utils.Constants
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import javax.inject.Inject

class FirebaseSource @Inject constructor(
    private val auth:FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val dataStorage:FirebaseStorage,
) {

    // Authentication
    val user=auth.currentUser

    fun createUsre(email:String, password:String)=
        auth.createUserWithEmailAndPassword(email,password)

    fun login(email:String, password:String)=
        auth.signInWithEmailAndPassword(email,password)

    fun logOut()=auth.signOut()

    fun resetPassword(email: String)=auth.sendPasswordResetEmail(email)
    //FireStore
    fun saveDoctors(doctors: Doctors):Task<Void>{
        val ref=firestore.collection(Constants.COLLLECTION_DOCTORS_ACCEPTED)
            .document(auth.uid.toString())
        val mydoctor=doctors
        mydoctor.doctorsId=ref.id
        return ref.set(mydoctor)
    }

    fun getSearchSpecialtiest(name:String)=firestore.collection(Constants.SPICALISTIEST_COLLECTION)
        .orderBy("name")
        .startAt(name.trim())
        .endAt(name.trim()+"\uf8ff")
        .get()

    fun getUserData()=firestore.collection(Constants.COLLLECTION_DOCTORS_ACCEPTED)
        .document(auth.uid.toString()).get()

    //DataStorage
    fun saveImg(imgpitmap:ByteArray)=dataStorage.reference
        .child("${auth.currentUser?.uid}/images/${UUID.randomUUID()}}")

    fun addTimeSchedula(time:TimeSchedule):Task<Void>{
        val ref=firestore.collection(Constants.TIME_SCHEDULA_COLLECTION)
            .document(auth.currentUser!!.uid).collection("date").document()
        var mytime=time
        time.id=ref.id
        return ref.set(time)
    }

    fun getAllTimeSchedula()=firestore.collection(Constants.TIME_SCHEDULA_COLLECTION)
        .document(auth.currentUser!!.uid).collection("date")

    fun getMyAppointemnt()=firestore.collection(Constants.USER_APPOINTEMENT_COLLECTION)
        .whereEqualTo("doctorId",auth.currentUser?.uid)

    fun dleteTimeSchedula(time: TimeSchedule)=firestore.collection(Constants.TIME_SCHEDULA_COLLECTION)
        .document(auth.currentUser!!.uid).collection("date")
        .document(time.id!!).delete()

    fun getUserMedicalTest(userId:String)=firestore.collection(Constants.COLLLECTION_MEDICALTEST)
        .whereEqualTo("userid",userId)

    fun getUserMedication(userId:String)=firestore.collection(Constants.COLLLECTION_MEDICATION)
        .whereEqualTo("userid",userId)

}