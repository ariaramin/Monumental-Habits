package com.ariaramin.monumentalhabits.SharedPreferences

import android.content.Context
import com.ariaramin.monumentalhabits.Utils.Constants

class SharedPreferencesManager(private val context: Context) {

    fun setOnBoardingStatus(status: Boolean) {
        val sharedPreferences = context.getSharedPreferences(Constants.ON_BOARDING, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(Constants.FINISHED, status)
        editor.apply()
    }

    fun getOnBoardingStatus(): Boolean {
        val sharedPreferences = context.getSharedPreferences(Constants.ON_BOARDING, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(Constants.FINISHED, false)
    }
}