package com.fridayhouse.universities.model

data class UniversityItem(
    val alpha_two_code: String,
    val country: String,
    val domains: List<String>,
    val name: String,
    val stateProvince: String,
    val web_pages: List<String>
)