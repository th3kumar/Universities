package com.fridayhouse.universities.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fridayhouse.universities.model.University
import com.fridayhouse.universities.repository.UniversityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class UniversityViewModel(private val universityRepository: UniversityRepository): ViewModel() {


    init {
        viewModelScope.launch(Dispatchers.IO){
            universityRepository.getUniversities()
        }
    }


    val university: LiveData<University>
        get() = universityRepository.university
}