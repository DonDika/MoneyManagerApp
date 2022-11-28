package com.dondika.moneymanagerapp.data.model

import com.google.firebase.Timestamp

data class User(
    var id: String,
    var name: String,
    var username: String,
    var password: String,
    var created: Timestamp? = Timestamp.now()
)
