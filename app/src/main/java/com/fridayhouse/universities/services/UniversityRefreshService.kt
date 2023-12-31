package com.fridayhouse.universities.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.fridayhouse.universities.AppData
import com.fridayhouse.universities.R
import com.fridayhouse.universities.activities.MainActivity
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

    //only need for started services
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        // Initialize apiInterface and universityRepository
        apiInterface = AppData.apiInterface
        universityRepository = AppData.universityRepository

        if (isConnectedToInternet(this)) {

            val notification = createNotificationChannel()// Create the notification
            startForeground(notificationId, notification)// Start the service in the foreground immediately after starting it

            // Start a coroutine to periodically fetch data
            GlobalScope.launch(Dispatchers.IO) {
                while (true) {
                    refreshDataFromApi()
                    delay(10000) // 10 seconds delay
                }
            }
            showToast("Service started") // Display a toast message indicating that the service has started
            } else {
            showToast("Please check internet connectivity before starting the service")
             }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        //only need for bound services
        //bound services are services where other components can communicate with it by binding to it
        return null
    }

    private fun createNotificationChannel(): Notification {
        // Create an explicit intent for MainActivity
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        mainActivityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            mainActivityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "University Refresh Service",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("University Data Refresh")
            .setContentText("Refreshing data every 10 seconds")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent) // Set the PendingIntent to open MainActivity
            .setAutoCancel(true) // Auto-cancel the notification when clicked
            .build()
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
            // Handle exceptions that may occur during the API request mostly due to unavailability of internet connection
            e.printStackTrace()
            showToast("An error occurred due to Internet: ${e.message}")
        }
    }

    private fun isConnectedToInternet(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Check for network availability
        val network = connectivityManager.activeNetwork
        if (network != null) {
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        }

        return false
    }

    private fun showToast(message: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}
