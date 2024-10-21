package com.tcc.poc.feature.ui.fragments.home

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.tcc.poc.R
import com.tcc.poc.domain.models.Transaction
import com.tcc.poc.domain.models.TransactionResponse
import com.tcc.poc.feature.ui.fragments.util.formatDate

class TransactionAdapter(private val transactionList: List<TransactionResponse>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val transactionNumber: TextView = itemView.findViewById(R.id.tv_transacrion_id)
        val amount: TextView = itemView.findViewById(R.id.tv_amount)
        val date: TextView = itemView.findViewById(R.id.tv_date)
        val vendor: TextView = itemView.findViewById(R.id.transaction_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactionList[position]
        holder.transactionNumber.text = transaction.transactionNumber
        holder.amount.text =String.format("%.2f", transaction.amount!!.toDouble())
        holder.date.text = transaction.createdDate
        holder.vendor.text = transaction.business.name
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }
}
