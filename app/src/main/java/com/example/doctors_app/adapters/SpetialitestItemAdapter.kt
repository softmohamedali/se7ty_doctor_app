package com.example.doctors_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.doctors_app.databinding.LayoutSpeialtiesItemBinding
import com.example.doctors_app.models.Specialties
import com.example.doctors_app.utils.MyDiff

class SpetialitestItemAdapter(
    itemCllickListener:SpetialtiesItemClick
):RecyclerView.Adapter<SpetialitestItemAdapter.Vh>() {
    private var specialties= mutableListOf<Specialties>()
    private var itemListener=itemCllickListener
    class Vh(var view: LayoutSpeialtiesItemBinding):RecyclerView.ViewHolder(view.root){

        companion object{
            fun from(parent: ViewGroup):Vh
            {
                val myView=LayoutSpeialtiesItemBinding.inflate(
                    LayoutInflater.from(parent.context),parent,false)
                return Vh(myView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh.from(parent)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.view.nameSpLayout.text=specialties[position].name
        holder.itemView.setOnClickListener {
            specialties[position].name?.let { it1 -> itemListener.itemClick(it1) }
        }
    }

    override fun getItemCount(): Int {
        return specialties.size
    }

    fun setData(newList:MutableList<Specialties>)
    {
        val mydiff=MyDiff(specialties,newList)
        val result=DiffUtil.calculateDiff(mydiff)
        specialties=newList
        result.dispatchUpdatesTo(this)
    }
    public interface SpetialtiesItemClick{
        fun itemClick(name:String)
    }
}

