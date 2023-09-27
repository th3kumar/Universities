package com.fridayhouse.universities.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fridayhouse.universities.api.ApiInterface
import com.fridayhouse.universities.model.University

class UniversityRepository(private val apiInterface: ApiInterface) {

    val universityLiveData = MutableLiveData<University>()

    val university : LiveData<University>
        get() = universityLiveData

    suspend fun getUniversities(){
        val result = apiInterface.getUniversities()
        if(result.body() != null){
            universityLiveData.postValue(result.body())
        }
    }
}