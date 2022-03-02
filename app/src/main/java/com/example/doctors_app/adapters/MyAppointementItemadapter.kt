package com.example.doctors_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.doctors_app.databinding.LayoutMyappointmentItemBinding
import com.example.doctors_app.models.Appointeiment
import com.example.doctors_app.models.Client
import com.example.doctors_app.utils.MyDiff

class MyAppointementItemadapter (
    itemCllickListener: MyAppointementItemadapter.MyAppointementItemClick
        ): RecyclerView.Adapter<MyAppointementItemadapter.Vh>() {
    private var todayAppoientmentList= mutableListOf<Appointeiment>()
    private var itemListener=itemCllickListener

    class Vh(var view: LayoutMyappointmentItemBinding): RecyclerView.ViewHolder(view.root){

        companion object{
            fun from(parent: ViewGroup):Vh
            {
                val myView= LayoutMyappointmentItemBinding.inflate(
                    LayoutInflater.from(parent.context),parent,false)
                return Vh(myView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh.from(parent)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.view.namePatientTv.text=todayAppoientmentList[position].client?.name
        holder.view.fromTimeTv.text="time : from${todayAppoientmentList[position].timeSchedule?.fromTime}"+
                " to ${todayAppoientmentList[position].timeSchedule?.toTime}"
        holder.view.toTimeTv.text="Date: ${todayAppoientmentList[position].timeSchedule?.date}"
        holder.view.viewEmrBtn.setOnClickListener {
            itemListener.viewEmrClick(todayAppoientmentList[position].client!!)
        }
        holder.view.startChatBtn.setOnClickListener {
            itemListener.startChatClick(todayAppoientmentList[position].clientId!!)
        }
    }

    override fun getItemCount(): Int {
        return todayAppoientmentList.size
    }

    fun setData(newList:MutableList<Appointeiment>)
    {
        val mydiff= MyDiff(todayAppoientmentList,newList)
        val result= DiffUtil.calculateDiff(mydiff)
        todayAppoientmentList=newList
        result.dispatchUpdatesTo(this)
    }
    public interface MyAppointementItemClick{
        fun viewEmrClick(client:Client)
        fun startChatClick(client:String)
    }
}

