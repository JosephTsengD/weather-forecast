package com.weatherforecast.feature.weather

import com.weatherforecast.core.common.CurrentWeather
import com.weatherforecast.core.common.DailyForecast
import com.weatherforecast.core.common.WeatherRepository
import com.weatherforecast.core.common.CityRepository
import com.weatherforecast.core.common.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {
    
    @Mock
    private lateinit var weatherRepository: WeatherRepository
    
    @Mock
    private lateinit var cityRepository: CityRepository
    
    private lateinit var viewModel: WeatherViewModel
    
    private val testDispatcher = StandardTestDispatcher()
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = WeatherViewModel(weatherRepository, cityRepository)
    }
    
    @Test
    fun `loadWeather should update uiState with weather data when successful`() = runTest {
        // Given
        val mockCity = com.weatherforecast.core.common.City(
            id = "1668341",
            name = "台北市",
            country = "台灣",
            latitude = 25.0330,
            longitude = 121.5654,
            isSelected = true
        )
        
        val mockWeather = CurrentWeather(
            cityId = "1668341",
            cityName = "台北市",
            temperature = 28.0,
            feelsLike = 30.0,
            description = "晴天",
            icon = "01d",
            humidity = 65,
            windSpeed = 12.0,
            pressure = 1013
        )
        
        val mockForecast = listOf(
            DailyForecast(
                date = "10/24",
                dayOfWeek = "週二",
                highTemp = 26.0,
                lowTemp = 20.0,
                description = "多雲",
                icon = "02d",
                humidity = 70,
                windSpeed = 10.0
            )
        )
        
        whenever(cityRepository.getSelectedCity()).thenReturn(mockCity)
        whenever(weatherRepository.getCurrentWeather("1668341")).thenReturn(Result.Success(mockWeather))
        whenever(weatherRepository.getWeeklyForecast("1668341")).thenReturn(Result.Success(mockForecast))
        
        // When
        viewModel.loadWeather()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        val uiState = viewModel.uiState.value
        assertFalse(uiState?.isLoading ?: true)
        assertEquals(mockWeather, uiState?.currentWeather)
        assertEquals(mockForecast, uiState?.weeklyForecast)
        assertNull(uiState?.error)
    }
    
    @Test
    fun `loadWeather should show error when repository fails`() = runTest {
        // Given
        val mockCity = com.weatherforecast.core.common.City(
            id = "1668341",
            name = "台北市",
            country = "台灣",
            latitude = 25.0330,
            longitude = 121.5654,
            isSelected = true
        )
        
        whenever(cityRepository.getSelectedCity()).thenReturn(mockCity)
        whenever(weatherRepository.getCurrentWeather("1668341")).thenReturn(Result.Error(Exception("Network error")))
        
        // When
        viewModel.loadWeather()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        val uiState = viewModel.uiState.value
        assertFalse(uiState?.isLoading ?: true)
        assertNull(uiState?.currentWeather)
        assertTrue(uiState?.weeklyForecast?.isEmpty() ?: false)
        assertEquals("Network error", uiState?.error)
    }
}
