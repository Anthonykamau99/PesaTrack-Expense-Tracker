package com.pesatrack.ui.expense

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pesatrack.data.local.AppDatabase
import com.pesatrack.data.local.Expense
import com.pesatrack.data.repository.ExpenseRepository
import com.pesatrack.databinding.ActivityExpenseHistoryBinding

class ExpenseHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExpenseHistoryBinding
    private lateinit var viewModel: ExpenseViewModel
    private lateinit var adapter: ExpenseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpenseHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = AppDatabase.getDatabase(this)
        val repository = ExpenseRepository(database.expenseDao(), database.budgetDao())
        val factory = ExpenseViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ExpenseViewModel::class.java]

        adapter = ExpenseAdapter(
            onEditClick = { expense ->
                val intent = Intent(this, AddEditExpenseActivity::class.java)
                intent.putExtra("EXPENSE_ID", expense.id)
                startActivity(intent)
            },
            onDeleteClick = { expense ->
                showDeleteConfirmation(expense)
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        viewModel.allExpenses.observe(this) { expenses ->
            if (expenses.isEmpty()) {
                binding.tvEmpty.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.tvEmpty.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                adapter.submitList(expenses)
            }
        }

        binding.btnAdd.setOnClickListener {
            startActivity(Intent(this, AddEditExpenseActivity::class.java))
        }
    }

    private fun showDeleteConfirmation(expense: Expense) {
        AlertDialog.Builder(this)
            .setTitle("Delete Expense")
            .setMessage("Are you sure you want to delete \"${expense.title}\"?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.delete(expense)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
