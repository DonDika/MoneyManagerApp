package com.dondika.moneymanagerapp.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.dondika.moneymanagerapp.ui.BaseActivity
import com.dondika.moneymanagerapp.R
import com.dondika.moneymanagerapp.data.local.PreferenceManager
import com.dondika.moneymanagerapp.ui.auth.login.LoginActivity
import com.dondika.moneymanagerapp.ui.home.HomeActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    private val pref by lazy { PreferenceManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            if (pref.getInt("pref_is_login") == 0){
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                startActivity(Intent(this, HomeActivity::class.java))
            }
            finish()
        }, 2000)

    }


}