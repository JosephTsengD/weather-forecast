package com.weatherforecast.data.weather

import com.weatherforecast.core.common.CurrentWeather
import com.weatherforecast.core.common.DailyForecast
import com.weatherforecast.core.common.WeatherRepository
import com.weatherforecast.core.common.Result
import com.weatherforecast.core.network.WeatherApi
import com.weatherforecast.core.network.NetworkConfig
import com.weatherforecast.core.network.toDomain
import com.weatherforecast.core.network.toDailyForecastList
import com.weatherforecast.core.database.WeatherDao
import com.weatherforecast.core.database.CityDao
import com.weatherforecast.core.database.toDomain
import com.weatherforecast.core.database.toCacheEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 天氣倉庫實作
 */
class WeatherRepositoryImpl(
    private val weatherApi: WeatherApi,
    private val weatherDao: WeatherDao,
    private val cityDao: CityDao
) : WeatherRepository {
    
    override suspend fun getCurrentWeather(cityId: String): Result<CurrentWeather> = withContext(Dispatchers.IO) {
        try {
            android.util.Log.d("WeatherRepository", "調用當前天氣 API: $cityId")
            // 先嘗試從網路獲取
            val weatherDto = weatherApi.getCurrentWeather(cityId, NetworkConfig.API_KEY)
            android.util.Log.d("WeatherRepository", "API 響應成功")
            val weather = weatherDto.toDomain()
            
            // 保存到緩存
            saveWeatherCache(weather)
            
            Result.Success(weather)
        } catch (e: Exception) {
            android.util.Log.e("WeatherRepository", "獲取當前天氣失敗", e)
            // 網路失敗時嘗試從緩存獲取
            val cachedWeather = getWeatherCache(cityId)
            if (cachedWeather != null) {
                android.util.Log.d("WeatherRepository", "使用緩存數據")
                Result.Success(cachedWeather)
            } else {
                android.util.Log.e("WeatherRepository", "無緩存數據")
                Result.Error(e)
            }
        }
    }
    
    override suspend fun getWeeklyForecast(cityId: String): Result<List<DailyForecast>> = withContext(Dispatchers.IO) {
        try {
            android.util.Log.d("WeatherRepository", "調用預報 API: $cityId")
            val forecastDto = weatherApi.getWeeklyForecast(cityId, NetworkConfig.API_KEY)
            android.util.Log.d("WeatherRepository", "API 響應成功，數據點數: ${forecastDto.list.size}")
            val forecast = forecastDto.toDailyForecastList()
            android.util.Log.d("WeatherRepository", "轉換後的每日預報數: ${forecast.size}")
            Result.Success(forecast)
        } catch (e: Exception) {
            android.util.Log.e("WeatherRepository", "獲取預報失敗", e)
            android.util.Log.e("WeatherRepository", "錯誤訊息: ${e.message}")
            Result.Error(e)
        }
    }
    
    override suspend fun saveWeatherCache(weather: CurrentWeather) = withContext(Dispatchers.IO) {
        weatherDao.insertWeatherCache(weather.toCacheEntity())
    }
    
    override suspend fun getWeatherCache(cityId: String): CurrentWeather? = withContext(Dispatchers.IO) {
        weatherDao.getWeatherCache(cityId)?.toDomain()
    }
}
