package com.pesatrack.ui.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.pesatrack.data.local.AppDatabase
import com.pesatrack.data.repository.ExpenseRepository
import com.pesatrack.databinding.ActivityDashboardBinding
import com.pesatrack.ui.budget.BudgetActivity
import com.pesatrack.ui.expense.AddEditExpenseActivity
import com.pesatrack.ui.expense.ExpenseHistoryActivity
import com.pesatrack.util.ReminderScheduler
import java.text.NumberFormat
import java.util.Locale

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var viewModel: DashboardViewModel
    private var isDarkMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load saved theme preference before setting content view
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        isDarkMode = prefs.getBoolean("dark_mode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = AppDatabase.getDatabase(this)
        val repository = ExpenseRepository(database.expenseDao(), database.budgetDao())
        val factory = DashboardViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[DashboardViewModel::class.java]

        // Schedule the daily budget reminder
        ReminderScheduler.scheduleDailyReminder(this)

        setupThemeToggle()
        setupObservers()
        setupClickListeners()
    }

    private fun setupThemeToggle() {
        binding.btnThemeToggle.setOnClickListener {
            isDarkMode = !isDarkMode

            getSharedPreferences("settings", MODE_PRIVATE)
                .edit()
                .putBoolean("dark_mode", isDarkMode)
                .apply()

            AppCompatDelegate.setDefaultNightMode(
                if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }

    private fun setupObservers() {
        val formatter = NumberFormat.getCurrencyInstance(Locale("en", "KE"))
        formatter.currency = java.util.Currency.getInstance("KES")

        viewModel.allExpenses.observe(this) { expenses ->
            val totalSpent = viewModel.getTotalSpent(expenses)
            binding.tvTotalSpent.text = "Spent: ${formatter.format(totalSpent)}"

            // Recent transactions (show up to 3)
            val recent = expenses.take(3)
            if (recent.isEmpty()) {
                binding.tvRecent1.text = "No expenses yet"
                binding.tvRecent2.text = ""
                binding.tvRecent3.text = ""
            } else {
                binding.tvRecent1.text = recent.getOrNull(0)?.let {
                    "${it.title}  •  ${formatter.format(it.amount)}"
                } ?: ""
                binding.tvRecent2.text = recent.getOrNull(1)?.let {
                    "${it.title}  •  ${formatter.format(it.amount)}"
                } ?: ""
                binding.tvRecent3.text = recent.getOrNull(2)?.let {
                    "${it.title}  •  ${formatter.format(it.amount)}"
                } ?: ""
            }

            // Category summary
            val categoryTotals = viewModel.getCategoryTotals(expenses)
            val summary = if (categoryTotals.isEmpty()) {
                "No spending yet"
            } else {
                categoryTotals.entries
                    .sortedByDescending { it.value }
                    .take(4)
                    .joinToString("\n") { "• ${it.key}: ${formatter.format(it.value)}" }
            }
            binding.tvCategorySummary.text = summary

            updateRemaining(totalSpent)
        }

        viewModel.budget.observe(this) { budget ->
            val amount = budget?.amount ?: 0.0
            binding.tvBudget.text = formatter.format(amount)
            val totalSpent = viewModel.allExpenses.value?.let { viewModel.getTotalSpent(it) } ?: 0.0
            updateRemaining(totalSpent)
        }
    }

    private fun updateRemaining(totalSpent: Double) {
        val formatter = NumberFormat.getCurrencyInstance(Locale("en", "KE"))
        formatter.currency = java.util.Currency.getInstance("KES")

        val budgetAmount = viewModel.budget.value?.amount ?: 0.0
        val remaining = budgetAmount - totalSpent
        binding.tvRemaining.text = "Remaining: ${formatter.format(remaining)}"

        val percent = if (budgetAmount > 0) {
            ((totalSpent / budgetAmount) * 100).toInt().coerceIn(0, 100)
        } else {
            0
        }
        binding.tvPercentUsed.text = "$percent% of budget used"
        binding.progressBudget.progress = percent
    }

    private fun setupClickListeners() {
        binding.btnAddExpense.setOnClickListener {
            startActivity(Intent(this, AddEditExpenseActivity::class.java))
        }

        binding.btnViewHistory.setOnClickListener {
            startActivity(Intent(this, ExpenseHistoryActivity::class.java))
        }

        binding.btnBudget.setOnClickListener {
            startActivity(Intent(this, BudgetActivity::class.java))
        }
    }
}
