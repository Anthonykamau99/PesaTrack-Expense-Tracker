package com.pesatrack.ui.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pesatrack.data.repository.ExpenseRepository
import kotlinx.coroutines.launch

class BudgetViewModel(private val repository: ExpenseRepository) : ViewModel() {

    val budget = repository.budget

    fun saveBudget(amount: Double) = viewModelScope.launch {
        repository.saveBudget(amount)
    }
}

class BudgetViewModelFactory(
    private val repository: ExpenseRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BudgetViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BudgetViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
