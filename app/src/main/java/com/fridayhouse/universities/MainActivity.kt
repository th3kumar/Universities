package com.fridayhouse.universities

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.fridayhouse.universities.api.ApiInterface
import com.fridayhouse.universities.api.ApiUtilities
import com.fridayhouse.universities.repository.UniversityRepository
import com.fridayhouse.universities.viewModel.UniversityViewModel
import com.fridayhouse.universities.viewModel.UniversityViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var universityViewModel: UniversityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiInterface = ApiUtilities.getInstance().create(ApiInterface::class.java)

        val universityRepository = UniversityRepository(apiInterface)

        universityViewModel = ViewModelProvider(this, UniversityViewModelFactory(universityRepository)).get(UniversityViewModel::class.java)

        universityViewModel.university.observe(this) {
            it.iterator().forEach { universityItem ->
                Log.d(
                    "incommingData",
                    "name: ${universityItem.name}\n domain: ${universityItem.domains}\n webpage: ${universityItem.web_pages}\n country: ${universityItem.country}\n"
                )
            }

        }
    }
}