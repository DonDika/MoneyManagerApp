package com.dondika.moneymanagerapp.utils

import com.google.firebase.Timestamp
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    const val PREF_IS_LOGIN = "pref_is_login"
    const val PREF_NAME = "pref_name"
    const val PREF_USERNAME = "pref_username"
    const val PREF_PASSWORD = "pref_password"
    const val PREF_DATE = "pref_date"
    const val PREF_AVATAR = "pref_avatar"


    fun timestampToString(timestamp: Timestamp): String? {
        return if (timestamp != null){
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            dateFormat.format(timestamp.toDate())
        } else {
            null
        }
    }

    fun amountFormat(number: Int): String{
        val numberFormat: NumberFormat = DecimalFormat("#,###")
        return numberFormat.format(number)
    }



}