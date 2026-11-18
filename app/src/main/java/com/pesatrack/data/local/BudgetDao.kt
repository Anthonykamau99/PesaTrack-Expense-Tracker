package com.pesatrack.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(budget: Budget)

    @Query("SELECT * FROM budget WHERE id = 1 LIMIT 1")
    fun getBudget(): LiveData<Budget?>
}
