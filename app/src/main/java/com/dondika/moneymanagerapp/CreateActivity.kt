package com.dondika.moneymanagerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dondika.moneymanagerapp.databinding.ActivityCreateBinding

class CreateActivity : BaseActivity() {

    private val binding by lazy { ActivityCreateBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }


}