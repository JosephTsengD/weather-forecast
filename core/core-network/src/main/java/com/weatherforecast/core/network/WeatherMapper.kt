package com.weatherforecast.core.network

import com.weatherforecast.core.common.CurrentWeather
import com.weatherforecast.core.common.DailyForecast
import com.weatherforecast.core.common.City
import java.text.SimpleDateFormat
import java.util.*

/**
 * DTO 轉換為 Domain 模型的擴展函數
 */

fun CurrentWeatherDto.toDomain(): CurrentWeather {
    return CurrentWeather(
        cityId = id.toString(),
        cityName = name,
        temperature = main.temp,
        feelsLike = main.feels_like,
        description = weather.firstOrNull()?.description ?: "",
        icon = weather.firstOrNull()?.icon ?: "",
        humidity = main.humidity,
        windSpeed = wind.speed,
        pressure = main.pressure,
        timestamp = dt * 1000L
    )
}

fun ForecastItemDto.toDomain(): DailyForecast {
    val date = Date(dt * 1000L)
    val dateFormat = SimpleDateFormat("MM/dd", Locale.getDefault())
    val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
    
    // 將英文星期轉換為中文
    val dayOfWeekInEnglish = dayFormat.format(date)
    val dayOfWeekInChinese = when (dayOfWeekInEnglish) {
        "Monday" -> "週一"
        "Tuesday" -> "週二"
        "Wednesday" -> "週三"
        "Thursday" -> "週四"
        "Friday" -> "週五"
        "Saturday" -> "週六"
        "Sunday" -> "週日"
        else -> dayOfWeekInEnglish
    }
    
    return DailyForecast(
        date = dateFormat.format(date),
        dayOfWeek = dayOfWeekInChinese,
        highTemp = main.temp_max,
        lowTemp = main.temp_min,
        description = weather.firstOrNull()?.description ?: "",
        icon = weather.firstOrNull()?.icon ?: "",
        humidity = main.humidity,
        windSpeed = wind.speed
    )
}

fun CityDto.toDomain(): City {
    return City(
        id = id.toString(),
        name = name,
        country = sys?.country ?: country ?: "",
        latitude = coord.lat,
        longitude = coord.lon
    )
}

/**
 * 將一週預報轉換為每日預報（取每天的最高溫和最低溫）
 */
fun WeeklyForecastDto.toDailyForecastList(): List<DailyForecast> {
    // 將 3 小時預報按日期分組
    val dailyMap = mutableMapOf<String, MutableList<ForecastItemDto>>()
    
    list.forEach { item ->
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Date(item.dt * 1000L))
        
        if (!dailyMap.containsKey(date)) {
            dailyMap[date] = mutableListOf()
        }
        dailyMap[date]?.add(item)
    }
    
    // 為每個日期計算最高溫和最低溫
    return dailyMap.mapNotNull { (_, items) ->
        if (items.isNotEmpty()) {
            val maxTemp = items.maxOfOrNull { it.main.temp } ?: 0.0
            val minTemp = items.minOfOrNull { it.main.temp } ?: 0.0
            
            // 使用中午的天氣條件（第4個數據點，大約12點）
            val noonIndex = (items.size / 2).coerceAtMost(items.size - 1)
            val noonItem = items[noonIndex]
            
            val dateFormat = SimpleDateFormat("MM/dd", Locale.getDefault())
            val formattedDate = dateFormat.format(Date(noonItem.dt * 1000L))
            
            val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
            val dayOfWeekInEnglish = dayFormat.format(Date(noonItem.dt * 1000L))
            val dayOfWeekInChinese = when (dayOfWeekInEnglish) {
                "Monday" -> "週一"
                "Tuesday" -> "週二"
                "Wednesday" -> "週三"
                "Thursday" -> "週四"
                "Friday" -> "週五"
                "Saturday" -> "週六"
                "Sunday" -> "週日"
                else -> dayOfWeekInEnglish
            }
            
            DailyForecast(
                date = formattedDate,
                dayOfWeek = dayOfWeekInChinese,
                highTemp = maxTemp,
                lowTemp = minTemp,
                description = noonItem.weather.firstOrNull()?.description ?: "",
                icon = noonItem.weather.firstOrNull()?.icon ?: "",
                humidity = noonItem.main.humidity,
                windSpeed = noonItem.wind.speed
            )
        } else null
    }.sortedBy { it.date }.take(7) // 只取前7天
}
