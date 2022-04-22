package com.sattoholic.weatherdustchecker.viewmodel

import android.location.LocationManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sattoholic.weatherdustchecker.models.Dust
import com.sattoholic.weatherdustchecker.models.NowLocation
import com.sattoholic.weatherdustchecker.models.Weather
import com.sattoholic.weatherdustchecker.repository.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentsViewModel(private val repository: Repository) : ViewModel() {
    private val location = NowLocation.getInstance()

    var weatherData = Weather("", "", "", "")
    var dustData = Dust("", "", "", "", "", "", "")

    private var _dataLoaded = MutableLiveData<Boolean>().apply {
        this.value = false
    }

    private var _dataFailed = MutableLiveData<Boolean>().apply {
        this.value = false
    }
    private var _dataFailThrow = Throwable()
    private var weatherLoaded = false
    private var dustLoaded = false

    val dataLoaded: LiveData<Boolean>
        get() = _dataLoaded
    val dataFailed: LiveData<Boolean>
        get() = _dataFailed
    val locationUpdated = repository.dataLoaded
    val dataFailThrow
        get() = _dataFailThrow

    fun updateLocation(locationManager: LocationManager) {
        _dataLoaded.value = false
        repository.updateLocation(locationManager)
    }

    fun updateWeather() {
        weatherLoaded = false
        repository.updateWeather(location).enqueue(
            object : Callback<Weather> {
                override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                    val data = response.body()
                    if (data != null) {
                        weatherData = data
                        weatherLoaded = true
                        if (dustLoaded) {
                            _dataLoaded.value = true
                        }
                    }
                }

                override fun onFailure(call: Call<Weather>, t: Throwable) {
                    _dataFailed.value = true
                }
            }
        )
    }

    fun updateDust() {
        dustLoaded = false
        repository.updateDust(location).enqueue(
            object : Callback<Dust> {
                override fun onResponse(call: Call<Dust>, response: Response<Dust>) {
                    val data = response.body()
                    if (data != null) {
                        dustData = data
                        dustLoaded = true
                        if (weatherLoaded) {
                            _dataLoaded.value = true
                        }
                    }
                }

                override fun onFailure(call: Call<Dust>, t: Throwable) {
                    _dataFailed.value = true
                    _dataFailThrow = t
                }
            }
        )
    }
}

class FragmentsViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FragmentsViewModel::class.java))
            return FragmentsViewModel(repository) as T
        else {
            throw IllegalAccessException("잘못된 뷰모델 클래스 입니다.")
        }
    }
}