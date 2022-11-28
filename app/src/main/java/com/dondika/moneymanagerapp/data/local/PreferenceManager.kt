package com.dondika.moneymanagerapp.data.local

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {

    //instance
    private val PREF_NAME = "moneyManager.pref"
    private var sharedPreferences: SharedPreferences
    val editor: SharedPreferences.Editor

    init {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    //helper
    fun put(key: String, value: String){
        editor.putString(key, value).apply()
    }

    fun put(key: String, value: Int){
        editor.putInt(key, value).apply()
    }

    fun getString(key: String): String?{
        return sharedPreferences.getString(key, "")
    }

    fun getInt(key: String): Int?{
        return sharedPreferences.getInt(key, 0)
    }

    fun clear(){
        editor.putInt("pref_is_login", 0).apply()
    }


}










