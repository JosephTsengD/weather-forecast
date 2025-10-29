package com.weatherforecast.data.weather

import com.weatherforecast.core.common.City
import com.weatherforecast.core.common.CityRepository
import com.weatherforecast.core.common.Result
import com.weatherforecast.core.network.WeatherApi
import com.weatherforecast.core.network.NetworkConfig
import com.weatherforecast.core.network.toDomain
import com.weatherforecast.core.database.CityDao
import com.weatherforecast.core.database.toDomain
import com.weatherforecast.core.database.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 城市倉庫實作
 */
class CityRepositoryImpl(
    private val weatherApi: WeatherApi,
    private val cityDao: CityDao
) : CityRepository {
    
    override suspend fun getAllCities(): List<City> = withContext(Dispatchers.IO) {
        cityDao.getAllCities().map { it.toDomain() }
    }
    
    override suspend fun getSelectedCity(): City = withContext(Dispatchers.IO) {
        val selectedCity = cityDao.getSelectedCity()
        if (selectedCity != null) {
            selectedCity.toDomain()
        } else {
            // 如果沒有選中的城市，返回預設城市（台北）
            val defaultCity = City(
                id = "1668341",
                name = "台北市",
                country = "台灣",
                latitude = 25.0330,
                longitude = 121.5654,
                isSelected = true
            )
            saveCity(defaultCity)
            defaultCity
        }
    }
    
    override suspend fun saveCity(city: City) = withContext(Dispatchers.IO) {
        cityDao.insertCity(city.toEntity())
    }
    
    override suspend fun deleteCity(cityId: String): Unit = withContext(Dispatchers.IO) {
        val city = cityDao.getAllCities().find { it.id == cityId }
        city?.let { cityDao.deleteCity(it) }
    }
    
    override suspend fun setSelectedCity(cityId: String) = withContext(Dispatchers.IO) {
        cityDao.clearSelectedCities()
        cityDao.setSelectedCity(cityId)
    }
    
    override suspend fun searchCities(query: String): List<City> = withContext(Dispatchers.IO) {
        try {
            android.util.Log.d("CityRepository", "搜索城市: $query")
            // 先從本地搜尋
            val localResults = cityDao.searchCities(query).map { it.toDomain() }
            android.util.Log.d("CityRepository", "本地搜索結果: ${localResults.size}")
            
            // 如果本地沒有結果，從網路搜尋
            if (localResults.isEmpty() && query.isNotBlank()) {
                android.util.Log.d("CityRepository", "從網路搜索城市")
                val searchResponse = weatherApi.searchCities(query, NetworkConfig.API_KEY)
                val networkResults = searchResponse.list.map { it.toDomain() }
                android.util.Log.d("CityRepository", "網路搜索結果: ${networkResults.size}")
                
                // 保存搜尋結果到本地
                networkResults.forEach { saveCity(it) }
                
                networkResults
            } else {
                localResults
            }
        } catch (e: Exception) {
            android.util.Log.e("CityRepository", "搜索失敗", e)
            // 網路搜尋失敗時返回本地結果
            cityDao.searchCities(query).map { it.toDomain() }
        }
    }
}
