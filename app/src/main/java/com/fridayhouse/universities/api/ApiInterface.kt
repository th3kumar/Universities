package com.fridayhouse.universities.api

import com.fridayhouse.universities.model.University
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {

    @GET("search?country=India")
    suspend fun getUniversities() : Response<University>
}