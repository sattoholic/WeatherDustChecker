package com.sattoholic.weatherdustchecker.models

data class Dust(
    val pm10: String,
    val pm25: String,
    val pm10Status: String,
    val pm25Status: String,
    val co2: String,
    val o3: String,
    val no2: String
)