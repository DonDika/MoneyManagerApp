package com.dondika.moneymanagerapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.dondika.moneymanagerapp.data.model.Category
import com.dondika.moneymanagerapp.ui.BaseActivity
import com.dondika.moneymanagerapp.ui.create.CreateActivity
import com.dondika.moneymanagerapp.databinding.ActivityHomeBinding
import com.dondika.moneymanagerapp.databinding.HomeAvatarBinding
import com.dondika.moneymanagerapp.databinding.HomeDashboardBinding
import com.dondika.moneymanagerapp.ui.profile.ProfileActivity
import com.dondika.moneymanagerapp.ui.transaction.TransactionActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.math.log


class HomeActivity : BaseActivity() {

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    private lateinit var bindingAvatar: HomeAvatarBinding
    private lateinit var bindingDashboard: HomeDashboardBinding

    private val firestore by lazy { Firebase.firestore }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupListener()

        testFirestore()
    }

    override fun onStart() {
        super.onStart()

        testFirestore()
    }

    private fun testFirestore() {
        val categories: ArrayList<Category> = arrayListOf()
        firestore.collection("category")
            .get()
            .addOnSuccessListener { result ->
                result.forEach { document ->
                    //Log.e("getdata", document.data["name"].toString() )
                    categories.add( Category(document.data["name"].toString()) )
                }
                Log.e("getdata", "categories = $categories" )
            }

    }

    private fun setupBinding() {
        setContentView(binding.root)
        bindingAvatar = binding.includeAvatar
        bindingDashboard = binding.includeDashboard
    }


    private fun setupListener() {
        bindingAvatar.imageAvatar.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        binding.fabCreate.setOnClickListener {
            startActivity(Intent(this, CreateActivity::class.java))
        }
        binding.textTransaction.setOnClickListener {
            startActivity(Intent(this, TransactionActivity::class.java))
        }
    }


}