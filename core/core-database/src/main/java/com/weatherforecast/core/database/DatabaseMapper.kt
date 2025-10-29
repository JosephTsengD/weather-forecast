package com.weatherforecast.core.database

import com.weatherforecast.core.common.City
import com.weatherforecast.core.common.CurrentWeather

/**
 * 數據庫實體轉換為 Domain 模型的擴展函數
 */

fun CityEntity.toDomain(): City {
    return City(
        id = id,
        name = name,
        country = country,
        latitude = latitude,
        longitude = longitude,
        isSelected = isSelected
    )
}

fun City.toEntity(): CityEntity {
    return CityEntity(
        id = id,
        name = name,
        country = country,
        latitude = latitude,
        longitude = longitude,
        isSelected = isSelected
    )
}

fun WeatherCacheEntity.toDomain(): CurrentWeather {
    return CurrentWeather(
        cityId = cityId,
        cityName = cityName,
        temperature = temperature,
        feelsLike = feelsLike,
        description = description,
        icon = icon,
        humidity = humidity,
        windSpeed = windSpeed,
        pressure = pressure,
        timestamp = timestamp
    )
}

fun CurrentWeather.toCacheEntity(): WeatherCacheEntity {
    return WeatherCacheEntity(
        cityId = cityId,
        cityName = cityName,
        temperature = temperature,
        feelsLike = feelsLike,
        description = description,
        icon = icon,
        humidity = humidity,
        windSpeed = windSpeed,
        pressure = pressure,
        timestamp = timestamp
    )
}
