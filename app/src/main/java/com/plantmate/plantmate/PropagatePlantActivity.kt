package com.plantmate.plantmate

import android.annotation.SuppressLint
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
import com.plantmate.plantmate.databinding.ActivityModifyPlantBinding
import com.plantmate.plantmate.fragments.FragmentTopNavModify
import com.plantmate.plantmate.objects.FragmentUtils
import com.plantmate.plantmate.objects.FragmentUtils.replaceFragment
import com.plantmate.plantmate.objects.FragmentUtils.replaceFragmentInit
import com.plantmate.plantmate.objects.FullScreenUtils.setFullScreen
import java.text.NumberFormat
import java.util.*

class PropagatePlantActivity : AppCompatActivity(){

    private lateinit var binding: ActivityModifyPlantBinding

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
                binding.confirmModifyButton.backgroundTintList = ColorStateList.valueOf(getColor(R.color.teal_primary))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // set binding and full screen to remove action bar
        super.onCreate(savedInstanceState)
        binding = ActivityModifyPlantBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFullScreen(this)

        // add listener for stock/price changes
        binding.stockInput.addTextChangedListener(textWatcher)
        binding.priceInput.addTextChangedListener(textWatcher)

        binding.confirmModifyButton.setOnClickListener{
            showConfirmationDialog()
        }

        // set activity specific text and colors
        binding.stockText.text = "Propagated Stocks:"
        binding.priceText.text = "Propagation Value (php):"
        binding.totalTitle.text = "New Stock Total"
        binding.confirmModifyButton.text = "Propagate"

        binding.stockText.setTextColor(getColor(R.color.teal_primary))
        binding.priceText.setTextColor(getColor(R.color.teal_primary))
        binding.totalTitle.setTextColor(getColor(R.color.teal_primary))
        binding.totalPrice.setTextColor(getColor(R.color.teal_primary))
        binding.totalPhp.setTextColor(getColor(R.color.teal_primary))
        binding.cardView.setCardBackgroundColor(getColor(R.color.teal_primary))
        binding.confirmModifyButton.backgroundTintList = ColorStateList.valueOf(getColor(R.color.gray))

        binding.priceInput.setBackgroundResource(R.drawable.propagate_input_box)
        binding.stockInput.setBackgroundResource(R.drawable.propagate_input_box)

        // replace topNav with top nav fragment
        val topNav = FragmentTopNavModify(getColor(R.color.teal_primary))
        replaceFragmentInit(topNav, R.id.top_Panel, supportFragmentManager)
    }

    @SuppressLint("InflateParams")
    private fun showConfirmationDialog() {
        // create dialog instance and set view
        val dialog = Dialog(this)
        val view = layoutInflater.inflate(R.layout.modify_confirmation, null)
        dialog.setContentView(view)

        // find views by id and set proper data and listeners
        val dialogBox = view.findViewById<RelativeLayout>(R.id.dialog_box)
        dialogBox.setBackgroundResource(R.drawable.propagate_input_box)

        val dialogTitle = view.findViewById<TextView>(R.id.dialog_title)
        dialogTitle.setTextColor(getColor(R.color.teal_primary))

        val dialogMessage = view.findViewById<TextView>(R.id.dialog_message)
        dialogMessage.text ="you are about to add ${binding.stockInput.text} propagated plants"

        val cancelButton = view.findViewById<AppCompatButton>(R.id.dialog_cancel_button)
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        val confirmButton = view.findViewById<AppCompatButton>(R.id.dialog_confirm_button)
        confirmButton.backgroundTintList = ColorStateList.valueOf(getColor(R.color.teal_primary));
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
}