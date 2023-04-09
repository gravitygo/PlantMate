package com.plantmate.plantmate

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.plantmate.plantmate.databinding.ActivityModifyPlantBinding
import com.plantmate.plantmate.fragments.FragmentTopNavModify
import com.plantmate.plantmate.objects.FragmentUtils
import com.plantmate.plantmate.objects.FragmentUtils.replaceFragment
import com.plantmate.plantmate.objects.FragmentUtils.replaceFragmentInit
import com.plantmate.plantmate.objects.FullScreenUtils.setFullScreen
import java.text.NumberFormat
import java.util.*

class PlantTransactionActivity : AppCompatActivity(){

    private lateinit var binding: ActivityModifyPlantBinding
    private val MAX_FILE_SIZE: Long = 1024 * 1024 * 10
    var transactionType = 0
    var primaryColor = 0
    var stockText = ""
    var priceText = ""
    var totalText = ""
    var actionText = ""
    var plantID = ""

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // if stock and price input is not empty
            if (binding.stockInput.text.toString().trim().isNotEmpty() && binding.priceInput.text.toString().trim().isNotEmpty()){
                // update total price
                val stock = Integer.parseInt(binding.stockInput.text.toString())
                val price = binding.priceInput.text.toString().toDouble()
                val total = NumberFormat.getNumberInstance(Locale.US).format(stock*price)
                binding.totalPrice.text = total
                binding.confirmModifyButton.isEnabled = true;
                binding.confirmModifyButton.backgroundTintList = ColorStateList.valueOf(primaryColor)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // set binding and full screen to remove action bar

        super.onCreate(savedInstanceState)
        binding = ActivityModifyPlantBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFullScreen(this)

        plantID = intent.getStringExtra("plantID").toString()
        transactionType = intent.getIntExtra("transactionType", 0)
        when (transactionType) {
            1 -> {
                primaryColor = ContextCompat.getColor(this, R.color.primary)
                stockText = "Sold Stocks:"
                priceText = "Price Sold (php):"
                totalText = "Sale Total:"
                actionText = "Sell"
                binding.priceInput.setBackgroundResource(R.drawable.sell_input_box)
                binding.stockInput.setBackgroundResource(R.drawable.sell_input_box)
            }
            2 -> {
                primaryColor = ContextCompat.getColor(this, R.color.teal_primary)
                stockText = "Propagated Stocks:"
                priceText = "Propagation Value (php):"
                totalText = "New Stock Total:"
                actionText = "Propagate"
                binding.priceInput.setBackgroundResource(R.drawable.propagate_input_box)
                binding.stockInput.setBackgroundResource(R.drawable.propagate_input_box)
            }
            3 -> {
                primaryColor = ContextCompat.getColor(this, R.color.purple_primary)
                stockText = "New Stocks:"
                priceText = "Price Bought (php):"
                totalText = "Purchase Total:"
                actionText = "Stock Up"
                binding.priceInput.setBackgroundResource(R.drawable.stockup_input_box)
                binding.stockInput.setBackgroundResource(R.drawable.stockup_input_box)
            }
            4 -> {
                primaryColor = ContextCompat.getColor(this, R.color.red_primary)
                stockText = "Perished Stocks:"
                priceText = "Price Lost (php):"
                totalText = "Perished Stock Total:"
                actionText = "Dispose"
                binding.priceInput.setBackgroundResource(R.drawable.dispose_input_box)
                binding.stockInput.setBackgroundResource(R.drawable.dispose_input_box)
            }
        }

        val mAuth = FirebaseAuth.getInstance()
        val db = Firebase.firestore

        val collectionList = listOf("Araceae", "Asphodelaceae", "Cactaceae", "Rutaceae")
        for (col in collectionList) {
            db.collection("users").document("${mAuth.currentUser?.uid}").collection(col)
                .document(plantID).get()
                .addOnSuccessListener { result ->
                    if (result.getString("plantFamily") != null) {
                        val imageExtension = result.getString("imageType").toString()
                        Log.d("IMAGE EXTENSION (INSIDE)", imageExtension)
                        binding.sciName.text = result.getString("plantScientificName").toString()
                        binding.cvName.text = result.getString("plantCultivarName").toString()
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

        // add listener for stock/price changes
        binding.stockInput.addTextChangedListener(textWatcher)
        binding.priceInput.addTextChangedListener(textWatcher)

        binding.confirmModifyButton.setOnClickListener{
            showConfirmationDialog()
        }

        // set activity specific text and colors
        binding.stockText.text = stockText
        binding.priceText.text = priceText
        binding.totalTitle.text = totalText
        binding.confirmModifyButton.text = actionText

        binding.stockText.setTextColor(primaryColor)
        binding.priceText.setTextColor(primaryColor)
        binding.totalTitle.setTextColor(primaryColor)
        binding.totalPrice.setTextColor(primaryColor)
        binding.totalPhp.setTextColor(primaryColor)
        binding.cardView.setCardBackgroundColor(primaryColor)
        binding.confirmModifyButton.backgroundTintList = ColorStateList.valueOf(getColor(R.color.gray))

        // replace topNav with top nav fragment
        val topNav = FragmentTopNavModify(primaryColor)
        replaceFragmentInit(topNav, R.id.top_Panel, supportFragmentManager)
    }

    @SuppressLint("InflateParams")
    private fun showConfirmationDialog() {
        // create dialog instance and set view
        val dialog = Dialog(this)
        val view = layoutInflater.inflate(R.layout.modify_confirmation, null)
        val dialogBox = view.findViewById<RelativeLayout>(R.id.dialog_box)
        dialog.setContentView(view)
        var dialogConfirmationMessage = "you are about to "
        // find views by id and set proper data and listeners
        when (transactionType) {
            1 -> {
                dialogBox.setBackgroundResource(R.drawable.sell_input_box)
                dialogConfirmationMessage += "sell "
            }
            2 -> {
                dialogBox.setBackgroundResource(R.drawable.propagate_input_box)
                dialogConfirmationMessage += "propagate "
            }
            3 -> {
                dialogBox.setBackgroundResource(R.drawable.stockup_input_box)
                dialogConfirmationMessage += "stock up "
            }
            4 -> {
                dialogBox.setBackgroundResource(R.drawable.dispose_input_box)
                dialogConfirmationMessage += "dispose "
            }
        }

        val dialogTitle = view.findViewById<TextView>(R.id.dialog_title)
        dialogTitle.setTextColor(primaryColor)

        val dialogMessage = view.findViewById<TextView>(R.id.dialog_message)
        dialogMessage.text = dialogConfirmationMessage +  binding.stockInput.text +" plants"

        val cancelButton = view.findViewById<AppCompatButton>(R.id.dialog_cancel_button)
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        val confirmButton = view.findViewById<AppCompatButton>(R.id.dialog_confirm_button)
        confirmButton.backgroundTintList = ColorStateList.valueOf(primaryColor);
        confirmButton.setOnClickListener {
            dialog.dismiss()

            // reset stock and price data
            binding.stockInput.text.clear()
            binding.priceInput.text.clear()
            binding.totalPrice.text = "0.00"
            binding.confirmModifyButton.backgroundTintList = ColorStateList.valueOf(getColor(R.color.gray))

            Toast.makeText(applicationContext, "Changes saved", Toast.LENGTH_SHORT).show();
        }

        dialog.show()
    }

    private fun ByteArray.toBitmap(): Bitmap {
        return BitmapFactory.decodeByteArray(this, 0, this.size)
    }
}