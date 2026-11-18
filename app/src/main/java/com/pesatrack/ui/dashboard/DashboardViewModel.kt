package com.pesatrack.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pesatrack.data.local.Expense
import com.pesatrack.data.repository.ExpenseRepository

class DashboardViewModel(private val repository: ExpenseRepository) : ViewModel() {

    val allExpenses = repository.allExpenses
    val budget = repository.budget

    fun getTotalSpent(expenses: List<Expense>): Double {
        return expenses.sumOf { it.amount }
    }

    fun getCategoryTotals(expenses: List<Expense>): Map<String, Double> {
        return expenses.groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
    }
}

class DashboardViewModelFactory(
    private val repository: ExpenseRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
