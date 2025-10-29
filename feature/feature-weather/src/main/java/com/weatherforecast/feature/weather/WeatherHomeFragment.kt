package com.weatherforecast.feature.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.weatherforecast.core.utils.UiUtils
import com.weatherforecast.feature.weather.databinding.FragmentWeatherHomeBinding
import com.weatherforecast.feature.weather.R
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * 天氣主畫面 Fragment
 */
class WeatherHomeFragment : Fragment() {
    
    private var _binding: FragmentWeatherHomeBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: WeatherViewModel by viewModel()
    
    private lateinit var dailyForecastAdapter: DailyForecastAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        setupClickListeners()
        viewModel.loadWeather()
    }
    
    override fun onResume() {
        super.onResume()
        // 每次恢復顯示時重新載入天氣數據
        viewModel.loadWeather()
    }
    
    private fun setupRecyclerView() {
        dailyForecastAdapter = DailyForecastAdapter()
        binding.rvWeeklyForecast.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dailyForecastAdapter
        }
    }
    
    private fun setupObservers() {
        viewModel.uiState.observe(viewLifecycleOwner, Observer { state ->
            when {
                state.isLoading -> showLoading()
                state.error != null -> showError(state.error)
                else -> showWeatherData(state)
            }
        })
    }
    
    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.scrollView.visibility = View.GONE
    }
    
    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.scrollView.visibility = View.VISIBLE
    }
    
    private fun showError(error: String) {
        hideLoading()
        UiUtils.showError(binding.root, error)
    }
    
    private fun showWeatherData(state: WeatherUiState) {
        hideLoading()
        
        state.currentWeather?.let { weather ->
            binding.apply {
                tvCityName.text = weather.cityName
                tvTemperature.text = UiUtils.formatTemperature(weather.temperature)
                tvDescription.text = weather.description
                tvFeelsLike.text = getString(R.string.feels_like_format, UiUtils.formatTemperature(weather.feelsLike))
                tvHumidity.text = getString(R.string.humidity_format, UiUtils.formatHumidity(weather.humidity))
                tvWindSpeed.text = getString(R.string.wind_speed_format, UiUtils.formatWindSpeed(weather.windSpeed))
                tvPressure.text = getString(R.string.pressure_format, UiUtils.formatPressure(weather.pressure))
                
                UiUtils.loadWeatherIcon(requireContext(), ivWeatherIcon, weather.icon)
            }
        }
        
        dailyForecastAdapter.submitList(state.weeklyForecast)
    }
    
    private fun setupClickListeners() {
        binding.btnSelectCity.setOnClickListener {
            showCityListFragment()
        }
        
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshWeather()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }
    
    private fun showCityListFragment() {
        val callback = activity as? OnCitySelectionRequested
        callback?.showCitySelection()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
