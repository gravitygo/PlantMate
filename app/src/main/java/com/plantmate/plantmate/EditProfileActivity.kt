package com.plantmate.plantmate

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.plantmate.plantmate.databinding.ActivityEditProfileBinding
import com.plantmate.plantmate.fragments.FragmentTopNav
import com.plantmate.plantmate.objects.FragmentUtils
import com.plantmate.plantmate.objects.FragmentUtils.replaceFragment
import com.plantmate.plantmate.objects.FragmentUtils.replaceFragmentInit
import com.plantmate.plantmate.objects.FullScreenUtils.setFullScreen

class EditProfileActivity: AppCompatActivity(){

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var mAuth: FirebaseAuth
    val db = Firebase.firestore
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (binding.gardenNameInput.text.toString().trim().isNotEmpty()){
                binding.confirmChangeButton.isEnabled = true
                binding.confirmChangeButton.backgroundTintList = ColorStateList.valueOf(getColor(R.color.primary))
            }
        }
    }

    // TODO (Lind): make changes for garden name from db

    override fun onCreate(savedInstanceState: Bundle?) {
        // set binding and full screen to remove action bar
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFullScreen(this)

        mAuth = FirebaseAuth.getInstance()
        val db = Firebase.firestore

        db.collection("users").document("${mAuth.currentUser?.uid}")
            .get()
            .addOnSuccessListener { result ->
                binding.gardenNameInput.setText(result.getString("gardenName"))
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }

        binding.gardenNameInput.addTextChangedListener(textWatcher)
        binding.confirmChangeButton.setOnClickListener{
            showConfirmationDialog()
        }

        // replace topNav with top nav fragment
        val topNav = FragmentTopNav(getColor(R.color.primary))
        replaceFragmentInit(topNav, R.id.top_Panel, supportFragmentManager)
    }

    private fun showConfirmationDialog() {
        // create dialog instance and set view
        val dialog = Dialog(this)
        val view = layoutInflater.inflate(R.layout.modify_confirmation, null)
        dialog.setContentView(view)

        // find views by id and set proper data and listeners
        val dialogBox = view.findViewById<RelativeLayout>(R.id.dialog_box)
        dialogBox.setBackgroundResource(R.drawable.sell_input_box)

        val dialogTitle = view.findViewById<TextView>(R.id.dialog_title)
        dialogTitle.setTextColor(getColor(R.color.primary))

        val dialogMessage = view.findViewById<TextView>(R.id.dialog_message)
        dialogMessage.text ="you are about to change your garden name to \"" + binding.gardenNameInput.text + "\""

        val cancelButton = view.findViewById<AppCompatButton>(R.id.dialog_cancel_button)
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        val confirmButton = view.findViewById<AppCompatButton>(R.id.dialog_confirm_button)
        confirmButton.backgroundTintList = ColorStateList.valueOf(getColor(R.color.primary))
        confirmButton.setOnClickListener {
            binding.confirmChangeButton.isEnabled = false
            binding.confirmChangeButton.backgroundTintList = ColorStateList.valueOf(getColor(R.color.gray))
            db.collection("users")
                .document("${mAuth.currentUser?.uid}")
                .update("gardenName", "${binding.gardenNameInput.text}")
            Toast.makeText(applicationContext, "Changes saved", Toast.LENGTH_SHORT).show()
            val goToHome = Intent(this, HomeActivity::class.java)
            startActivity(goToHome)
            dialog.dismiss()
        }

        dialog.show()
    }
}