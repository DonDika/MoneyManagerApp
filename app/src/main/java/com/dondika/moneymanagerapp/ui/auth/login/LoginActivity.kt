package com.dondika.moneymanagerapp.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.dondika.moneymanagerapp.R
import com.dondika.moneymanagerapp.data.local.PreferenceManager
import com.dondika.moneymanagerapp.data.model.User
import com.dondika.moneymanagerapp.ui.BaseActivity
import com.dondika.moneymanagerapp.ui.auth.register.RegisterActivity
import com.dondika.moneymanagerapp.databinding.ActivityLoginBinding
import com.dondika.moneymanagerapp.ui.home.HomeActivity
import com.dondika.moneymanagerapp.utils.Utils
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : BaseActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val firestore by lazy { Firebase.firestore }
    private val pref by lazy { PreferenceManager(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupListener()
    }

    private fun setupListener() {
        binding.textRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        binding.buttonLogin.setOnClickListener {
            if (isRequired()){
                login()
            } else {
                Toast.makeText(this, "Isi data dengan lengkap!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun login(){
        progress(true)
        firestore.collection("user")
            //checking username and password
            .whereEqualTo("username", binding.editUsername.text.toString())
            .whereEqualTo("password", binding.editPassword.text.toString())
            .get()
            .addOnSuccessListener { result ->
                progress(false)
                if (result.isEmpty){
                    binding.textAlert.visibility = View.VISIBLE
                } else {
                    result.forEach { document ->
                        //save user data
                        val userData = User(
                            document.data["name"].toString(),
                            document.data["username"].toString(),
                            document.data["password"].toString(),
                            document.data["created"] as Timestamp
                        )
                        saveSession(userData)
                    }
                    Toast.makeText(this, "welcome !", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
            }
    }

    private fun progress(isProgress: Boolean){
        binding.textAlert.visibility = View.GONE
        when(isProgress){
            true -> {
                binding.buttonLogin.apply {
                    text = "Loading"
                    isEnabled = false
                }
            }
            false -> {
                binding.buttonLogin.apply {
                    text = "Login"
                    isEnabled = true
                }
            }
        }
    }

    private fun isRequired(): Boolean {
        return (
            binding.editUsername.text.toString().isNotEmpty() &&
            binding.editPassword.text.toString().isNotEmpty()
        )
    }

    private fun saveSession(user: User){
        //save data using preferences
        pref.put(Utils.PREF_IS_LOGIN, 1)
        pref.put(Utils.PREF_NAME, user.name)
        pref.put(Utils.PREF_USERNAME, user.username)
        pref.put(Utils.PREF_PASSWORD, user.password)
        pref.put(Utils.PREF_DATE, Utils.timestampToString(user.created)!!)

        if (pref.getInt(Utils.PREF_AVATAR) == 0){
            pref.put(Utils.PREF_AVATAR, R.drawable.avatar1)
        }
    }




}











