package com.plantmate.plantmate.holders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.plantmate.plantmate.R
import com.plantmate.plantmate.adapters.AdapterCategory
import com.plantmate.plantmate.objects.Plant

class HolderCategory(itemView: View) : RecyclerView.ViewHolder(itemView)  {
    fun bind(header: String, item:ArrayList<Plant>){
        itemView.findViewById<TextView>(R.id.fragment_category_tv_family).setText(header)
        itemView.findViewById<RecyclerView>(R.id.fragment_category_rv_items).layoutManager =
            LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
        itemView.findViewById<RecyclerView>(R.id.fragment_category_rv_items).adapter = AdapterCategory(item)
    }
}