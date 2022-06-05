package com.ariaramin.monumentalhabits

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ariaramin.monumentalhabits.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        linkBottomNavigationViewToNavHost()
    }

    private fun linkBottomNavigationViewToNavHost() {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHost.navController
        setUpBottomNavigationView(navController)
    }

    private fun setUpBottomNavigationView(navController: NavController) {
        binding.bottomNavigationView.itemIconTintList = null
        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}