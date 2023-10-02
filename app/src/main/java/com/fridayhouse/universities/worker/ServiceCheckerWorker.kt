package com.fridayhouse.universities.worker

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.fridayhouse.universities.services.UniversityRefreshService

class ServiceCheckerWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

        override fun doWork(): Result {
            try {
                val isServiceRunning = isForegroundServiceRunning(applicationContext)

                if (!isServiceRunning) {
                    startForegroundService()
                }

                return Result.success()
            } catch (e: Exception) {
                // Handle the exception, log it, or show an error message.
                Log.e("ServiceCheckerClass", "Error in doWork(): ${e.message}", e)
                return Result.failure()
            }

    }

    private fun isForegroundServiceRunning(context: Context): Boolean {
        val serviceClassName = "UniversityRefreshService" // Replace with your service class name
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
