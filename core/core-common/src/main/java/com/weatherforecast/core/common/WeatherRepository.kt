package com.weatherforecast.core.common

/**
 * 天氣倉庫介面
 */
interface WeatherRepository {
    suspend fun getCurrentWeather(cityId: String): Result<CurrentWeather>
    suspend fun getWeeklyForecast(cityId: String): Result<List<DailyForecast>>
    suspend fun saveWeatherCache(weather: CurrentWeather)
    suspend fun getWeatherCache(cityId: String): CurrentWeather?
}

/**
 * 城市倉庫介面
 */
interface CityRepository {
    suspend fun getAllCities(): List<City>
    suspend fun getSelectedCity(): City
    suspend fun saveCity(city: City)
    suspend fun deleteCity(cityId: String)
    suspend fun setSelectedCity(cityId: String)
    suspend fun searchCities(query: String): List<City>
}
