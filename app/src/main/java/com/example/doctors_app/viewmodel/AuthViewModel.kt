package com.example.doctors_app.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.doctors_app.data.Repo
import com.example.doctors_app.data.remote.FirebaseSource
import com.example.doctors_app.models.Doctors
import com.example.doctors_app.models.Specialties
import com.example.doctors_app.utils.StatusResult
import com.google.firebase.firestore.QuerySnapshot
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo:Repo,
    application: Application
):AndroidViewModel(application) {
    private var _isLogin=MutableLiveData<StatusResult<Boolean>>()
    private var _isRegister=MutableLiveData<StatusResult<Boolean>>()
    private var _isPassSent=MutableLiveData<StatusResult<Boolean>>()
    private var _speialitest=MutableLiveData<StatusResult<MutableList<Specialties>>>()
    private var _userInfo= MutableLiveData<StatusResult<Doctors>>()

    val isLogin:LiveData<StatusResult<Boolean>> get() = _isLogin
    val isRegister:LiveData<StatusResult<Boolean>> get() = _isRegister
    val isPassSent:LiveData<StatusResult<Boolean>> get() = _isPassSent
    val speialitest:LiveData<StatusResult<MutableList<Specialties>>> get() = _speialitest
    val userInfo:LiveData<StatusResult<Doctors>> get() = _userInfo

    val user=repo.firebaseSource.user
    fun logOut()=repo.firebaseSource.logOut()

    fun forgetPassWord(email:String){
        _isPassSent.value=StatusResult.OnLoading()
        repo.firebaseSource.resetPassword(email).addOnCompleteListener {
            if (it.isSuccessful)
            {
                _isPassSent.value=StatusResult.OnSuccess()
            }
            else{
                _isPassSent.value=StatusResult.OnError(it.exception?.message.toString())
            }
        }
    }
    fun logIn(email:String,password:String){
        _isLogin.value=StatusResult.OnLoading()
        repo.firebaseSource.login(email, password).addOnCompleteListener {
            if (it.isSuccessful)
            {
                _isLogin.value=StatusResult.OnSuccess()
            }
            else{
                _isLogin.value=StatusResult.OnError(it.exception?.message.toString())
            }
        }
    }

    fun register(email: String, password: String, doctor:Doctors){
        _isRegister.value=StatusResult.OnLoading()
        repo.firebaseSource.createUsre(email, password).addOnCompleteListener {
            if (it.isSuccessful)
            {
                saveDoctors(doctor)
            }
            else{
                _isRegister.value=StatusResult.OnError(it.exception?.message.toString())
            }
        }
    }

    fun saveDoctorsInfo(doctor: Doctors,imgByteArray: ByteArray?,medicalImg:ByteArray?)
    {
        _isRegister.value=StatusResult.OnLoading()
        if (imgByteArray==null&&medicalImg==null)
        {
            saveDoctors(doctor)
        }
        else{
            _isRegister.value=StatusResult.OnLoading()
            saveImg(doctor, imgByteArray!!)
            saveImgMedical(doctor, medicalImg!!)
        }


    }

    private fun saveImg(doctor:Doctors,imgByteArray: ByteArray)
    {
        var downloadUri :String?=null
        val ref=repo.firebaseSource.saveImg(imgByteArray)
        val task=ref.putBytes(imgByteArray)
        task.continueWithTask {
            if (!it.isSuccessful) {
                it.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                downloadUri=task.result.toString()
                val doc=doctor
                doc.photo=downloadUri.toString()
                saveDoctors(doc)
            } else {
                _isRegister.value=StatusResult.OnError(task.exception?.message.toString())
            }
        }
    }

    private fun saveImgMedical(doctor:Doctors,imgByteArray: ByteArray)
    {
        var downloadUri :String?=null
        val ref=repo.firebaseSource.saveImg(imgByteArray)
        val task=ref.putBytes(imgByteArray)
        task.continueWithTask {
            if (!it.isSuccessful) {
                it.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                downloadUri=task.result.toString()
                val doc=doctor
                doc.medicalIdPhoto=downloadUri.toString()
                saveDoctors(doc)
            } else {
                _isRegister.value=StatusResult.OnError(task.exception?.message.toString())
            }
        }
    }
    private fun saveDoctors(doctor: Doctors)
    {
        repo.firebaseSource.saveDoctors(doctor)
            .addOnCompleteListener {
                if (it.isSuccessful)
                {

                    _isRegister.value=StatusResult.OnSuccess()
                }
                else{
                    _isRegister.value=StatusResult.OnError(it.exception?.message.toString())
                }
            }
    }

    fun getSearchSpecialties(name:String)
    {
        _speialitest.value=StatusResult.OnLoading()
        if (hasInternetConnection())
        {
            repo.firebaseSource.getSearchSpecialtiest(name).addOnSuccessListener {
                _speialitest.value=handledata(it)
            }.addOnFailureListener{
                _speialitest.value=StatusResult.OnError(it.message.toString())
                Log.d("mylog","${it.message}")
            }
        }
        else{
            _speialitest.value=StatusResult.OnError("No Internet Connection")

        }

    }

    fun getUserInfo(){
        _userInfo.value=StatusResult.OnLoading()
        repo.firebaseSource.getUserData().addOnSuccessListener {
            if (it.data!=null)
            {
                _userInfo.value=StatusResult.OnSuccess(data=it.toObject(Doctors::class.java))
            }else
            {
                _userInfo.value=StatusResult.OnError("No doucument found")
            }
        }.addOnFailureListener {
            _userInfo.value=StatusResult.OnError("${it.message}")
        }
    }


    private inline fun <reified T>handledata(value: QuerySnapshot?): StatusResult<MutableList<T>>? {
        val data= mutableListOf<T>()
        if (value!=null)
        {
            value.documents.forEach {
                data.add(it.toObject(T::class.java)!!)
            }
            if (data.isEmpty())
            {
                return StatusResult.OnError(msg = "No data Found")
            }
            else{
                return StatusResult.OnSuccess(data = data)
            }
        }else{
            return StatusResult.OnError(msg = "No data Found")
        }
    }


    private fun hasInternetConnection(): Boolean {
        val connectivityManger = getApplication<Application>()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netWorkActive = connectivityManger.activeNetwork ?: return false
        val networkCapability =
            connectivityManger.getNetworkCapabilities(netWorkActive) ?: return false
        when {
            networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
            networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
            else -> return false
        }

    }
}