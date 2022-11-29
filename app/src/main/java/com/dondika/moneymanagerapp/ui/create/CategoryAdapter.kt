package com.dondika.moneymanagerapp.ui.create

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dondika.moneymanagerapp.R
import com.dondika.moneymanagerapp.data.model.Category
import com.dondika.moneymanagerapp.databinding.AdapterCategoryBinding
import com.google.android.material.button.MaterialButton

class CategoryAdapter(
    val context: Context,
    var categories: ArrayList<Category>,
    var listener: AdapterListener?
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var listButton: ArrayList<MaterialButton> = arrayListOf()

    fun setData(dataCategory: List<Category>){
        categories.clear()
        categories.addAll(dataCategory)
        notifyDataSetChanged()
    }

    private fun setButton(buttonSelected: MaterialButton){
        listButton.forEach { button ->
            button.setBackgroundColor(ContextCompat.getColor(context, android.R.color.darker_gray))
        }
        buttonSelected.setBackgroundColor(ContextCompat.getColor(context, R.color.teal_700))
    }

    /*private fun setButton(category: String){
        listButton.forEach { button ->
            if (button.text.toString().contains(category)){
                button.setBackgroundColor(ContextCompat.getColor(context, R.color.teal_700))
            }
        }
    }*/


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AdapterCategoryBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        holder.binding.apply {
            buttonCategory.text = category.name
            buttonCategory.setOnClickListener {
                listener?.onClick(category)
                setButton(it as MaterialButton)
            }
        }
        listButton.add(holder.binding.buttonCategory)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    inner class ViewHolder(val binding: AdapterCategoryBinding): RecyclerView.ViewHolder(binding.root){

    }

    interface AdapterListener{
        fun onClick(category: Category)
    }


}