package com.dondika.moneymanagerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dondika.moneymanagerapp.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}