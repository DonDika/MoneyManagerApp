package com.dondika.moneymanagerapp.ui.create

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.dondika.moneymanagerapp.R
import com.dondika.moneymanagerapp.data.model.Category
import com.dondika.moneymanagerapp.data.model.Transaction
import com.dondika.moneymanagerapp.ui.BaseActivity
import com.dondika.moneymanagerapp.databinding.ActivityCreateBinding
import com.google.android.material.button.MaterialButton
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UpdateActivity : BaseActivity() {

    private val binding by lazy { ActivityCreateBinding.inflate(layoutInflater) }
    private val firestore by lazy { Firebase.firestore }
    private val transactionId by lazy { intent.getStringExtra("transactionId") }

    private lateinit var transaction: Transaction
    private lateinit var categoryAdapter: CategoryAdapter

    private var chooseCategory: String = ""
    private var type: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupListener()
        setupAdapter()
    }

    override fun onStart() {
        super.onStart()

        detailTransaction()
    }




    private fun setupListener() {

        binding.buttonSave.apply {
            text = "Simpan Perubahan"
            setOnClickListener {
                progress(true)

                transaction.amount = binding.editAmount.text.toString().toInt()
                transaction.note = binding.editNote.text.toString()

                //edit data firestore
                firestore.collection("transaction")
                    .document(transactionId!!)
                    .set(transaction)
                    .addOnSuccessListener {
                        progress(false)
                        Toast.makeText(applicationContext, "Transaksi berhasil diupdate", Toast.LENGTH_SHORT).show()
                        finish()
                    }
            }
        }

        binding.buttonIn.setOnClickListener {
            transaction.type = "IN"
            setButton(it as MaterialButton)
        }
        binding.buttonOut.setOnClickListener {
            transaction.type = "OUT"
            setButton(it as MaterialButton)
        }

    }

    private fun setButton(buttonSelected: MaterialButton){
        listOf<MaterialButton>(binding.buttonIn, binding.buttonOut).forEach {
            it.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray))
        }
        buttonSelected.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_700))
    }

    private fun setupAdapter() {
        categoryAdapter = CategoryAdapter(this, arrayListOf(), object: CategoryAdapter.AdapterListener{
            override fun onClick(category: Category) {
                transaction.category = category.name!!
            }
        })
        binding.listCategory.adapter = categoryAdapter
    }


    private fun detailTransaction() {
        firestore.collection("transaction")
            .document(transactionId!!)
            .get()
            .addOnSuccessListener { result ->
                transaction = Transaction(
                    result.id,
                    result["username"].toString(),
                    result["category"].toString(),
                    result["type"].toString(),
                    result["amount"].toString().toInt(),
                    result["note"].toString(),
                    result["created"] as Timestamp
                )
                binding.editAmount.setText(transaction.amount.toString())
                binding.editNote.setText(transaction.note)

                when(transaction.type){
                    "IN" -> setButton(binding.buttonIn)
                    "OUT" -> setButton(binding.buttonOut)
                }
                getCategory()
            }
    }

    private fun getCategory() {
        val categories: ArrayList<Category> = arrayListOf()
        firestore.collection("category")
            .get()
            .addOnSuccessListener { result ->
                result.forEach { document ->
                    categories.add( Category(document.data["name"].toString()) )
                }
                categoryAdapter.setData(categories)
                Handler(Looper.myLooper()!!).postDelayed({
                    categoryAdapter.setButton(transaction.category)
                }, 200)
            }
    }


    private fun progress(isLoading: Boolean){
        when(isLoading){
            true -> {
                binding.buttonSave.text = "Loading"
                binding.buttonSave.isEnabled = false
            }
            false -> {
                binding.buttonSave.text = "Simpan Perubahan"
                binding.buttonSave.isEnabled = true
            }
        }
    }

}