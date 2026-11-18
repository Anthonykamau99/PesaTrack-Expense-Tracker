package com.pesatrack.ui.expense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pesatrack.data.local.Expense
import com.pesatrack.data.repository.ExpenseRepository
import kotlinx.coroutines.launch

class ExpenseViewModel(private val repository: ExpenseRepository) : ViewModel() {

    val allExpenses = repository.allExpenses

    fun insert(expense: Expense) = viewModelScope.launch {
        repository.insertExpense(expense)
    }

    fun update(expense: Expense) = viewModelScope.launch {
        repository.updateExpense(expense)
    }

    fun delete(expense: Expense) = viewModelScope.launch {
        repository.deleteExpense(expense)
    }

    suspend fun getExpenseById(id: Long): Expense? {
        return repository.getExpenseById(id)
    }
}

class ExpenseViewModelFactory(
    private val repository: ExpenseRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
