package com.plantmate.plantmate.holders

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.plantmate.plantmate.R
import com.plantmate.plantmate.ViewPlantActivity
import com.plantmate.plantmate.objects.Plant

class HolderPlant(itemView: View) : RecyclerView.ViewHolder(itemView)  {
    private val MAX_FILE_SIZE: Long = 1024 * 1024 * 10
    fun bindData(item: Plant){
        itemView.findViewById<TextView>(R.id.fragment_product_tv_productName).text = "Stock: ${item.plantStock}"

        val mAuth = FirebaseAuth.getInstance()
        val db = Firebase.firestore

        val collectionList = listOf("Araceae", "Asphodelaceae", "Cactaceae", "Rutaceae")
        for (col in collectionList) {
            db.collection("users").document("${mAuth.currentUser?.uid}").collection(col)
                .document(item.plantId).get()
                .addOnSuccessListener { result ->
                    if (result.getString("plantFamily") != null) {
                        var imageExtension = result.getString("imageType").toString()
                        val storageRef = Firebase.storage.reference
                        val pathRef = mAuth.currentUser?.uid.toString() + "/" + item.plantId + "." + imageExtension
                        val imageRef = storageRef.child(pathRef)
                        Log.d("PATH REFERENCE!", pathRef)
                        imageRef.getBytes(MAX_FILE_SIZE).addOnSuccessListener { res ->
                            itemView.findViewById<ImageView>(R.id.fragment_product_iv_productImage).setImageBitmap(res.toBitmap())
                        }
                    }
                }
        }

        itemView.findViewById<ImageView>(R.id.fragment_product_iv_productImage).setOnClickListener{
            val intent = Intent(itemView.context, ViewPlantActivity::class.java)
            intent.putExtra("plantID", item.plantId)
            startActivity(itemView.context, intent, null)
        }

    }

    private fun ByteArray.toBitmap(): Bitmap {
        return BitmapFactory.decodeByteArray(this, 0, this.size)
    }

}