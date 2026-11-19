package com.pesatrack.util

import android.content.Context
import androidx.work.*
import com.pesatrack.worker.BudgetReminderWorker
import java.util.concurrent.TimeUnit

object ReminderScheduler {

    fun scheduleDailyReminder(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<BudgetReminderWorker>(
            1, TimeUnit.DAYS
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "budget_reminder",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}
