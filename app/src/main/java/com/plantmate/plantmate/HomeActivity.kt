package com.plantmate.plantmate

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.plantmate.plantmate.databinding.ActivityHomeBinding
import com.plantmate.plantmate.fragments.FragmentChart
import com.plantmate.plantmate.fragments.FragmentHome
import com.plantmate.plantmate.objects.FragmentUtils.replaceFragment
import com.plantmate.plantmate.objects.FullScreenUtils.setFullScreen

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setFullScreen(this)

        val profileButton = binding.activityProfile
        val chartButton = binding.activityHomeChart
        val homeButton = binding.activityHomeHome
        val addButton = binding.activityHomeAdd

        val homeFragment = FragmentHome()
        val chartFragment = FragmentChart()

        replaceFragment(homeFragment, R.id.activity_home_fragment_view, supportFragmentManager)

        homeButton.setOnClickListener(){
            chartButton.setImageResource(R.drawable.chart)
            homeButton.setImageResource(R.drawable.select_home)
            replaceFragment(homeFragment, R.id.activity_home_fragment_view, supportFragmentManager)
        }
        chartButton.setOnClickListener(){
            chartButton.setImageResource(R.drawable.select_chart)
            homeButton.setImageResource(R.drawable.home)
            replaceFragment(chartFragment, R.id.activity_home_fragment_view, supportFragmentManager)
        }

        profileButton.setOnClickListener{
            val goToProfile = Intent(this, EditProfileActivity::class.java)
            startActivity(goToProfile)
        }

        addButton.setOnClickListener(){
            val goToAddPlant = Intent(this, AddPlantActivity::class.java)
            startActivity(goToAddPlant)
        }

        val scanQrButton = binding.activityHomeQr
        scanQrButton.setOnClickListener(){
            val goToScanQR = Intent(this, ScanQrActivity::class.java)
            startActivity(goToScanQR)
        }

    }

}