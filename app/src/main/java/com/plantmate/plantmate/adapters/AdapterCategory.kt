package com.plantmate.plantmate.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.plantmate.plantmate.R
import com.plantmate.plantmate.ViewPlantActivity
import com.plantmate.plantmate.holders.HolderPlant
import com.plantmate.plantmate.objects.Plant

class AdapterCategory(private val data : ArrayList<Plant>): RecyclerView.Adapter<HolderPlant>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderPlant {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.fragment_product, parent, false)
        return HolderPlant(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: HolderPlant, position: Int) {
        var plant = data.get(position)
        holder.itemView.setOnClickListener(){
            val context = holder.itemView.context
            val goToProduct = Intent(context, ViewPlantActivity::class.java)
            // TODO: pass data with bundle in line 26 (replace null (ata(maybe(i tink))))
            ContextCompat.startActivity(context, goToProduct, null)
        }
        holder.bindData(plant)
    }
}