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
import com.github.sumimakito.awesomeqr.AwesomeQrRenderer
import com.github.sumimakito.awesomeqr.option.RenderOption
import com.github.sumimakito.awesomeqr.option.color.Color
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
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
    private lateinit var db: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    var transactionType = ""
    var stockText = ""
    var priceText = ""
    var totalText = ""
    var actionText = ""
    var plantID = ""
    var primaryColor = 0
    var plantStock = 0
    var plantSale = 0
    var plantWithered = 0

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // if stock and price input is not empty
            if (isValidEntry()){
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

    private fun isValidEntry() : Boolean{
        var flag = false

        if (binding.stockInput.text.toString().trim().isNotEmpty() && binding.priceInput.text.toString().trim().isNotEmpty()){

            val stockInput = binding.stockInput.text.toString().toInt()
            val princeInput = binding.priceInput.text.toString().toFloat()

            if (transactionType == "Sale" || transactionType == "Dispose"){
                if(stockInput <= plantStock && princeInput > 0 && stockInput > 0){
                    return true
                }
            } else if (transactionType == "Propagation" || transactionType == "Purchase"){
                if(princeInput > 0 && stockInput > 0){
                    return true
                }
            }
        }
        return flag
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // set binding and full screen to remove action bar

        super.onCreate(savedInstanceState)
        binding = ActivityModifyPlantBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFullScreen(this)

        db = Firebase.firestore
        mAuth = FirebaseAuth.getInstance()

        plantID = intent.getStringExtra("plantID").toString()
        transactionType = intent.getStringExtra("transactionType")!!
        when (transactionType) {
            "Sale" -> {
                primaryColor = ContextCompat.getColor(this, R.color.primary)
                stockText = "Sold Stocks:"
                priceText = "Price Sold (php):"
                totalText = "Sale Total:"
                actionText = "Sell"
                binding.priceInput.setBackgroundResource(R.drawable.sell_input_box)
                binding.stockInput.setBackgroundResource(R.drawable.sell_input_box)
            }
            "Propagation" -> {
                primaryColor = ContextCompat.getColor(this, R.color.teal_primary)
                stockText = "Propagated Stocks:"
                priceText = "Propagation Cost (php):"
                totalText = "New Stock Total:"
                actionText = "Propagate"
                binding.priceInput.setBackgroundResource(R.drawable.propagate_input_box)
                binding.stockInput.setBackgroundResource(R.drawable.propagate_input_box)
            }
            "Purchase" -> {
                primaryColor = ContextCompat.getColor(this, R.color.purple_primary)
                stockText = "New Stocks:"
                priceText = "Price Bought (php):"
                totalText = "Purchase Total:"
                actionText = "Stock Up"
                binding.priceInput.setBackgroundResource(R.drawable.stockup_input_box)
                binding.stockInput.setBackgroundResource(R.drawable.stockup_input_box)
            }
            "Dispose" -> {
                primaryColor = ContextCompat.getColor(this, R.color.red_primary)
                stockText = "Perished Stocks:"
                priceText = "Price Lost (php):"
                totalText = "Perished Stock Total:"
                actionText = "Dispose"
                binding.priceInput.setBackgroundResource(R.drawable.dispose_input_box)
                binding.stockInput.setBackgroundResource(R.drawable.dispose_input_box)
            }
        }


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
                        plantStock = Integer.parseInt(result.getLong("plantStock").toString())
                        plantSale = Integer.parseInt(result.getLong("plantSale").toString())
                        plantWithered = Integer.parseInt(result.getLong("plantWithered").toString())
                        binding.currentStockText.text = plantStock.toString()
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

        binding.currentStockTitle.setTextColor(primaryColor)
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
            "Sale" -> {
                dialogBox.setBackgroundResource(R.drawable.sell_input_box)
                dialogConfirmationMessage += "sell "
            }
            "Propagation" -> {
                dialogBox.setBackgroundResource(R.drawable.propagate_input_box)
                dialogConfirmationMessage += "propagate "
            }
            "Purchase" -> {
                dialogBox.setBackgroundResource(R.drawable.stockup_input_box)
                dialogConfirmationMessage += "stock up "
            }
            "Dispose" -> {
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


            ///////////////////////////////// INSERT INTO DB HERE

            val userDocRef = db.collection("users").document(mAuth.currentUser?.uid!!)
            val transactionRef = userDocRef.collection("transaction")
            val aggregateRef = userDocRef.collection("aggregate")
            var transactionCount = binding.stockInput.text.toString().toFloat()
            var transactionPrice = binding.priceInput.text.toString().toFloat()
            var transactionCost = transactionCount * transactionPrice


            val transaction = hashMapOf(
                "plantId" to plantID,
                "cost" to transactionCost,
                "date" to FieldValue.serverTimestamp(),
                "type" to transactionType
            )

            transactionRef.add(transaction).addOnSuccessListener {
                Toast.makeText(applicationContext, "Transaction saved", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { e ->
                Toast.makeText(applicationContext, "Transaction error", Toast.LENGTH_SHORT).show()
                Log.d("Transaction Error", e.toString())
            }


            // Convert yourDate to a Calendar object
            val calendar = Calendar.getInstance()

            // Set the time to the start of the day
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.time = Date(calendar.time.time /1000 * 1000)
            var calendarTimeStamp = Timestamp(calendar.time)

            // Query transactions with date within the range of the day
            val query = aggregateRef.whereEqualTo("date", calendarTimeStamp)

            query.get().addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    // There are no documents matching the query
                    var netprofit = 0f
                    var propagation = 0f
                    var purchases = 0f
                    var sales = 0f
                    var withered = 0f

                    when (transactionType) {
                        "Sale" -> {
                            sales = transactionCost
                        }
                        "Propagation" -> {
                            propagation = transactionCost
                        }
                        "Purchase" -> {
                            purchases = transactionCost
                        }
                        "Dispose" -> {
                            withered = transactionCost
                        }
                    }

                    netprofit = sales - propagation - withered - purchases

                    val aggregateInstance = hashMapOf(
                        "date" to calendarTimeStamp,
                        "netprofit" to netprofit,
                        "propagation" to propagation,
                        "purchases" to purchases,
                        "sales" to sales,
                        "withered" to withered
                    )

                    aggregateRef.add(aggregateInstance).addOnSuccessListener {
                        Toast.makeText(applicationContext, "Aggregate saved", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener { e ->
                        Toast.makeText(applicationContext, "Aggregate error", Toast.LENGTH_SHORT).show()
                        Log.d("Transaction Error", e.toString())
                    }


                } else {
                    for (document in documents) {

                        when (transactionType) {
                            "Sale" -> {
                                val aggregateDocRef = aggregateRef.document(document.id)
                                aggregateDocRef.get()
                                    .addOnSuccessListener { documentSnapshot ->
                                        if (documentSnapshot.exists()) {
                                            aggregateDocRef.update("sales", FieldValue.increment(transactionCost.toDouble()))
                                            aggregateDocRef.update("netprofit", FieldValue.increment(transactionCost.toDouble()))
                                        }
                                    }
                            }
                            "Propagation" -> {
                                val aggregateDocRef = aggregateRef.document(document.id)
                                aggregateDocRef.get()
                                    .addOnSuccessListener { documentSnapshot ->
                                        if (documentSnapshot.exists()) {
                                            aggregateDocRef.update("propagation", FieldValue.increment(transactionCost.toDouble()))
                                            aggregateDocRef.update("netprofit", FieldValue.increment((transactionCost* -1).toDouble()))
                                        }
                                    }
                            }
                            "Purchase" -> {
                                val aggregateDocRef = aggregateRef.document(document.id)
                                aggregateDocRef.get()
                                    .addOnSuccessListener { documentSnapshot ->
                                        if (documentSnapshot.exists()) {
                                            aggregateDocRef.update("purchases", FieldValue.increment(transactionCost.toDouble()))
                                            aggregateDocRef.update("netprofit", FieldValue.increment((transactionCost* -1).toDouble()))
                                        }
                                    }
                            }
                            "Dispose" -> {
                                val aggregateDocRef = aggregateRef.document(document.id)
                                aggregateDocRef.get()
                                    .addOnSuccessListener { documentSnapshot ->
                                        if (documentSnapshot.exists()) {
                                            aggregateDocRef.update("withered", FieldValue.increment(transactionCost.toDouble()))
                                            aggregateDocRef.update("netprofit", FieldValue.increment((transactionCost* -1).toDouble()))
                                        }
                                    }
                            }
                        }

                    }
                }
            }.addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }


            if (transactionType == "Sale"){

                val collectionList = listOf("Araceae", "Asphodelaceae", "Cactaceae", "Rutaceae")
                for (col in collectionList) {
                    val plantDocRef = userDocRef.collection(col).document(plantID)
                    plantDocRef.get().addOnSuccessListener { result ->
                        if (result.getString("plantFamily") != null) {
                            plantDocRef.update("plantSale", FieldValue.increment(transactionCount.toLong()))
                            plantDocRef.update("plantStock", FieldValue.increment((transactionCount.toLong()) * -1))
                            plantStock -= transactionCount.toInt()
                            binding.currentStockText.text = plantStock.toString()
                        }
                    }
                }

            }
            else if (transactionType == "Propagation" || transactionType == "Purchase"){
                val collectionList = listOf("Araceae", "Asphodelaceae", "Cactaceae", "Rutaceae")
                for (col in collectionList) {
                    val plantDocRef = userDocRef.collection(col).document(plantID)
                    plantDocRef.get().addOnSuccessListener { result ->
                        if (result.getString("plantFamily") != null) {
                            plantDocRef.update("plantStock", FieldValue.increment(transactionCount.toLong()))
                            plantStock += transactionCount.toInt()
                            binding.currentStockText.text = plantStock.toString()
                        }
                    }
                }
            } else if (transactionType == "Dispose"){
                val collectionList = listOf("Araceae", "Asphodelaceae", "Cactaceae", "Rutaceae")
                for (col in collectionList) {
                    val plantDocRef = userDocRef.collection(col).document(plantID)
                    plantDocRef.get().addOnSuccessListener { result ->
                        if (result.getString("plantFamily") != null) {
                            plantDocRef.update("plantWithered", FieldValue.increment(transactionCount.toLong()))
                            plantDocRef.update("plantStock", FieldValue.increment((transactionCount.toLong()) * -1))
                            plantStock -= transactionCount.toInt()
                            binding.currentStockText.text = plantStock.toString()
                        }
                    }
                }
            }

            // reset stock and price data
            binding.stockInput.text.clear()
            binding.priceInput.text.clear()
            binding.totalPrice.text = "0.00"
            binding.confirmModifyButton.backgroundTintList = ColorStateList.valueOf(getColor(R.color.gray))


        }

        dialog.show()
    }

    private fun ByteArray.toBitmap(): Bitmap {
        return BitmapFactory.decodeByteArray(this, 0, this.size)
    }
}