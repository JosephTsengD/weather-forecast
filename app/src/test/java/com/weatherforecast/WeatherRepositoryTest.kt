package com.weatherforecast.data.weather

import com.weatherforecast.core.common.CurrentWeather
import com.weatherforecast.core.common.Result
import com.weatherforecast.core.common.getOrNull
import com.weatherforecast.core.network.WeatherApi
import com.weatherforecast.core.database.WeatherDao
import com.weatherforecast.core.database.CityDao
import com.weatherforecast.core.database.WeatherCacheEntity
import com.weatherforecast.core.database.toDomain
import com.weatherforecast.core.database.toCacheEntity
import com.weatherforecast.core.network.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherRepositoryImplTest {
    
    @Mock
    private lateinit var weatherApi: WeatherApi
    
    @Mock
    private lateinit var weatherDao: WeatherDao
    
    @Mock
    private lateinit var cityDao: CityDao
    
    private lateinit var repository: WeatherRepositoryImpl
    
    private val testDispatcher = StandardTestDispatcher()
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        repository = WeatherRepositoryImpl(weatherApi, weatherDao, cityDao)
    }
    
    @Test
    fun `getCurrentWeather should return cached data when network fails`() = runTest {
        // Given
        val cityId = "1668341"
        val cachedWeather = WeatherCacheEntity(
            cityId = cityId,
            cityName = "台北市",
            temperature = 28.0,
            feelsLike = 30.0,
            description = "晴天",
            icon = "01d",
            humidity = 65,
            windSpeed = 12.0,
            pressure = 1013,
            timestamp = System.currentTimeMillis()
        )
        
        whenever(weatherApi.getCurrentWeather(cityId)).thenThrow(Exception("Network error"))
        whenever(weatherDao.getWeatherCache(cityId)).thenReturn(cachedWeather)
        
        // When
        val result = repository.getCurrentWeather(cityId)
        
        // Then
        assertTrue(result is Result.Success)
        val resultData = result.getOrNull()
        assertEquals(cachedWeather.toDomain(), resultData)
        verify(weatherDao).getWeatherCache(cityId)
    }
    
    @Test
    fun `getCurrentWeather should save to cache when network succeeds`() = runTest {
        // Given
        val cityId = "1668341"
        val weatherDto = com.weatherforecast.core.network.CurrentWeatherDto(
            id = 1668341L,
            name = "台北市",
            main = com.weatherforecast.core.network.MainDto(
                temp = 28.0,
                feels_like = 30.0,
                temp_min = 25.0,
                temp_max = 32.0,
                humidity = 65,
                pressure = 1013
            ),
            weather = listOf(
                com.weatherforecast.core.network.WeatherDescriptionDto(
                    id = 800,
                    main = "Clear",
                    description = "晴天",
                    icon = "01d"
                )
            ),
            wind = com.weatherforecast.core.network.WindDto(
                speed = 12.0,
                deg = 180
            ),
            dt = System.currentTimeMillis() / 1000
        )
        
        whenever(weatherApi.getCurrentWeather(cityId)).thenReturn(weatherDto)
        
        // When
        val result = repository.getCurrentWeather(cityId)
        
        // Then
        assertTrue(result is Result.Success)
        val weather = weatherDto.toDomain()
        verify(weatherDao).insertWeatherCache(weather.toCacheEntity())
    }
}
