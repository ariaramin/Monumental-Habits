package com.ariaramin.monumentalhabits.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ariaramin.monumentalhabits.R
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        visibleBottomAppbar()
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private fun visibleBottomAppbar() {
        val bottomAppBar =
            requireActivity().findViewById<BottomAppBar>(R.id.bottomAppBar)
        val fab =
            requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        bottomAppBar.visibility = View.VISIBLE
        fab.visibility = View.VISIBLE
    }
}