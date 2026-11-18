package com.pesatrack.ui.expense

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pesatrack.data.local.Expense
import com.pesatrack.databinding.ItemExpenseBinding
import java.text.NumberFormat
import java.util.Locale

class ExpenseAdapter(
    private val onEditClick: (Expense) -> Unit,
    private val onDeleteClick: (Expense) -> Unit
) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    private var expenses = listOf<Expense>()

    fun submitList(list: List<Expense>) {
        expenses = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = ItemExpenseBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.bind(expenses[position])
    }

    override fun getItemCount(): Int = expenses.size

    inner class ExpenseViewHolder(
        private val binding: ItemExpenseBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(expense: Expense) {
            val formatter = NumberFormat.getCurrencyInstance(Locale("en", "KE"))
            formatter.currency = java.util.Currency.getInstance("KES")

            binding.tvTitle.text = expense.title
            binding.tvCategory.text = expense.category
            binding.tvDate.text = expense.date
            binding.tvAmount.text = formatter.format(expense.amount)

            binding.btnEdit.setOnClickListener { onEditClick(expense) }
            binding.btnDelete.setOnClickListener { onDeleteClick(expense) }
        }
    }
}
