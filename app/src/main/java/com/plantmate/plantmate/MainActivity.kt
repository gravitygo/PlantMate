package com.plantmate.plantmate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.plantmate.plantmate.databinding.ActivityMainBinding
import com.plantmate.plantmate.fragments.FragmentEntry
import com.plantmate.plantmate.objects.FullScreenUtils.setFullScreen
import com.plantmate.plantmate.objects.FragmentUtils.replaceFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreen(this)
        replaceFragment(FragmentEntry(), R.id.activity_entry_fragment_view, supportFragmentManager)
    }
}