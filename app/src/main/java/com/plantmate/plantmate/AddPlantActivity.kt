package com.plantmate.plantmate

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.plantmate.plantmate.databinding.ActivityAddPlantBinding
import com.plantmate.plantmate.fragments.FragmentTopNav
import com.plantmate.plantmate.objects.FragmentUtils.replaceFragment
import com.plantmate.plantmate.objects.FullScreenUtils.setFullScreen

class AddPlantActivity : AppCompatActivity(){

    private lateinit var binding: ActivityAddPlantBinding
    var ticker = 0
    var imageUploaded = false
    var sciNameFilled = false
    var cvNameFilled = false
    var stockFilled = false
    var descFilled = false
    // for legacy image uploads
    private lateinit var image : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        // set binding and fullscreen
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlantBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFullScreen(this)

        // replace fragment
        val topNav = FragmentTopNav(getColor(R.color.primary))
        replaceFragment(topNav, R.id.top_Panel, supportFragmentManager)

        // list of family types
        val items = listOf("Select Plant Family", "Cactaceae", "Araceae", "Asphodelaceae", "Rutaceae")

        // set up spinner
        val spinner = findViewById<Spinner>(R.id.actv)
        val spinnerAdapter= object : ArrayAdapter<String>(this, R.layout.drop_down_item, items) {

            override fun isEnabled(position: Int): Boolean {
                // Disable the first item from Spinner
                // First item will be used for hint
                return position != 0
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view: TextView = super.getDropDownView(position, convertView, parent) as TextView
                //set the color of first item in the drop down list to gray
                if(position == 0) {
                    view.setTextColor(getColor(R.color.gray))
                } else {
                    //here it is possible to define color for other items by
                    view.setTextColor(getColor(R.color.black))
                }
                return view
            }

        }

        // set spinner adapter and item layout
        spinnerAdapter.setDropDownViewResource(R.layout.drop_down_item)
        spinner.adapter = spinnerAdapter

        // set spinnerListener
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val value = parent!!.getItemAtPosition(position).toString()
                if(value == items[0]){
                    (view as TextView).setTextColor(getColor(R.color.gray))
                }
                if( sciNameFilled && cvNameFilled && (binding.actv.selectedItemPosition != 0) && stockFilled && descFilled){
                    binding.confirmAddButton.isEnabled = true
                    binding.confirmAddButton.backgroundTintList = ColorStateList.valueOf(getColor(R.color.primary))
                }
            }
        }


//        binding.stockInput.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
//            if (hasFocus) {
//
//            } else {
//                Toast.makeText(applicationContext, "Lost the focus", Toast.LENGTH_LONG).show()
//            }
//        }


        binding.sciNameInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if(binding.sciNameInput.text.isNotEmpty()){
                    sciNameFilled = true
                    if(imageUploaded && cvNameFilled && (binding.actv.selectedItemPosition != 0) && stockFilled && descFilled){
                        binding.confirmAddButton.isEnabled = true
                        binding.confirmAddButton.backgroundTintList = ColorStateList.valueOf(getColor(R.color.primary))
                    }
                }
            }
        })

        binding.cvNameInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if(binding.cvNameInput.text.isNotEmpty()){
                    cvNameFilled = true
                    if(imageUploaded && sciNameFilled && (binding.actv.selectedItemPosition != 0) && stockFilled && descFilled){
                        binding.confirmAddButton.isEnabled = true
                        binding.confirmAddButton.backgroundTintList = ColorStateList.valueOf(getColor(R.color.primary))
                    }
                }
            }
        })

        binding.stockInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if(binding.stockInput.text.isNotEmpty()){
                    stockFilled = true
                    if(imageUploaded && sciNameFilled && cvNameFilled && (binding.actv.selectedItemPosition != 0) && descFilled){
                        binding.confirmAddButton.isEnabled = true
                        binding.confirmAddButton.backgroundTintList = ColorStateList.valueOf(getColor(R.color.primary))
                    }
                }
            }
        })


        binding.descriptionInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if(binding.descriptionInput.text.isNotEmpty()){
                    descFilled = true
                    if(imageUploaded && sciNameFilled && cvNameFilled && (binding.actv.selectedItemPosition != 0) && stockFilled){
                        binding.confirmAddButton.isEnabled = true
                        binding.confirmAddButton.backgroundTintList = ColorStateList.valueOf(getColor(R.color.primary))
                    }
                }
            }
        })


        binding.descriptionInput.addTextChangedListener(textWatcher)
        // Registers a photo picker activity launcher in single-select mode.
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                binding.productImage.setImageURI(uri)
                binding.productInputButton.setImageResource(R.drawable.image_remove_button)
                imageUploaded = true
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            binding.productInputButton.setOnClickListener {
                if (ticker == 0){
                    val mimeType = "image/png"
                    pickMedia.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.SingleMimeType(
                                mimeType
                            )
                        )
                    )

                    if( sciNameFilled && cvNameFilled && (binding.actv.selectedItemPosition != 0) && stockFilled && descFilled){
                        binding.confirmAddButton.isEnabled = true
                        binding.confirmAddButton.backgroundTintList = ColorStateList.valueOf(getColor(R.color.primary))
                    }
                    ticker++
                } else if (ticker == 1){
                    binding.productImage.setImageResource(R.drawable.image_input_holder)
                    binding.productInputButton.setImageResource(R.drawable.image_input_button)
                    binding.confirmAddButton.isEnabled = false
                    binding.confirmAddButton.backgroundTintList = ColorStateList.valueOf(getColor(R.color.gray))
                    imageUploaded = false
                    ticker--
                }
            }


        } else {
            binding.productInputButton.setOnClickListener{
                if (ticker == 0){
                    image = findViewById(R.id.product_image)
                    uploadImage(image)
                    if( sciNameFilled && cvNameFilled && (binding.actv.selectedItemPosition != 0) && stockFilled && descFilled){
                        binding.confirmAddButton.isEnabled = true
                        binding.confirmAddButton.backgroundTintList = ColorStateList.valueOf(getColor(R.color.primary))
                    }
                    ticker++
                } else if (ticker == 1){
                    binding.productImage.setImageResource(R.drawable.image_input_holder)
                    binding.productInputButton.setImageResource(R.drawable.image_input_button)
                    binding.confirmAddButton.isEnabled = false
                    binding.confirmAddButton.backgroundTintList = ColorStateList.valueOf(getColor(R.color.gray))
                    imageUploaded = false
                    ticker--
                }
            }
        }


        binding.confirmAddButton.setOnClickListener{
            binding.productImage.setImageResource(R.drawable.image_input_holder)
            binding.productInputButton.setImageResource(R.drawable.image_input_button)
            binding.sciNameInput.text.clear()
            binding.cvNameInput.text.clear()
            binding.actv.setSelection(0)
            binding.stockInput.text.clear()
            binding.descriptionInput.text.clear()
            binding.descriptionInputCount.text = "0/500"
            sciNameFilled = false
            cvNameFilled = false
            stockFilled = false
            descFilled = false
            imageUploaded = false
            ticker = 0
            binding.confirmAddButton.isEnabled = false
            binding.confirmAddButton.backgroundTintList = ColorStateList.valueOf(getColor(R.color.gray))
            Toast.makeText(applicationContext, "Product added", Toast.LENGTH_SHORT).show()


        }
    }

    private fun uploadImage(Image: ImageView?){
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1){
            imageUploaded = true
            image.setImageURI(data?.data)
            binding.productInputButton.setImageResource(R.drawable.image_remove_button)

            binding.productInputButton.setOnClickListener{
                binding.productImage.setImageResource(R.drawable.image_input_holder)
                binding.productInputButton.setImageResource(R.drawable.image_input_button)
                imageUploaded = false
                binding.productInputButton.setOnClickListener{
                    image = findViewById(R.id.product_image)
                    uploadImage(image)
                }

            }

        }
    }


    val textWatcher = object : TextWatcher {

        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            binding.descriptionInputCount.text = s.toString().length.toString() + "/500"
            val sv = findViewById<ScrollView>(R.id.scroll_view)
            val tempView = findViewById<EditText>(R.id.description_input); // the view you'd like to locate
            val loc = IntArray(2)
            tempView.getLocationOnScreen(loc)
            sv.scrollTo(0, sv.height)
        }
    }
}