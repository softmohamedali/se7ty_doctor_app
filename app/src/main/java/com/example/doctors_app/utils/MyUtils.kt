package com.example.doctors_app.utils

import android.util.Log
import com.example.doctors_app.models.Appointeiment
import com.example.doctors_app.models.TimeSchedule
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object MyUtils {
    inline fun <reified T> handledata(value: QuerySnapshot?): StatusResult<MutableList<T>> {
        val data = mutableListOf<T>()
        if (value != null) {
            value.documents.forEach {
                data.add(it.toObject(T::class.java)!!)
            }
            if (data.isEmpty()) {
                return StatusResult.OnError(msg = "No data Found")
            } else {
                return StatusResult.OnSuccess(data = data)
            }
        } else {
            return StatusResult.OnError(msg = "No data Found")
        }
    }

    inline fun <reified T> handleSingledata(it: Task<DocumentSnapshot>): StatusResult<T>? {
        if (it.result != null) {
            Log.d("mylog", "${it.result.toObject(T::class.java)!!}")
            return StatusResult.OnSuccess(data = it.result.toObject(T::class.java)!!)
        } else {
            return StatusResult.OnError(msg = "No data Found")
        }
    }

    fun handledataTimeShedula(
        value: QuerySnapshot?,
        onExpiredDay:(TimeSchedule)->Unit
    ): StatusResult<MutableList<TimeSchedule>>{
        val todayDate=formateDate(LocalDateTime.now(),"dd-MM-yyyy").split('-')
        val data= mutableListOf<TimeSchedule>()
        if (value!=null)
        {
            value.documents.forEach {
                data.add(it.toObject(TimeSchedule::class.java)!!)
            }
            Log.d("mylg","${data.size}")
            data.forEach {
                Log.d("mylg","fere")
                var valid=true
                val timeScheduleDate=it.date!!.split('-')
                if (todayDate[2].toInt()>timeScheduleDate[2].toInt()){
                    valid=false
                }
                if (todayDate[1].toInt()>timeScheduleDate[1].toInt()){
                    valid=false
                }
                if (todayDate[0].toInt()>timeScheduleDate[0].toInt()){
                    valid=false
                }
                if (!valid){
                    onExpiredDay(it)
                }
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


    fun handledataAppointementForDay(
        value: QuerySnapshot?,
    ): StatusResult<MutableList<Appointeiment>>{
        Log.d("mylog","i.m here")
        val todayDate=formateDate(LocalDateTime.now(),"dd-MM-yyyy").split('-')
        val data= mutableListOf<Appointeiment>()
        if (value!=null)
        {
            value.documents.forEach {
                val appointment=it.toObject(Appointeiment::class.java)!!
                val timeScheduleDate=appointment.timeSchedule?.date!!.split('-')
                if (todayDate[0].toInt()==timeScheduleDate[0].toInt()){
                    data.add(appointment)
                }
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


    fun formateDate(dateTime: LocalDateTime,pattern:String):String{
        val mypattern= DateTimeFormatter.ofPattern(pattern)
        return dateTime.format(mypattern)
    }

//    fun toastSuccessBooking(context: Context, msg: String) {
//        var toast = Toast(context)
//        toast.duration = Toast.LENGTH_LONG
//        val view = LayoutToastSuccessBinding.inflate(LayoutInflater.from(context))
//        view.textView9.text = msg
//        toast.view = view.root
//        toast.setGravity(Gravity.CENTER, 0, 0)
//        toast.show()
//    }
}