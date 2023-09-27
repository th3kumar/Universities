package com.fridayhouse.universities

import com.fridayhouse.universities.api.ApiInterface
import com.fridayhouse.universities.repository.UniversityRepository

object AppData {
    lateinit var apiInterface: ApiInterface
    lateinit var universityRepository: UniversityRepository
}