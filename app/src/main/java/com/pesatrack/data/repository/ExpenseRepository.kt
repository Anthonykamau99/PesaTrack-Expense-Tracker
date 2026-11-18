package com.pesatrack.data.repository

import androidx.lifecycle.LiveData
import com.pesatrack.data.local.Budget
import com.pesatrack.data.local.BudgetDao
import com.pesatrack.data.local.Expense
import com.pesatrack.data.local.ExpenseDao

class ExpenseRepository(
    private val expenseDao: ExpenseDao,
    private val budgetDao: BudgetDao
) {
    val allExpenses: LiveData<List<Expense>> = expenseDao.getAllExpenses()
    val budget: LiveData<Budget?> = budgetDao.getBudget()

    suspend fun insertExpense(expense: Expense) {
        expenseDao.insert(expense)
    }

    suspend fun updateExpense(expense: Expense) {
        expenseDao.update(expense)
    }

    suspend fun deleteExpense(expense: Expense) {
        expenseDao.delete(expense)
    }

    suspend fun getExpenseById(id: Long): Expense? {
        return expenseDao.getExpenseById(id)
    }

    suspend fun saveBudget(amount: Double) {
        budgetDao.insertOrUpdate(Budget(amount = amount))
    }
}
