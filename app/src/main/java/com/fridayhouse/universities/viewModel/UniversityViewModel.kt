package com.fridayhouse.universities.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fridayhouse.universities.model.University
import com.fridayhouse.universities.repository.UniversityRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class UniversityViewModel(private val universityRepository: UniversityRepository): ViewModel() {

    // LiveData for the university data
    val university: LiveData<University>
        get() = universityRepository.university

    // LiveData for the loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading


    // Initialize a CoroutineExceptionHandler to handle exceptions
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        // Handle the exception as needed, e.g., log it
        throwable.printStackTrace()
        // Set isLoading to false since the request has completed (with or without error)
        _isLoading.postValue(false)
    }

    init {
        // Initialize isLoading to false
        _isLoading.value = false
        // Fetch universities data when the ViewModel is initialized
        fetchUniversities()
    }

    private fun fetchUniversities() {
        // Set isLoading to true when data fetching starts
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            // Fetch universities data from the repository
            universityRepository.getUniversities()
            // Set isLoading to false when data fetching is complete
            _isLoading.postValue(false)
        }
    }
}