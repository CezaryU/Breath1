package com.wel.wat.breathalyser

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.wel.wat.breathalyser.database.Measurement
import kotlinx.android.synthetic.main.custom_row.view.*

class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var measurementList = emptyList<Measurement>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)  {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_row, parent, false))
    }

    override fun getItemCount(): Int {
        return measurementList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = measurementList[position]
        holder.itemView.ml_text.text = currentItem.alcoholMl.toString()
        holder.itemView.pr_text.text = currentItem.alcoholPr.toString()
        holder.itemView.sum_text.text = currentItem.percentage.toString()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(measurement: List<Measurement>){
        this.measurementList = measurement
        notifyDataSetChanged()
    }
}