package com.example.doctors_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.doctors_app.databinding.LayoutDateScheduleItemBinding
import com.example.doctors_app.models.Client
import com.example.doctors_app.models.TimeSchedule
import com.example.doctors_app.utils.MyDiff

class DateScheduleAdapter (
    itemCllickListener: DateScheduleAdapter.DataSchedulementItemClick
): RecyclerView.Adapter<DateScheduleAdapter.Vh>() {
    private var dateScheduleList= mutableListOf<TimeSchedule>()
    private var itemListener=itemCllickListener

    class Vh(var view: LayoutDateScheduleItemBinding): RecyclerView.ViewHolder(view.root){

        companion object{
            fun from(parent: ViewGroup):Vh
            {
                val myView= LayoutDateScheduleItemBinding.inflate(
                    LayoutInflater.from(parent.context),parent,false)
                return Vh(myView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh.from(parent)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.view.fromTimeTvShedule.text="From : ${dateScheduleList[position].fromTime}"
        holder.view.toTimeTvShedule.text="To : ${dateScheduleList[position].toTime}"
        holder.view.dateTvShedule.text="Date : ${dateScheduleList[position].date}"
        holder.view.imgDelteDataitem.setOnClickListener {
            itemListener.deleteItemClick(dateScheduleList[position])
        }
    }

    override fun getItemCount(): Int {
        return dateScheduleList.size
    }

    fun setData(newList:MutableList<TimeSchedule>)
    {
        val mydiff= MyDiff(dateScheduleList,newList)
        val result= DiffUtil.calculateDiff(mydiff)
        dateScheduleList=newList
        result.dispatchUpdatesTo(this)
    }
    public interface DataSchedulementItemClick{
        fun deleteItemClick(dateSchedula: TimeSchedule)
    }
}
