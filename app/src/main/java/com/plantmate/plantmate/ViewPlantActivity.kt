package com.plantmate.plantmate

import android.content.ContentValues.TAG
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.plantmate.plantmate.databinding.ActivityViewPlantBinding
import com.plantmate.plantmate.fragments.FragmentTopNav
import com.plantmate.plantmate.objects.FragmentUtils.replaceFragmentInit
import com.plantmate.plantmate.objects.FullScreenUtils.setFullScreen

class ViewPlantActivity: AppCompatActivity(){

    private lateinit var binding: ActivityViewPlantBinding
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        //TODO: REMOVE THIS BUT THIS IS FOR LOGGING PURPOSES
        Log.d("Hi", "${intent.getStringExtra("Hi")}")
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
        val db = Firebase.firestore

        val plantID: String = intent.getStringExtra("plantID").toString()
        val plantFam: String = intent.getStringExtra("plantFam").toString()
        Log.d("VIEW11111111", plantID)
        Log.d("VIEW11111111", plantFam)

//        db.collection("users").document("${mAuth.currentUser?.uid}").collection(plantFam).document(plantID).get()
//            .addOnSuccessListener { result ->
//                Log.d("PRODUCT", "+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+")
//
//                binding.sciName.text = result.getString("plantScientificName").toString()
//                binding.cvName.text = result.getString("plantCultivarName").toString()
//                binding.descriptionBody.text = result.getString("plantDescription").toString()
//            }

        db.collection("users").document("${mAuth.currentUser?.uid}").collection("Asphodelaceae").document(plantID).get()
            .addOnSuccessListener { result ->
                Log.d("PRODUCT Aspho", "+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+" + result.id)

                binding.sciName.text = result.getString("plantScientificName").toString()
                binding.cvName.text = result.getString("plantCultivarName").toString()
                binding.descriptionBody.text = result.getString("plantDescription").toString()


                Log.d("PRODUCT Asphooooaceae", result.getString("plantScientificName").toString())
                Log.d("PRODUCT Asphooooaceae", result.getString("plantCultivarName").toString())
                Log.d("PRODUCT Asphooooaceae", result.getString("plantDescription").toString())
            }

        db.collection("users").document("${mAuth.currentUser?.uid}").collection("Araceae").document(plantID).get()
            .addOnSuccessListener { result ->
                Log.d("PRODUCT Araceae", "+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+" + result.id)

                binding.sciName.text = result.getString("plantScientificName").toString()
                binding.cvName.text = result.getString("plantCultivarName").toString()
                binding.descriptionBody.text = result.getString("plantDescription").toString()

                Log.d("PRODUCT ARAaceae", result.getString("plantScientificName").toString())
                Log.d("PRODUCT ARAaceae", result.getString("plantCultivarName").toString())
                Log.d("PRODUCT ARAaceae", result.getString("plantDescription").toString())
            }


        db.collection("users").document("${mAuth.currentUser?.uid}").collection("Cactaceae").document(plantID).get()
            .addOnSuccessListener { result ->
                Log.d("PRODUCT Cactaceae", "+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+" + result.id)

                binding.sciName.text = result.getString("plantScientificName").toString()
                binding.cvName.text = result.getString("plantCultivarName").toString()
                binding.descriptionBody.text = result.getString("plantDescription").toString()

                Log.d("PRODUCT Cactaceae", result.getString("plantScientificName").toString())
                Log.d("PRODUCT Cactaceae", result.getString("plantCultivarName").toString())
                Log.d("PRODUCT Cactaceae", result.getString("plantDescription").toString())

            }


        db.collection("users").document("${mAuth.currentUser?.uid}").collection("Rutaceae").document(plantID).get()
            .addOnSuccessListener { result ->
                Log.d("PRODUCT Rutaceae", "+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+" + result.id)

                binding.sciName.text = result.getString("plantScientificName").toString()
                binding.cvName.text = result.getString("plantCultivarName").toString()
                binding.descriptionBody.text = result.getString("plantDescription").toString()

                Log.d("PRODUCT RUTAceae", result.getString("plantScientificName").toString())
                Log.d("PRODUCT RUTAceae", result.getString("plantCultivarName").toString())
                Log.d("PRODUCT RUTAceae", result.getString("plantDescription").toString())
            }
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
            val goToSell = Intent(this, SellPlantActivity::class.java)
            startActivity(goToSell)
        }

        val propagateButton = view.findViewById<ConstraintLayout>(R.id.propagate)
        propagateButton.setOnClickListener {
            val goToPropagate = Intent(this, PropagatePlantActivity::class.java)
            startActivity(goToPropagate)
        }

        val stockupButton = view.findViewById<ConstraintLayout>(R.id.stockup)
        stockupButton.setOnClickListener {
            val goToStockUp = Intent(this, StockUpPlantActivity::class.java)
            startActivity(goToStockUp)
        }

        val disposeButton = view.findViewById<ConstraintLayout>(R.id.dispose)
        disposeButton.setOnClickListener {
            val goToDispose = Intent(this, DisposePlantActivity::class.java)
            startActivity(goToDispose)
        }
        dialog.show()
    }
}