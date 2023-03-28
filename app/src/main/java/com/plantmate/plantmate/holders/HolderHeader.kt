package com.plantmate.plantmate.holders

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.plantmate.plantmate.MainActivity
import com.plantmate.plantmate.R

class HolderHeader (itemView: View) : RecyclerView.ViewHolder(itemView)  {
    private lateinit var mAuth: FirebaseAuth
    fun bind(header: String, activity: Activity){
        val db = Firebase.firestore
        mAuth = FirebaseAuth.getInstance()
        var name:String
        db.collection("users").document("${mAuth.currentUser?.uid}")
            .get()
            .addOnSuccessListener { result ->
                itemView.findViewById<TextView>(R.id.fragment_header_tv_garden_name).text = result.getString("gardenName")
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }

        itemView.findViewById<ImageButton>(R.id.fragment_header_btn_logout).setOnClickListener{
            mAuth.signOut()
            val intent = Intent(itemView.rootView.context, MainActivity::class.java)
            ContextCompat.startActivity(itemView.context, intent, null)
            activity.finishAffinity()
        }
    }
}