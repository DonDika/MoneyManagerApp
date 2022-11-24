package com.dondika.moneymanagerapp

import android.os.Bundle
import com.dondika.moneymanagerapp.databinding.ActivityHomeBinding


class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


    }




}