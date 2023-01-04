package com.dondika.moneymanagerapp.ui.transaction

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.dondika.moneymanagerapp.data.local.PreferenceManager
import com.dondika.moneymanagerapp.data.model.Transaction
import com.dondika.moneymanagerapp.ui.BaseActivity
import com.dondika.moneymanagerapp.databinding.ActivityTransactionBinding
import com.dondika.moneymanagerapp.ui.create.UpdateActivity
import com.dondika.moneymanagerapp.utils.Utils
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TransactionActivity : BaseActivity() {

    private val binding by lazy { ActivityTransactionBinding.inflate(layoutInflater) }
    private lateinit var transactionAdapter: TransactionAdapter


    private val firestore by lazy { Firebase.firestore }
    private val pref by lazy { PreferenceManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupListener()
        setupAdapter()
    }

    override fun onStart() {
        super.onStart()
        getTransaction()
    }

    private fun setupListener() {
        binding.swipe.setOnRefreshListener {
            binding.textTransaction.text = "Menampilkan 50 transaksi terakhir"
            getTransaction()
        }
        binding.imageDate.setOnClickListener {
            DateFragment(object : DateFragment.DateListener {
                override fun onSuccess(dateStart: String, dateEnd: String) {
                    //Log.e("DATE", "$dateStart and $dateEnd " )
                    //Log.e("Date", Utils.stringToTimestamp("$dateStart 00:00").toString())

                    binding.textTransaction.text = "Menampilkan transaksi dari $dateStart - $dateEnd"
                    firestore.collection("transaction")
                        .orderBy("created", Query.Direction.DESCENDING)
                        //ambil semua data di transaction yang sesuai username
                        .whereEqualTo("username", pref.getString(Utils.PREF_USERNAME))
                        .whereGreaterThanOrEqualTo("created", Utils.stringToTimestamp("$dateStart 00:00")!!)
                        .whereLessThanOrEqualTo("created", Utils.stringToTimestamp("$dateEnd 23:59")!!)
                        .get()
                        .addOnSuccessListener { result ->
                            binding.swipe.isRefreshing = false
                            setTransaction(result)
                        }

                }
            }).apply {
                show(supportFragmentManager, "date fragment")
            }
        }
    }

    private fun setupAdapter() {
        transactionAdapter = TransactionAdapter(arrayListOf(), object : TransactionAdapter.AdapterListener{
            override fun onClick(transaction: Transaction) {
                val intent = Intent(this@TransactionActivity, UpdateActivity::class.java)
                intent.putExtra("transactionId", transaction.id)
                startActivity(intent)
            }
            override fun onLongClick(transaction: Transaction) {
                val alertDialog = AlertDialog.Builder(this@TransactionActivity)
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


    private fun setTransaction(result: QuerySnapshot){
        val transactions: ArrayList<Transaction> = arrayListOf()
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

    private fun getTransaction() {
        binding.swipe.isRefreshing = true
        //get transaction data from firestore
        firestore.collection("transaction")
            .orderBy("created", Query.Direction.DESCENDING)
            //ambil semua data di transaction yang sesuai username
            .whereEqualTo("username", pref.getString(Utils.PREF_USERNAME))
            .limit(50)
            .get()
            .addOnSuccessListener { result ->
                binding.swipe.isRefreshing = false
                setTransaction(result)
            }
    }

    private fun deleteTransaction(id: String) {
        firestore.collection("transaction")
            .document(id)
            .delete()
            .addOnSuccessListener {
                getTransaction()
            }
    }


}