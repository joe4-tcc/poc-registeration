package com.tcc.poc.feature.ui.fragments.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tcc.poc.R
import com.tcc.poc.domain.models.Transaction

class TransactionAdapter(private val transactionList: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val transactionNumber: TextView = itemView.findViewById(R.id.tv_transaction_id)
        val amount: TextView = itemView.findViewById(R.id.tv_amount)
        val date: TextView = itemView.findViewById(R.id.tv_date)
        val vendor: TextView = itemView.findViewById(R.id.transaction_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactionList[position]
        holder.transactionNumber.text = transaction.transactionNumber
        holder.amount.text = transaction.amount.toString()
        holder.date.text = transaction.date.toString()
        holder.vendor.text = transaction.vendor
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }
}
