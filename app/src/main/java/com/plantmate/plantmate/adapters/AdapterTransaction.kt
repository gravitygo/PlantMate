package com.plantmate.plantmate.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.plantmate.plantmate.R
import com.plantmate.plantmate.holders.HolderTransaction
import com.plantmate.plantmate.objects.TransactionData

class AdapterTransaction(private var data : ArrayList<TransactionData>): RecyclerView.Adapter<HolderTransaction>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderTransaction {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.fragment_transaction_item, parent, false)
        return HolderTransaction(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun notifyChange(updateData : ArrayList<TransactionData>){
        data = updateData
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: HolderTransaction, position: Int) {
        holder.bindData(data[position])
    }
}