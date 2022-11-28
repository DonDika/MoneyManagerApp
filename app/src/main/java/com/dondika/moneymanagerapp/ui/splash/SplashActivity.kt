package com.dondika.moneymanagerapp.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.dondika.moneymanagerapp.ui.BaseActivity
import com.dondika.moneymanagerapp.R
import com.dondika.moneymanagerapp.ui.auth.login.LoginActivity
import com.dondika.moneymanagerapp.ui.home.HomeActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            //startActivity(Intent(this, HomeActivity::class.java))
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 2000)
    }


}