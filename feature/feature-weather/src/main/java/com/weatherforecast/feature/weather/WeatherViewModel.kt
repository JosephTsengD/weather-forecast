package com.weatherforecast.feature.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weatherforecast.core.common.CurrentWeather
import com.weatherforecast.core.common.DailyForecast
import com.weatherforecast.core.common.WeatherRepository
import com.weatherforecast.core.common.CityRepository
import com.weatherforecast.core.common.Result
import com.weatherforecast.core.common.getOrNull
import com.weatherforecast.core.utils.ResourcesHelper
import com.weatherforecast.feature.weather.R
import kotlinx.coroutines.launch

/**
 * 天氣 UI 狀態
 */
data class WeatherUiState(
    val isLoading: Boolean = false,
    val currentWeather: CurrentWeather? = null,
    val weeklyForecast: List<DailyForecast> = emptyList(),
    val error: String? = null
)

/**
 * 天氣 ViewModel
 */
class WeatherViewModel(
    private val weatherRepository: WeatherRepository,
    private val cityRepository: CityRepository
) : ViewModel() {
    
    private val _uiState = MutableLiveData(WeatherUiState())
    val uiState: LiveData<WeatherUiState> = _uiState
    
    fun loadWeather() {
        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(isLoading = true, error = null)
            
            try {
                val selectedCity = cityRepository.getSelectedCity()
                android.util.Log.d("WeatherViewModel", "選中城市: ${selectedCity.name} (${selectedCity.id})")
                
                // 並發獲取當前天氣和一週預報
                val currentWeatherResult = weatherRepository.getCurrentWeather(selectedCity.id)
                val weeklyForecastResult = weatherRepository.getWeeklyForecast(selectedCity.id)
                
                // 檢查 Result 類型
                android.util.Log.d("WeatherViewModel", "當前天氣 Result: ${currentWeatherResult.javaClass.simpleName}")
                android.util.Log.d("WeatherViewModel", "預報 Result: ${weeklyForecastResult.javaClass.simpleName}")
                
                val currentWeather = currentWeatherResult.getOrNull()
                val weeklyForecast = weeklyForecastResult.getOrNull()
                
                // 如果是錯誤，記錄詳細信息
                if (currentWeatherResult is Result.Error) {
                    android.util.Log.e("WeatherViewModel", "當前天氣錯誤: ${(currentWeatherResult as Result.Error).exception.message}", (currentWeatherResult as Result.Error).exception)
                }
                if (weeklyForecastResult is Result.Error) {
                    android.util.Log.e("WeatherViewModel", "預報錯誤: ${(weeklyForecastResult as Result.Error).exception.message}", (weeklyForecastResult as Result.Error).exception)
                }
                
                android.util.Log.d("WeatherViewModel", "當前天氣: ${currentWeather != null}")
                android.util.Log.d("WeatherViewModel", "一週預報數量: ${weeklyForecast?.size ?: 0}")
                
                _uiState.value = _uiState.value?.copy(
                    isLoading = false,
                    currentWeather = currentWeather,
                    weeklyForecast = weeklyForecast ?: emptyList(),
                    error = null
                )
            } catch (e: Exception) {
                android.util.Log.e("WeatherViewModel", ResourcesHelper.getString(R.string.log_loading_weather_failed), e)
                _uiState.value = _uiState.value?.copy(
                    isLoading = false,
                    error = e.message ?: ResourcesHelper.getString(R.string.error_loading_weather)
                )
            }
        }
    }
    
    fun refreshWeather() {
        loadWeather()
    }
}
