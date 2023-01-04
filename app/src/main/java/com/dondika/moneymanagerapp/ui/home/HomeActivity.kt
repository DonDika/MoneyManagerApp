package com.dondika.moneymanagerapp.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.dondika.moneymanagerapp.data.local.PreferenceManager
import com.dondika.moneymanagerapp.data.model.Transaction
import com.dondika.moneymanagerapp.ui.BaseActivity
import com.dondika.moneymanagerapp.ui.create.CreateActivity
import com.dondika.moneymanagerapp.databinding.ActivityHomeBinding
import com.dondika.moneymanagerapp.databinding.HomeAvatarBinding
import com.dondika.moneymanagerapp.databinding.HomeDashboardBinding
import com.dondika.moneymanagerapp.ui.create.UpdateActivity
import com.dondika.moneymanagerapp.ui.profile.ProfileActivity
import com.dondika.moneymanagerapp.ui.transaction.TransactionActivity
import com.dondika.moneymanagerapp.ui.transaction.TransactionAdapter
import com.dondika.moneymanagerapp.utils.Utils
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
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
        setupAdapter()
    }

    override fun onStart() {
        super.onStart()

        getAvatar()
        getBalance()
        getTransaction()
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

    private fun setupAdapter() {
        transactionAdapter = TransactionAdapter(arrayListOf(), object : TransactionAdapter.AdapterListener{
            override fun onClick(transaction: Transaction) {
                val intent = Intent(this@HomeActivity, UpdateActivity::class.java)
                intent.putExtra("transactionId", transaction.id)
                startActivity(intent)
            }
            override fun onLongClick(transaction: Transaction) {
                val alertDialog = AlertDialog.Builder(this@HomeActivity)
                alertDialog.apply {
                    setTitle("Hapus")
                    setMessage("Hapus ${transaction.note} dari history transaksi?")
                    setNegativeButton("Batal"){ dialogInterface, _->
                        dialogInterface.dismiss()
                    }
                    setPositiveButton("Hapus"){ dialogInterface, _->
                        deleteTransaction(transaction.id!!)
                        dialogInterface.dismiss()
                    }
                }
                alertDialog.show()
            }
        })

        binding.listTransaction.adapter = transactionAdapter
    }


    private fun getBalance() {
        var totalBalance = 0
        var totalIn = 0
        var totalOut = 0
        //get balance data from firestore
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
                    totalBalance = totalIn - totalOut
                }
                bindingDashboard.textBalance.text = Utils.amountFormat(totalBalance)
                bindingDashboard.textIncome.text = Utils.amountFormat(totalIn)
                bindingDashboard.textOutcome.text = Utils.amountFormat(totalOut)
            }
    }

    private fun getTransaction() {
        progress(true)
        val transactions: ArrayList<Transaction> = arrayListOf()
        //get transaction data from firestore
        firestore.collection("transaction")
            .orderBy("created", Query.Direction.DESCENDING)
            //ambil semua data di transaction yang sesuai username
            .whereEqualTo("username", pref.getString(Utils.PREF_USERNAME))
            .limit(5)
            .get()
            .addOnSuccessListener { result ->
                progress(false)
                result.forEach { doc ->
                    val transactionData = Transaction(
                        //id document
                        doc.reference.id,
                        doc.data["username"].toString(),
                        doc.data["category"].toString(),
                        doc.data["type"].toString(),
                        doc.data["amount"].toString().toInt(),
                        doc.data["note"].toString(),
                        doc.data["created"] as Timestamp?
                    )
                    transactions.add(transactionData)
                }
                //tampilkan di recycler view
                transactionAdapter.setData(transactions)
            }
    }

    private fun getAvatar() {
        bindingAvatar.textAvatar.text = pref.getString(Utils.PREF_NAME)
        bindingAvatar.imageAvatar.setImageResource(pref.getInt(Utils.PREF_AVATAR)!!)
    }

    private fun progress(isLoading: Boolean){
        if (isLoading){
            binding.progress.visibility = View.VISIBLE
            binding.listTransaction.visibility = View.INVISIBLE
        } else {
            binding.progress.visibility = View.GONE
            binding.listTransaction.visibility = View.VISIBLE
        }
    }


    private fun deleteTransaction(id: String) {
        firestore.collection("transaction")
            .document(id)
            .delete()
            .addOnSuccessListener {
                getTransaction()
                getBalance()
            }
    }


}