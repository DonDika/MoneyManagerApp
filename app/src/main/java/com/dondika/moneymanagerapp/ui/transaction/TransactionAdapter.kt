package com.dondika.moneymanagerapp.ui.transaction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dondika.moneymanagerapp.R
import com.dondika.moneymanagerapp.data.model.Transaction
import com.dondika.moneymanagerapp.databinding.AdapterTransactionBinding
import com.dondika.moneymanagerapp.utils.Utils
import kotlin.collections.ArrayList

class TransactionAdapter(
    var transactions: ArrayList<Transaction>,
    var listener: AdapterListener?
) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    //add data
    fun setData(dataTransaction: List<Transaction>){
        transactions.clear()
        transactions.addAll(dataTransaction)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AdapterTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = transactions[position]

        holder.binding.apply {
            if (transaction.type.uppercase() == "IN") {
                holder.binding.imageType.setImageResource(R.drawable.ic_in)
            } else {
                holder.binding.imageType.setImageResource(R.drawable.ic_out)
            }

            textNote.text = transaction.note
            textCategory.text = transaction.category
            textAmount.text = Utils.amountFormat(transaction.amount)
            textDate.text = Utils.timestampToString(transaction.created)

            container.setOnClickListener {
                listener?.onClick(transaction)
            }
        }

    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    inner class ViewHolder(val binding: AdapterTransactionBinding): RecyclerView.ViewHolder(binding.root) {

    }

    interface AdapterListener {
        fun onClick(transaction: Transaction)
    }



}