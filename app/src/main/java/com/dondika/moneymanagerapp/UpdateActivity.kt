package com.dondika.moneymanagerapp

import android.os.Bundle
import com.dondika.moneymanagerapp.databinding.ActivityCreateBinding

class UpdateActivity : BaseActivity() {

    private val binding by lazy { ActivityCreateBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupListener()
    }

    private fun setupListener() {
        binding.buttonSave.text = "Simpan Perubahan"

    }

}