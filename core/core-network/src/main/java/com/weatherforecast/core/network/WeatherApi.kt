package com.weatherforecast.core.network

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * OpenWeatherMap API 介面
 */
interface WeatherApi {
    
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("id") cityId: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "zh_tw"
    ): CurrentWeatherDto
    
    @GET("forecast")
    suspend fun getWeeklyForecast(
        @Query("id") cityId: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "zh_tw"
    ): WeeklyForecastDto
    
    @GET("find")
    suspend fun searchCities(
        @Query("q") query: String,
        @Query("appid") apiKey: String,
        @Query("type") type: String = "like"
    ): CitySearchResponseDto
}

/**
 * 當前天氣 DTO
 */
data class CurrentWeatherDto(
    val id: Long,
    val name: String,
    val main: MainDto,
    val weather: List<WeatherDescriptionDto>,
    val wind: WindDto,
    val dt: Long
)

data class MainDto(
    val temp: Double,
    val feels_like: Double,
    var temp_min: Double,
    var temp_max: Double,
    val humidity: Int,
    val pressure: Int
)

data class WeatherDescriptionDto(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class WindDto(
    val speed: Double,
    val deg: Int
)

/**
 * 一週預報 DTO
 */
data class WeeklyForecastDto(
    val list: List<ForecastItemDto>
)

data class ForecastItemDto(
    val dt: Long,
    val main: MainDto,
    val weather: List<WeatherDescriptionDto>,
    val wind: WindDto,
    val dt_txt: String
)

/**
 * 城市搜尋回應 DTO
 */
data class CitySearchResponseDto(
    val list: List<CityDto>
)

data class CityDto(
    val id: Long,
    val name: String,
    val country: String? = null,
    val coord: CoordDto,
    val sys: CitySysDto? = null
)

data class CoordDto(
    val lat: Double,
    val lon: Double
)

data class CitySysDto(
    val country: String?
)
