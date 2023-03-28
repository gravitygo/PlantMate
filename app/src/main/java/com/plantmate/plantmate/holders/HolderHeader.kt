package com.plantmate.plantmate.holders

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.plantmate.plantmate.MainActivity
import com.plantmate.plantmate.R

class HolderHeader (itemView: View) : RecyclerView.ViewHolder(itemView)  {
    fun bind(header: String, activity: Activity){
        var name:String
        if(header[header.length-1] =='s'|| header[header.length-1] =='S')
            name="${header}' Garden"
        else
            name="${header}'s Garden"
        itemView.findViewById<TextView>(R.id.fragment_header_tv_garden_name).text = name

        itemView.findViewById<ImageButton>(R.id.fragment_header_btn_logout).setOnClickListener{
            val intent = Intent(itemView.rootView.context, MainActivity::class.java)
            ContextCompat.startActivity(itemView.context, intent, null)
            activity.finishAffinity()
        }
    }
}