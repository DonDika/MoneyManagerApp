package com.dondika.moneymanagerapp.ui.transaction

import android.os.Bundle
import android.util.Log
import com.dondika.moneymanagerapp.ui.BaseActivity
import com.dondika.moneymanagerapp.databinding.ActivityTransactionBinding

class TransactionActivity : BaseActivity() {

    private val binding by lazy { ActivityTransactionBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupListener()
    }

    private fun setupListener() {
        binding.imageDate.setOnClickListener {
            DateFragment(object : DateFragment.DateListener{
                override fun onSuccess(dateStart: String, dateEnd: String) {
                    Log.e("DATE", "$dateStart and $dateEnd ", )
                }
            }).apply {
                show(supportFragmentManager, "date fragment")
            }
        }
    }
}