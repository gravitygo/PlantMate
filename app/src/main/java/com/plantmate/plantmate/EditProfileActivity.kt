package com.plantmate.plantmate

import android.app.Dialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.plantmate.plantmate.databinding.ActivityEditProfileBinding
import com.plantmate.plantmate.fragments.FragmentTopNav
import com.plantmate.plantmate.objects.FragmentUtils.replaceFragment
import com.plantmate.plantmate.objects.FullScreenUtils.setFullScreen

class EditProfileActivity: AppCompatActivity(){

    private lateinit var binding: ActivityEditProfileBinding

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

        binding.gardenNameInput.addTextChangedListener(textWatcher)
        binding.confirmChangeButton.setOnClickListener{
            showConfirmationDialog()
        }

        // replace topNav with top nav fragment
        val topNav = FragmentTopNav(getColor(R.color.primary))
        replaceFragment(topNav, R.id.top_Panel, supportFragmentManager)
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
            Toast.makeText(applicationContext, "Changes saved", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }
}