package com.dondika.moneymanagerapp.ui.create

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.dondika.moneymanagerapp.R
import com.dondika.moneymanagerapp.data.local.PreferenceManager
import com.dondika.moneymanagerapp.data.model.Category
import com.dondika.moneymanagerapp.data.model.Transaction
import com.dondika.moneymanagerapp.ui.BaseActivity
import com.dondika.moneymanagerapp.databinding.ActivityCreateBinding
import com.dondika.moneymanagerapp.utils.Utils
import com.google.android.material.button.MaterialButton
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CreateActivity : BaseActivity() {

    private val binding by lazy { ActivityCreateBinding.inflate(layoutInflater) }
    private val firestore by lazy { Firebase.firestore }
    private val pref by lazy { PreferenceManager(this) }

    private lateinit var categoryAdapter: CategoryAdapter
    private var chooseCategory: String = ""
    private var type: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupAdapter()
        setupListener()
    }


    override fun onStart() {
        super.onStart()

        getCategory()
    }


    private fun setupListener() {
        binding.buttonIn.setOnClickListener {
            type = "IN"
            setButton(it as MaterialButton)
        }
        binding.buttonOut.setOnClickListener {
            type = "OUT"
            setButton(it as MaterialButton)
        }
        binding.buttonSave.setOnClickListener {
            progress(true)
            val addTransactionData = Transaction(
                id = null,
                username = pref.getString(Utils.PREF_USERNAME)!!,
                category = chooseCategory,
                type = type,
                amount = binding.editAmount.text.toString().toInt(),
                note = binding.editNote.text.toString(),
                created = Timestamp.now()
            )
            //add to firestore
            firestore.collection("transaction")
                .add(addTransactionData)
                .addOnSuccessListener {
                    progress(false)
                    Toast.makeText(this, "Transaksi berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    finish()
                }
        }
    }


    private fun progress(isLoading: Boolean){
        when(isLoading){
            true -> {
                binding.buttonSave.text = "Loading"
                binding.buttonSave.isEnabled = false
            }
            false -> {
                binding.buttonSave.text = "Simpan"
                binding.buttonSave.isEnabled = true
            }
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
                chooseCategory = category.name!!
                //Log.e("choosecategory", chooseCategory )
            }
        })
        binding.listCategory.adapter = categoryAdapter
    }


    private fun getCategory() {
        val categories: ArrayList<Category> = arrayListOf()
        firestore.collection("category")
            .get()
            .addOnSuccessListener { result ->
                result.forEach { document ->
                    //Log.e("getdata", document.data["name"].toString() )
                    categories.add( Category(document.data["name"].toString()) )
                }
                //Log.e("getdata", "categories = $categories" )
                categoryAdapter.setData(categories)
            }
    }



}