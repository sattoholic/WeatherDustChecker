package com.sattoholic.weatherdustchecker.repository

import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.sattoholic.weatherdustchecker.models.Dust
import com.sattoholic.weatherdustchecker.models.NowLocation
import com.sattoholic.weatherdustchecker.models.Weather
import com.sattoholic.weatherdustchecker.service.DustAPIService
import com.sattoholic.weatherdustchecker.service.WeatherAPIService
import com.sattoholic.weatherdustchecker.utils.DustDeserializer
import com.sattoholic.weatherdustchecker.utils.WeatherDeserializer
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Repository(API_KEY: String, TOKEN: String) {
    private var WEATHER_APP_ID = ""
    private var DUST_APP_TOKEN = ""
    private val location = NowLocation.getInstance()
    private val _dataLoaded = MutableLiveData<Boolean>().apply { this.value = false }

    init {
        WEATHER_APP_ID = API_KEY
        DUST_APP_TOKEN = TOKEN
    }

    val dataLoaded: LiveData<Boolean>
        get() = _dataLoaded

    private val weatherRetrofit  = Retrofit.Builder()
        .baseUrl("http://api.openweathermap.org")
        .addConverterFactory(GsonConverterFactory.create(
            GsonBuilder().registerTypeAdapter(
                Weather::class.java,
                WeatherDeserializer()
            ).create()
        )
        ).build()

    private val weatherAPIService = weatherRetrofit.create(WeatherAPIService::class.java)

    private val dustRetrofit = Retrofit.Builder().baseUrl("http://api.waqi.info").addConverterFactory(GsonConverterFactory.create(
        GsonBuilder().registerTypeAdapter(
            Dust::class.java,
            DustDeserializer()
        ).create()
    )).build()

    private val dustAPIService = dustRetrofit.create(DustAPIService::class.java)

    @Suppress("MissingPermission")
    fun updateLocation(locationManager: LocationManager){
        _dataLoaded.value = false
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, object: LocationListener{
            override fun onLocationChanged(p0: Location) {
                location.setLocation(p0.latitude, p0.longitude)
                _dataLoaded.value = true
                locationManager.removeUpdates(this)
            }
        })
    }

    fun updateWeather(location: NowLocation): Call<Weather> = this.weatherAPIService.getWeatherStatusInfo(WEATHER_APP_ID, location.getLatitude(), location.getLongitude())

    fun updateDust(location: NowLocation): Call<Dust> = this.dustAPIService.getDustStatusInfo(location.getLatitude(), location.getLongitude(), DUST_APP_TOKEN)

}