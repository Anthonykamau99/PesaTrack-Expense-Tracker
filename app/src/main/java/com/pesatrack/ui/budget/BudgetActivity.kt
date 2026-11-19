package com.pesatrack.ui.budget

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.pesatrack.data.local.AppDatabase
import com.pesatrack.data.repository.ExpenseRepository
import com.pesatrack.databinding.ActivityBudgetBinding
import java.text.NumberFormat
import java.util.Locale

class BudgetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBudgetBinding
    private lateinit var viewModel: BudgetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBudgetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = AppDatabase.getDatabase(this)
        val repository = ExpenseRepository(database.expenseDao(), database.budgetDao())
        val factory = BudgetViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[BudgetViewModel::class.java]

        val formatter = NumberFormat.getCurrencyInstance(Locale("en", "KE"))
        formatter.currency = java.util.Currency.getInstance("KES")

        viewModel.budget.observe(this) { budget ->
            val amount = budget?.amount ?: 0.0
            binding.tvCurrentBudget.text = "Current Budget: ${formatter.format(amount)}"
            if (amount > 0) {
                binding.etBudget.setText(amount.toString())
            }
        }

        binding.btnSaveBudget.setOnClickListener {
            val text = binding.etBudget.text.toString().trim()
            if (text.isEmpty() || text.toDoubleOrNull() == null || text.toDouble() <= 0) {
                binding.etBudget.error = "Enter a valid positive amount"
                return@setOnClickListener
            }
            viewModel.saveBudget(text.toDouble())
            Toast.makeText(this, "Budget saved", Toast.LENGTH_SHORT).show()
        }

        // Simple financial tips
        binding.tvTips.text = """
            • Create a monthly budget and stick to it.
            • Try to save at least 10-20% of your allowance.
            • Track every expense – small ones add up.
            • Separate needs (food, transport) from wants.
            • Review your spending every week.
        """.trimIndent()
    }
}
