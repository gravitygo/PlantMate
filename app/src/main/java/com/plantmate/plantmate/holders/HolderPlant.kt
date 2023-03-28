package com.plantmate.plantmate.holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.plantmate.plantmate.R
import com.plantmate.plantmate.objects.Plant

class HolderPlant(itemView: View) : RecyclerView.ViewHolder(itemView)  {
    fun bindData(item: Plant){
        itemView.findViewById<TextView>(R.id.fragment_product_tv_productName).setText("Stock: ${item.plantStock}")
        itemView.findViewById<ImageView>(R.id.fragment_product_iv_productImage).setImageResource(R.drawable.ic_launcher_background)
    }
}