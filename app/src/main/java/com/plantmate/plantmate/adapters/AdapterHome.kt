package com.plantmate.plantmate.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.plantmate.plantmate.R
import com.plantmate.plantmate.holders.HolderCategory
import com.plantmate.plantmate.holders.HolderHeader
import com.plantmate.plantmate.objects.Plant

class AdapterHome(private val data : HashMap<String, ArrayList<Plant>>, val activity: Activity): RecyclerView.Adapter<RecyclerView.ViewHolder>()  {
    companion object {
        private const val VIEW_TYPE_FIRST = 1
        private const val VIEW_TYPE_NORMAL = 2
    }

    // Determine the view type for a given position
    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            // First item, use first view type
            VIEW_TYPE_FIRST
        } else {
            // Normal item, use normal view type
            VIEW_TYPE_NORMAL
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view : View
        return when (viewType) {
            VIEW_TYPE_FIRST -> {
                // Inflate layout for first view type
                view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_header, parent, false)
                HolderHeader(view)
            }
            VIEW_TYPE_NORMAL -> {
                // Inflate layout for normal view type
                view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_category, parent, false)
                HolderCategory(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return data.size+1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_FIRST -> {
                val headerHolder = holder as HolderHeader
                headerHolder.bind("Chyle", activity)
            }
            VIEW_TYPE_NORMAL -> {
                val key = data.keys.elementAt(position - 1)
                val value = data[key]!!
                val categoryHolder = holder as HolderCategory
                categoryHolder.bind(key, value)
            }
        }
    }
}