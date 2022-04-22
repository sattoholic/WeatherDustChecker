package com.sattoholic.weatherdustchecker.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.sattoholic.weatherdustchecker.models.Weather
import java.lang.reflect.Type

class WeatherDeserializer : JsonDeserializer<Weather> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Weather {
        val root = json?.asJsonObject
        val weatherNode = root?.getAsJsonArray("weather")?.get(0)?.asJsonObject
        val idValue = weatherNode?.get("id")?.asString

        val mainNode = root?.getAsJsonObject("main")
        val tempValue = mainNode?.get("temp")?.asString
        val pressureValue = mainNode?.get("pressure")?.asString
        val humidityValue = mainNode?.get("humidity")?.asString

        return Weather(tempValue!!, humidityValue!!, pressureValue!!, idValue!!)
    }
}