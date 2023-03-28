package com.plantmate.plantmate

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.plantmate.plantmate.databinding.ActivityViewPlantBinding
import com.plantmate.plantmate.fragments.FragmentTopNav
import com.plantmate.plantmate.objects.FullScreenUtils.setFullScreen

class ViewPlantActivity: AppCompatActivity(){

    private lateinit var binding: ActivityViewPlantBinding
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
        replaceFragment(topNav)
    }

    private fun replaceFragment(fragment: Fragment){
        // instantiate fragment manager and replace with given fragment
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        fragmentTransaction.replace(R.id.top_Panel, fragment)
        fragmentTransaction.commit()
    }

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