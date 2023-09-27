package com.fridayhouse.universities.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.fridayhouse.universities.AppData
import com.fridayhouse.universities.R
import com.fridayhouse.universities.api.ApiInterface
import com.fridayhouse.universities.repository.UniversityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UniversityRefreshService : Service() {

    private val notificationId = 1
    private val channelId = "university_refresh_channel"

    private lateinit var apiInterface: ApiInterface // Declare apiInterface
    private lateinit var universityRepository: UniversityRepository // Declare universityRepository

    override fun onCreate() {
        super.onCreate()

        apiInterface = AppData.apiInterface
        universityRepository = AppData.universityRepository

        startForegroundService()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun startForegroundService() {
        createNotificationChannel()

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("University Data Refresh")
            .setContentText("Refreshing data every 10 seconds")
            .setSmallIcon(R.drawable.ic_notification)
            .build()

        startForeground(notificationId, notification)

        // Start a coroutine to periodically fetch data
        GlobalScope.launch(Dispatchers.IO) {
            while (true) {
                refreshDataFromApi()
                delay(10000) // 10 seconds delay
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "University Refresh Service",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private suspend fun refreshDataFromApi() {
        try {
            val response = apiInterface.getUniversities()
            if (response.isSuccessful) {
                val universities = response.body()
                if (universities != null) {
                    // Update your data source or ViewModel with the new data
                    // For example, if you're using LiveData:
                    universityRepository.universityLiveData.postValue(universities)
                }
            } else {
                // Handle the case where the API request was not successful
                // You can log an error or perform error handling here
                showToast("Unknown error occurred")
            }
        } catch (e: Exception) {
            // Handle exceptions that may occur during the API request
            e.printStackTrace()
            showToast("An error occurred: ${e.message}")
        }
    }

    private fun showToast(message: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}
