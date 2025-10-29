package com.weatherforecast.feature.city

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weatherforecast.core.common.City
import com.weatherforecast.core.common.CityRepository
import com.weatherforecast.core.utils.ResourcesHelper
import com.weatherforecast.feature.city.R
import kotlinx.coroutines.launch

/**
 * 城市列表 UI 狀態
 */
data class CityListUiState(
    val isLoading: Boolean = false,
    val cities: List<City> = emptyList(),
    val searchQuery: String = "",
    val error: String? = null
)

/**
 * 城市列表 ViewModel
 */
class CityListViewModel(
    private val cityRepository: CityRepository
) : ViewModel() {
    
    private val _uiState = MutableLiveData(CityListUiState())
    val uiState: LiveData<CityListUiState> = _uiState
    
    private val _shouldNavigateBack = MutableLiveData<Boolean>()
    val shouldNavigateBack: LiveData<Boolean> = _shouldNavigateBack
    
    fun loadCities() {
        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(isLoading = true, error = null)
            
            try {
                val cities = cityRepository.getAllCities()
                _uiState.value = _uiState.value?.copy(
                    isLoading = false,
                    cities = cities,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(
                    isLoading = false,
                    error = e.message ?: ResourcesHelper.getString(R.string.error_loading_cities)
                )
            }
        }
    }
    
    fun searchCities(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(searchQuery = query)
            
            try {
                val cities = if (query.isBlank()) {
                    cityRepository.getAllCities()
                } else {
                    cityRepository.searchCities(query)
                }
                
                _uiState.value = _uiState.value?.copy(
                    cities = cities,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(
                    error = e.message ?: ResourcesHelper.getString(R.string.error_searching_cities)
                )
            }
        }
    }
    
    fun selectCity(city: City) {
        viewModelScope.launch {
            try {
                cityRepository.setSelectedCity(city.id)
                // 通知 Fragment 返回上一頁
                _shouldNavigateBack.value = true
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(
                    error = e.message ?: ResourcesHelper.getString(R.string.error_selecting_city)
                )
            }
        }
    }
    
    fun deleteCity(city: City) {
        viewModelScope.launch {
            try {
                cityRepository.deleteCity(city.id)
                loadCities()
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(
                    error = e.message ?: ResourcesHelper.getString(R.string.error_deleting_city)
                )
            }
        }
    }
}
