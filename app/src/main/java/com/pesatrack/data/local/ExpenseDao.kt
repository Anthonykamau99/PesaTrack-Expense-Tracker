package com.pesatrack.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ExpenseDao {

    @Insert
    suspend fun insert(expense: Expense)

    @Update
    suspend fun update(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)

    @Query("SELECT * FROM expenses ORDER BY id DESC")
    fun getAllExpenses(): LiveData<List<Expense>>

    @Query("SELECT * FROM expenses WHERE id = :id LIMIT 1")
    suspend fun getExpenseById(id: Long): Expense?
}
