package com.plantmate.plantmate

import android.content.ContentValues.TAG
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.plantmate.plantmate.databinding.ActivityViewPlantBinding
import com.plantmate.plantmate.fragments.FragmentTopNav
import com.plantmate.plantmate.objects.FragmentUtils.replaceFragmentInit
import com.plantmate.plantmate.objects.FullScreenUtils.setFullScreen
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

class ViewPlantActivity: AppCompatActivity(){

    private lateinit var binding: ActivityViewPlantBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var plantID: String
    private lateinit var imageExtension: String
    private val MAX_FILE_SIZE: Long = 1024 * 1024 * 10
    override fun onCreate(savedInstanceState: Bundle?) {
        // set binding and fullscreen
        super.onCreate(savedInstanceState)
        binding = ActivityViewPlantBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFullScreen(this)

        // show bottom sheet on button click
        binding.dealButton.setOnClickListener{
            showBottomSheetDialog()
        }

        // replace fragment
        val topNav = FragmentTopNav(getColor(R.color.primary))
        replaceFragmentInit(topNav, R.id.top_Panel, supportFragmentManager)

        mAuth = FirebaseAuth.getInstance()
        db = Firebase.firestore

        plantID = intent.getStringExtra("plantID").toString()

        val collectionList = listOf("Araceae", "Asphodelaceae", "Cactaceae", "Rutaceae")
        for (col in collectionList) {
            db.collection("users").document("${mAuth.currentUser?.uid}").collection(col)
                .document(plantID).get()
                .addOnSuccessListener { result ->
                    if (result.getString("plantFamily") != null) {
                        imageExtension = result.getString("imageType").toString()
                        Log.d("IMAGE EXTENSION (INSIDE)", imageExtension)
                        binding.sciName.text = result.getString("plantScientificName").toString()
                        binding.cvName.text = result.getString("plantCultivarName").toString()
                        binding.descriptionBody.text = result.getString("plantDescription").toString()

                        binding.stockCount.text = result.getLong("plantStock").toString()
                        binding.saleCount.text = result.getLong("plantSale").toString()
                        binding.witheredCount.text = result.getLong("plantWithered").toString()
                        // api call2
                        val storageRef = Firebase.storage.reference
                        val pathRef = mAuth.currentUser?.uid.toString() + "/" + plantID + "." +imageExtension
                        val imageRef = storageRef.child(pathRef)
                        Log.d("PATH REFERENCE!", pathRef)
                        imageRef.getBytes(MAX_FILE_SIZE).addOnSuccessListener { res ->
                            binding.productImage.setImageBitmap(res.toBitmap())
                        }
                    }
                }
        }

    }

    private fun ByteArray.toBitmap(): Bitmap {
        return BitmapFactory.decodeByteArray(this, 0, this.size)
    }

    @SuppressLint("InflateParams")
    private fun showBottomSheetDialog() {
        // create bottomSheetDialog instance and set view
        val dialog = BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.modify_slider, null)
        dialog.setContentView(view)

        // find views by id and set listeners
        val sellButton = view.findViewById<ConstraintLayout>(R.id.sell)
        sellButton.setOnClickListener {
            val goToSell = Intent(this, PlantTransactionActivity::class.java)
            goToSell.putExtra("transactionType", "Sale")
            goToSell.putExtra("plantID", plantID)
            startActivity(goToSell)
        }

        val propagateButton = view.findViewById<ConstraintLayout>(R.id.propagate)
        propagateButton.setOnClickListener {
            val goToPropagate = Intent(this, PlantTransactionActivity::class.java)
            goToPropagate.putExtra("transactionType", "Propagation")
            goToPropagate.putExtra("plantID", plantID)
            startActivity(goToPropagate)
        }

        val stockupButton = view.findViewById<ConstraintLayout>(R.id.stockup)
        stockupButton.setOnClickListener {
            val goToStockUp = Intent(this, PlantTransactionActivity::class.java)
            goToStockUp.putExtra("transactionType", "Purchase")
            goToStockUp.putExtra("plantID", plantID)
            startActivity(goToStockUp)
        }

        val disposeButton = view.findViewById<ConstraintLayout>(R.id.dispose)
        disposeButton.setOnClickListener {
            val goToDispose = Intent(this, PlantTransactionActivity::class.java)
            goToDispose.putExtra("transactionType", "Dispose")
            goToDispose.putExtra("plantID", plantID)
            startActivity(goToDispose)
        }
        dialog.show()
    }
}