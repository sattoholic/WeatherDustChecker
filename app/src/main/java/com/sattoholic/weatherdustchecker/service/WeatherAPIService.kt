package com.sattoholic.weatherdustchecker.service

import com.sattoholic.weatherdustchecker.models.Weather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPIService {
    @GET("/data/2.5/weather")
    fun getWeatherStatusInfo(
        @Query("appid") appid: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric"
    ): Call<Weather>
}