package com.plantmate.plantmate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.plantmate.plantmate.databinding.ActivityMainBinding
import com.plantmate.plantmate.fragments.FragmentEntry
import com.plantmate.plantmate.objects.FullScreenUtils.setFullScreen
import com.plantmate.plantmate.objects.FragmentUtils.replaceFragment
import com.plantmate.plantmate.objects.FragmentUtils.replaceFragmentInit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(FirebaseAuth.getInstance().currentUser!=null){
            val intent = Intent(binding.root.context, HomeActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }else{
            setFullScreen(this)
            replaceFragmentInit(FragmentEntry(), R.id.activity_entry_fragment_view, supportFragmentManager)
        }
    }
}