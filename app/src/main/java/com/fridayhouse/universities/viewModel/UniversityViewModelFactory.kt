package com.fridayhouse.universities.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fridayhouse.universities.repository.UniversityRepository

class UniversityViewModelFactory(private val universityRepository: UniversityRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UniversityViewModel(universityRepository) as T
    }
}