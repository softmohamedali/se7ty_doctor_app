package com.example.doctors_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.doctors_app.databinding.LayoutMedicalTestItemBinding
import com.example.doctors_app.models.MedicalTest
import com.example.doctors_app.utils.MyDiff

class MedicalTestItemAdapter (
    itemCllickListener: MedicalTestItemAdapter.MedicalTestItemClick
) : RecyclerView.Adapter<MedicalTestItemAdapter.Vh>() {
    private var userMedicalTestList = mutableListOf<MedicalTest>()
    private var itemListener = itemCllickListener

    class Vh(var view: LayoutMedicalTestItemBinding) : RecyclerView.ViewHolder(view.root) {

        companion object {
            fun from(parent: ViewGroup): Vh {
                val myView = LayoutMedicalTestItemBinding.inflate(
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
        holder.view.btnViewAttachement.setOnClickListener {
            itemListener.btnViewAttachmentClick(userMedicalTestList[position].img!!)
        }
        holder.view.tvLapName.text=userMedicalTestList[position].labName
        holder.view.tvTestDate.text=userMedicalTestList[position].testdate
        holder.view.tvTestResult.text=userMedicalTestList[position].testResult
        holder.view.tvTestName.text=userMedicalTestList[position].testName
        if (userMedicalTestList[position].img.isNullOrEmpty()){
            holder.view.btnViewAttachement.visibility= View.GONE
        }else{
            holder.view.btnViewAttachement.visibility= View.VISIBLE
        }

    }

    override fun getItemCount(): Int {
        return userMedicalTestList.size
    }

    fun setData(newList: MutableList<MedicalTest>) {
        val mydiff = MyDiff(userMedicalTestList, newList)
        val result = DiffUtil.calculateDiff(mydiff)
        userMedicalTestList = newList
        result.dispatchUpdatesTo(this)
    }

    public interface MedicalTestItemClick {
        fun btnViewAttachmentClick(img:String)
    }
}