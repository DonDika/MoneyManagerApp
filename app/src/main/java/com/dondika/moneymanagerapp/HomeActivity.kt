package com.dondika.moneymanagerapp

import android.content.Intent
import android.os.Bundle
import com.dondika.moneymanagerapp.databinding.ActivityHomeBinding
import com.dondika.moneymanagerapp.databinding.HomeAvatarBinding
import com.dondika.moneymanagerapp.databinding.HomeDashboardBinding


class HomeActivity : BaseActivity() {

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    private lateinit var bindingAvatar: HomeAvatarBinding
    private lateinit var bindingDashboard: HomeDashboardBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupListener()

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
    }


}