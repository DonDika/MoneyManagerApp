package com.dondika.moneymanagerapp.ui.transaction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dondika.moneymanagerapp.R
import com.dondika.moneymanagerapp.data.model.Transaction
import com.dondika.moneymanagerapp.databinding.AdapterTransactionBinding
import kotlin.collections.ArrayList

class TransactionAdapter(
    var transactions: ArrayList<Transaction>,
    var listener: AdapterListener?
) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: AdapterTransactionBinding): RecyclerView.ViewHolder(binding.root) {

    }

    interface AdapterListener {
        fun onClick(transaction: Transaction)
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
            textAmount.text = transaction.amount.toString()
            textDate.text = transaction.created.toString()

            container.setOnClickListener {
                listener?.onClick(transaction)
            }
        }

    }

    override fun getItemCount(): Int {
        return transactions.size
    }


}