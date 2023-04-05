package com.plantmate.plantmate.fragments

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.plantmate.plantmate.R
import com.plantmate.plantmate.databinding.FragmentEntryBinding
import com.plantmate.plantmate.objects.FragmentUtils.replaceFragment

class FragmentEntry: Fragment(R.layout.fragment_entry) {
    private lateinit var binding: FragmentEntryBinding
    private lateinit var vgContainer: ViewGroup

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vgContainer = container!!
        binding = FragmentEntryBinding.inflate(inflater)

        val containerHeight = container.parent as ConstraintLayout
        val setHeight = containerHeight.layoutParams

        val fragmentView = R.id.activity_entry_fragment_view

        binding.fragmentEntryLogin.setOnClickListener {
            setHeight.height  = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 700F, resources.displayMetrics).toInt()
            containerHeight.layoutParams = setHeight
            replaceFragment(FragmentLogin(), fragmentView, parentFragmentManager)
        }
        binding.fragmentEntrySignUp.setOnClickListener {
            setHeight.height  = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 700F, resources.displayMetrics).toInt()
            containerHeight.layoutParams = setHeight
            replaceFragment(FragmentSignup(), fragmentView, parentFragmentManager)
        }
        return binding.root
    }

    override fun onResume() {
        val containerHeight = vgContainer.parent as ConstraintLayout

        val setHeight = containerHeight.layoutParams
        setHeight.height  = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300F, resources.displayMetrics).toInt()
        containerHeight.layoutParams = setHeight
        super.onResume()
    }
}