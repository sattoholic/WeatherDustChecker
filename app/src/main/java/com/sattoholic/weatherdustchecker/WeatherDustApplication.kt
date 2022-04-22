package com.sattoholic.weatherdustchecker

import android.app.Application
import com.sattoholic.weatherdustchecker.repository.Repository

class WeatherDustApplication() : Application() {
    var repository: Repository? = null

    constructor(api_key: String, token: String) : this() {
        this.repository = Repository(api_key, token)
    }

    companion object {
        private var INSTANCE: WeatherDustApplication? = null

        fun getInstance(api_key: String, token: String): WeatherDustApplication {
            if (INSTANCE == null) {
                this.INSTANCE = WeatherDustApplication(api_key, token)
            }
            return INSTANCE!!
        }

        fun getInstance(): WeatherDustApplication {
            if (INSTANCE == null) {
                throw IllegalAccessException("애플리케이션 초기화 안됨")
            }
            return INSTANCE!!
        }
    }
}