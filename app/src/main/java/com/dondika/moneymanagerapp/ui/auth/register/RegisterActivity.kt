package com.dondika.moneymanagerapp.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.dondika.moneymanagerapp.data.model.User
import com.dondika.moneymanagerapp.ui.BaseActivity
import com.dondika.moneymanagerapp.databinding.ActivityRegisterBinding
import com.dondika.moneymanagerapp.ui.auth.login.LoginActivity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : BaseActivity() {

    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    private val firestore by lazy { Firebase.firestore }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupListener()
    }


    private fun setupListener() {
        binding.textLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        binding.buttonRegister.setOnClickListener {
            if (isRequired()){
                checkUsername()
            } else{
                Toast.makeText(this, "Isi data dengan lengkap", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isRequired(): Boolean{
        return (
            binding.editName.text.toString().isNotEmpty() &&
            binding.editUsername.text.toString().isNotEmpty() &&
            binding.editPassword.text.toString().isNotEmpty()
        )
    }

    private fun progress(isProgress: Boolean){
        binding.textAlert.visibility = View.GONE
        when(isProgress){
            true -> {
                binding.buttonRegister.text = "Loading"
                binding.buttonRegister.isEnabled = false
            }
            false -> {
                binding.buttonRegister.text = "Register"
                binding.buttonRegister.isEnabled = true
            }
        }
    }

    private fun checkUsername(){
        progress(true)
        firestore.collection("user")
            // check username if already exist
            .whereEqualTo("username", binding.editUsername.text.toString())
            .get()
            .addOnSuccessListener { result ->
                progress(false)
                if (result.isEmpty){
                    addUser()
                } else {
                    binding.textAlert.visibility = View.VISIBLE
                }
            }
    }

    private fun addUser(){
        progress(true)
        val userData = User(
            binding.editName.text.toString(),
            binding.editUsername.text.toString(),
            binding.editPassword.text.toString(),
            Timestamp.now()
        )
        //add data to firestore
        firestore.collection("user")
            .add(userData)
            .addOnSuccessListener {
                progress(false)
                Toast.makeText(this, "User berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
    }




}












