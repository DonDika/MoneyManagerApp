package com.dondika.moneymanagerapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.dondika.moneymanagerapp.data.local.PreferenceManager
import com.dondika.moneymanagerapp.data.model.Category
import com.dondika.moneymanagerapp.data.model.Transaction
import com.dondika.moneymanagerapp.ui.BaseActivity
import com.dondika.moneymanagerapp.ui.create.CreateActivity
import com.dondika.moneymanagerapp.databinding.ActivityHomeBinding
import com.dondika.moneymanagerapp.databinding.HomeAvatarBinding
import com.dondika.moneymanagerapp.databinding.HomeDashboardBinding
import com.dondika.moneymanagerapp.ui.profile.ProfileActivity
import com.dondika.moneymanagerapp.ui.transaction.TransactionActivity
import com.dondika.moneymanagerapp.ui.transaction.TransactionAdapter
import com.dondika.moneymanagerapp.utils.Utils
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeActivity : BaseActivity() {

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    private lateinit var bindingAvatar: HomeAvatarBinding
    private lateinit var bindingDashboard: HomeDashboardBinding
    private lateinit var transactionAdapter: TransactionAdapter

    private val firestore by lazy { Firebase.firestore }
    private val pref by lazy { PreferenceManager(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupListener()
        setupList()

        //testFirestore()
    }


    override fun onStart() {
        super.onStart()

        testFirestore()
        getAvatar()
        getBalance()
        getTransaction()
    }

    private fun getTransaction() {
        binding.progress.visibility = View.VISIBLE
        val transactions: ArrayList<Transaction> = arrayListOf()
        firestore.collection("transaction")
            .whereEqualTo("username", pref.getString(Utils.PREF_USERNAME))
            .get()
            .addOnSuccessListener { result ->
                binding.progress.visibility = View.GONE
                result.forEach { doc ->
                    val transactionData = Transaction(
                        //id document
                        doc.reference.id,
                        doc.data["username"].toString(),
                        doc.data["category"].toString(),
                        doc.data["type"].toString(),
                        doc.data["amount"].toString().toInt(),
                        doc.data["note"].toString(),
                        doc.data["created"] as Timestamp
                    )
                    transactions.add(transactionData)
                }
                transactionAdapter.setData(transactions)
            }
    }


    private fun setupList() {
        transactionAdapter = TransactionAdapter(arrayListOf(), null)
        binding.listTransaction.adapter = transactionAdapter
    }

    private fun getBalance() {
        var totalBalance = 0
        var totalIn = 0
        var totalOut = 0
        firestore.collection("transaction")
            .whereEqualTo("username", pref.getString(Utils.PREF_USERNAME))
            .get()
            .addOnSuccessListener { result ->
                result.forEach { doc ->
                    totalBalance += doc.data["amount"].toString().toInt()
                    when(doc.data["type"].toString()){
                        "IN" -> totalIn += doc.data["amount"].toString().toInt()
                        "OUT" -> totalOut += doc.data["amount"].toString().toInt()
                    }
                }
                bindingDashboard.textBalance.text = Utils.amountFormat(totalBalance)
                bindingDashboard.textIncome.text = Utils.amountFormat(totalIn)
                bindingDashboard.textOutcome.text = Utils.amountFormat(totalOut)
            }
    }

    private fun getAvatar() {
        bindingAvatar.textAvatar.text = pref.getString(Utils.PREF_NAME)
        bindingAvatar.imageAvatar.setImageResource(pref.getInt(Utils.PREF_AVATAR)!!)
    }

    private fun testFirestore() {
        val categories: ArrayList<Category> = arrayListOf()
        firestore.collection("category")
            .get()
            .addOnSuccessListener { result ->
                result.forEach { document ->
                    //Log.e("getdata", document.data["name"].toString() )
                    categories.add( Category(document.data["name"].toString()) )
                }
                Log.e("getdata", "categories = $categories" )
            }

    }

    private fun setupBinding() {
        setContentView(binding.root)
        bindingAvatar = binding.includeAvatar
        bindingDashboard = binding.includeDashboard
    }


    private fun setupListener() {
        bindingAvatar.imageAvatar.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("balance", bindingDashboard.textBalance.text.toString())
            startActivity(intent)
        }
        binding.fabCreate.setOnClickListener {
            startActivity(Intent(this, CreateActivity::class.java))
        }
        binding.textTransaction.setOnClickListener {
            startActivity(Intent(this, TransactionActivity::class.java))
        }
    }


}