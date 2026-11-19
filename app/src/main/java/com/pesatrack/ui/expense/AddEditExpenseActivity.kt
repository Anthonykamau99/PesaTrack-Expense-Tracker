package com.pesatrack.ui.expense

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.pesatrack.data.local.AppDatabase
import com.pesatrack.data.local.Expense
import com.pesatrack.data.repository.ExpenseRepository
import com.pesatrack.databinding.ActivityAddEditExpenseBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddEditExpenseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditExpenseBinding
    private lateinit var viewModel: ExpenseViewModel
    private var expenseId: Long = -1L

    private val categories = listOf(
        "Food", "Transport", "Entertainment",
        "Shopping", "Airtime and Data", "Other"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = AppDatabase.getDatabase(this)
        val repository = ExpenseRepository(database.expenseDao(), database.budgetDao())
        val factory = ExpenseViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ExpenseViewModel::class.java]

        // Spinner for categories
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = adapter

        // Check if we are editing
        expenseId = intent.getLongExtra("EXPENSE_ID", -1L)
        if (expenseId != -1L) {
            binding.tvTitle.text = "Edit Expense"
            binding.btnSave.text = "Update Expense"
            loadExpense(expenseId)
        } else {
            // Default date = today
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            binding.etDate.setText(today)
        }

        binding.btnSave.setOnClickListener { saveExpense() }
        binding.btnCancel.setOnClickListener { finish() }
    }

    private fun loadExpense(id: Long) {
        lifecycleScope.launch {
            val expense = viewModel.getExpenseById(id)
            expense?.let {
                binding.etTitle.setText(it.title)
                binding.etAmount.setText(it.amount.toString())
                binding.etDate.setText(it.date)
                val position = categories.indexOf(it.category)
                if (position >= 0) binding.spinnerCategory.setSelection(position)
            }
        }
    }

    private fun saveExpense() {
        val title = binding.etTitle.text.toString().trim()
        val amountText = binding.etAmount.text.toString().trim()
        val date = binding.etDate.text.toString().trim()
        val category = binding.spinnerCategory.selectedItem.toString()

        // Validation
        when {
            title.isEmpty() -> {
                binding.etTitle.error = "Title is required"
                return
            }
            amountText.isEmpty() -> {
                binding.etAmount.error = "Amount is required"
                return
            }
            amountText.toDoubleOrNull() == null || amountText.toDouble() <= 0 -> {
                binding.etAmount.error = "Enter a valid positive amount"
                return
            }
            date.isEmpty() -> {
                binding.etDate.error = "Date is required"
                return
            }
        }

        val amount = amountText.toDouble()

        val expense = Expense(
            id = if (expenseId != -1L) expenseId else 0,
            title = title,
            amount = amount,
            category = category,
            date = date
        )

        if (expenseId != -1L) {
            viewModel.update(expense)
            Toast.makeText(this, "Expense updated", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.insert(expense)
            Toast.makeText(this, "Expense saved", Toast.LENGTH_SHORT).show()
        }
        finish()
    }
}
