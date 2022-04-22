package com.sattoholic.weatherdustchecker.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.sattoholic.weatherdustchecker.models.Dust
import java.lang.reflect.Type

class DustDeserializer : JsonDeserializer<Dust> {
    private val checkCategory = { i: Int? ->
        when (i) {
            in 0..100 -> "좋음"
            in 101..200 -> "보통"
            in 201..300 -> "나쁨"
            else -> "매우 나쁨"
        }
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Dust {
        val root = json?.asJsonObject
        val dataNode = root?.getAsJsonObject("data")
        val iaqiNode = dataNode?.getAsJsonObject("iaqi")
        val pm10Node = iaqiNode?.getAsJsonObject("pm10")
        val pm25Node = iaqiNode?.getAsJsonObject("pm25")
        val coNode = iaqiNode?.getAsJsonObject("co")
        val no2Node = iaqiNode?.getAsJsonObject("no2")
        val o3Node = iaqiNode?.getAsJsonObject("o3")

        val pm10Value = pm10Node?.get("v")?.asInt
        val pm25Value = pm25Node?.get("v")?.asInt
        val co2Value = coNode?.get("v")?.asString
        val no2Value = no2Node?.get("v")?.asString
        val o3Value = o3Node?.get("v")?.asString

        return Dust(
            pm10Value!!.toString(),
            pm25Value!!.toString(),
            checkCategory(pm10Value),
            checkCategory(pm25Value),
            co2Value!!,
            o3Value!!,
            no2Value!!
        )
    }
}