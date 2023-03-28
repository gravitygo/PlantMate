package com.plantmate.plantmate.objects

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

object FragmentUtils {
    fun replaceFragment(fragment: Fragment, container: Int, fragmentManager: FragmentManager) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}