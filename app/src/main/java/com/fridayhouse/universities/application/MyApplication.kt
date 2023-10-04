package com.fridayhouse.universities.application

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.fridayhouse.universities.AppData
import com.fridayhouse.universities.api.ApiInterface
import com.fridayhouse.universities.api.ApiUtilities
import com.fridayhouse.universities.repository.UniversityRepository
import com.fridayhouse.universities.worker.ServiceCheckerWorker
import java.util.concurrent.TimeUnit

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize apiInterface and universityRepository
        AppData.apiInterface = ApiUtilities.getInstance().create(ApiInterface::class.java)
        AppData.universityRepository = UniversityRepository(AppData.apiInterface)

        // Define constraints and create the periodic work request
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val serviceCheckerWorkRequest = PeriodicWorkRequest.Builder(
            ServiceCheckerWorker::class.java,
            15, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()

        // Enqueue the work request
        val workManager = WorkManager.getInstance(this)
        workManager.enqueueUniquePeriodicWork(
            "ServiceCheckerWorker",
            ExistingPeriodicWorkPolicy.KEEP, // ExistingWorkPolicy
            serviceCheckerWorkRequest
        )
    }
}
