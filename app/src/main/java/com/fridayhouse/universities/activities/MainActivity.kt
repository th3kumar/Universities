package com.fridayhouse.universities.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.fridayhouse.universities.AppData
import com.fridayhouse.universities.R
import com.fridayhouse.universities.adapters.UniversityAdapter
import com.fridayhouse.universities.api.ApiInterface
import com.fridayhouse.universities.api.ApiUtilities
import com.fridayhouse.universities.repository.UniversityRepository
import com.fridayhouse.universities.services.UniversityRefreshService
import com.fridayhouse.universities.viewModel.UniversityViewModel
import com.fridayhouse.universities.viewModel.UniversityViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var universityViewModel: UniversityViewModel
    private lateinit var adapter: UniversityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiInterface = ApiUtilities.getInstance().create(ApiInterface::class.java)
        val universityRepository = UniversityRepository(apiInterface)
        val progressBarAnimation = findViewById<LottieAnimationView>(R.id.progressBarAnimation)
        val titleTextView = findViewById<TextView>(R.id.textView)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // Set the instances in the AppData singleton
        AppData.apiInterface = apiInterface
        AppData.universityRepository = universityRepository
        universityViewModel = ViewModelProvider(this, UniversityViewModelFactory(universityRepository)).get(UniversityViewModel::class.java)

       // Set the adapter for your RecyclerView
        adapter = UniversityAdapter()
        adapter.context = this
        recyclerView.adapter = adapter


        val serviceIntent = Intent(this, UniversityRefreshService::class.java)
        startService(serviceIntent)


        // Observe LiveData from ViewModel for loading state
        universityViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                showProgressBar(progressBarAnimation, titleTextView)
            } else {
                hideProgressBar(progressBarAnimation, titleTextView)
            }
        }

        // Observe LiveData from ViewModel
        universityViewModel.university.observe(this) { universities ->
            // Update your RecyclerView adapter with the list of universities
            if (universities != null) {
                adapter.differ.submitList(universities)
            }
        }
    }

    private fun hideProgressBar(
        progressBarAnimation: LottieAnimationView,
        titleTextView: TextView
    ) {
       // Hide the ProgressBar and the title
        progressBarAnimation.visibility = View.GONE
        titleTextView.visibility = View.VISIBLE
    }

    private fun showProgressBar(
        progressBarAnimation: LottieAnimationView,
        titleTextView: TextView
    ) {
        // Show the ProgressBar and the title
        progressBarAnimation.visibility = View.VISIBLE
        titleTextView.visibility = View.GONE
    }
}