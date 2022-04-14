package com.sattoholic.weatherdustchecker.models

class NowLocation{
    private var latitude = 0.0
    private var longitude = 0.0

    fun setLocation(latitude: Double, longitude: Double){
        this.latitude = latitude
        this.longitude = longitude
    }

    fun getLatitude(): Double = this.latitude
    fun getLongitude(): Double = this.longitude

    companion object{
        private var INSTANCE: NowLocation? = null

        fun getInstance(): NowLocation{
            if(INSTANCE == null){
                INSTANCE = NowLocation()
            }
            return INSTANCE!!
        }
    }
}
