package com.example.doctors_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.doctors_app.databinding.LayoutMedicationItemBinding
import com.example.doctors_app.models.Medecation
import com.example.doctors_app.utils.MyDiff

class MedicationItemAdapter () : RecyclerView.Adapter<MedicationItemAdapter.Vh>() {
    private var userMedicationList = mutableListOf<Medecation>()

    class Vh(var view: LayoutMedicationItemBinding) : RecyclerView.ViewHolder(view.root) {

        companion object {
            fun from(parent: ViewGroup): Vh {
                val myView = LayoutMedicationItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                return Vh(myView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh.from(parent)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.view.tvMedicationName.text=userMedicationList[position].medicationName
        holder.view.tvNote.text=userMedicationList[position].notes
    }

    override fun getItemCount(): Int {
        return userMedicationList.size
    }

    fun setData(newList: MutableList<Medecation>) {
        val mydiff = MyDiff(userMedicationList, newList)
        val result = DiffUtil.calculateDiff(mydiff)
        userMedicationList = newList
        result.dispatchUpdatesTo(this)
    }


}