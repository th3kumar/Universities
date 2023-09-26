package com.fridayhouse.universities.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.fridayhouse.universities.R
import com.fridayhouse.universities.adapters.UniversityAdapter
import com.fridayhouse.universities.api.ApiInterface
import com.fridayhouse.universities.api.ApiUtilities
import com.fridayhouse.universities.repository.UniversityRepository
import com.fridayhouse.universities.viewModel.UniversityViewModel
import com.fridayhouse.universities.viewModel.UniversityViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var universityViewModel: UniversityViewModel
    private lateinit var adapter: UniversityAdapter // Create an adapter for your RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiInterface = ApiUtilities.getInstance().create(ApiInterface::class.java)

        val universityRepository = UniversityRepository(apiInterface)

        universityViewModel = ViewModelProvider(this, UniversityViewModelFactory(universityRepository)).get(UniversityViewModel::class.java)

        // Initialize RecyclerView and its adapter
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = UniversityAdapter() // Create your custom adapter
        adapter.context = this // Set the context for the adapter
        recyclerView.adapter = adapter


        // Observe LiveData from ViewModel
        universityViewModel.university.observe(this, { universities ->
            Log.d("UniversityActivity", "LiveData changed: $universities")
            // Update your RecyclerView adapter with the list of universities
            if (universities != null) {
                adapter.differ.submitList(universities)
                Log.d("UniversityActivity", "Updating RecyclerView with ${universities.size} universities")
            }
        })
        // Trigger the data fetch operation
        Log.d("UniversityActivity", "Fetching universities data")

//        universityViewModel.university.observe(this) {
//            it.iterator().forEach { universityItem ->
//                Log.d(
//                    "incommingData",
//                    "name: ${universityItem.name}\n domain: ${universityItem.domains}\n webpage: ${universityItem.web_pages}\n country: ${universityItem.country}\n"
//                )
//            }
//
//        }


    }
}