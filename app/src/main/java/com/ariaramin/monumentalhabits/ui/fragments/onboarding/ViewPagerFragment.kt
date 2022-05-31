package com.ariaramin.monumentalhabits.ui.fragments.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.ariaramin.monumentalhabits.R
import com.ariaramin.monumentalhabits.databinding.FragmentViewPagerBinding
import com.ariaramin.monumentalhabits.ui.fragments.onboarding.screen.FirstScreenFragment
import com.ariaramin.monumentalhabits.ui.fragments.onboarding.screen.FourthScreenFragment
import com.ariaramin.monumentalhabits.ui.fragments.onboarding.screen.SecondScreenFragment
import com.ariaramin.monumentalhabits.ui.fragments.onboarding.screen.ThirdScreenFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ViewPagerFragment : Fragment() {

    private lateinit var binding: FragmentViewPagerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        handleViewPager()
        changeNextTextViewText()
        binding.nextTextView.setOnClickListener {
            if (getItem(0) < 3) binding.onboardingViewPager.currentItem = getItem(1)
        }

        return binding.root
    }

    private fun handleViewPager() {
        val fragmentList = listOf(
            FirstScreenFragment(),
            SecondScreenFragment(),
            ThirdScreenFragment(),
            FourthScreenFragment()
        )
        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )
        binding.onboardingViewPager.adapter = adapter
        binding.viewPagerIndicator.attachTo(binding.onboardingViewPager)
    }

    private fun changeNextTextViewText() {
        binding.onboardingViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 3) {
                    binding.nextTextView.text = getString(R.string.start)
                } else {
                    binding.nextTextView.text = getString(R.string.next)
                }
            }
        })
    }

    private fun getItem(i: Int): Int {
        return binding.onboardingViewPager.currentItem + i
    }
}