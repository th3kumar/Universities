package com.fridayhouse.universities.worker

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.fridayhouse.universities.services.UniversityRefreshService

class ServiceCheckerWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val isServiceRunning = isForegroundServiceRunning(applicationContext)

        if (!isServiceRunning) {
            startForegroundService()
        }

        return Result.success()
    }

    private fun isForegroundServiceRunning(context: Context): Boolean {
        val serviceClassName = "com.fridayhouse.universities.services.UniversityRefreshService" // Replace with your service class name
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        val runningServices = activityManager.getRunningServices(Integer.MAX_VALUE)

        for (serviceInfo in runningServices) {
            if (serviceClassName == serviceInfo.service.className) {
                return true
            }
        }

        return false
    }

    private fun startForegroundService() {
        // Start your foreground service here
        val serviceIntent = Intent(applicationContext, UniversityRefreshService::class.java)
        ContextCompat.startForegroundService(applicationContext, serviceIntent)
    }
}
