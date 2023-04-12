package com.plantmate.plantmate.holders

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.plantmate.plantmate.R
import com.plantmate.plantmate.objects.TransactionData
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class HolderTransaction(itemView: View) : RecyclerView.ViewHolder(itemView) {
    @SuppressLint("SetTextI18n")
    fun bindData(item: TransactionData){
        itemView.findViewById<TextView>(R.id.fragment_transaction_item_tv_transaction_date).text = "Date: ${getDateWithoutTime(item.date.toDate())}"
        itemView.findViewById<TextView>(R.id.fragment_transaction_item_tv_transaction_type).text = item.transactionType
        itemView.findViewById<TextView>(R.id.fragment_transaction_item_tv_cost).text = "Cost: ${DecimalFormat("₱#,##0.00").format(item.cost.toFloat())}"
        itemView.findViewById<TextView>(R.id.fragment_transaction_item_tv_product_id).text = "Plant Name: ${item.plantName}"
    }
    fun getDateWithoutTime(date: Date): String {
        return SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault()).format(date)
    }
}