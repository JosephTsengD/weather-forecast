package com.weatherforecast.feature.weather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weatherforecast.core.common.DailyForecast
import com.weatherforecast.core.utils.UiUtils
import com.weatherforecast.feature.weather.databinding.ItemDailyForecastBinding

/**
 * 每日預報適配器
 */
class DailyForecastAdapter : ListAdapter<DailyForecast, DailyForecastAdapter.DailyForecastViewHolder>(DailyForecastDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyForecastViewHolder {
        val binding = ItemDailyForecastBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DailyForecastViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: DailyForecastViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class DailyForecastViewHolder(
        private val binding: ItemDailyForecastBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(forecast: DailyForecast) {
            binding.apply {
                tvDay.text = forecast.dayOfWeek
                tvDate.text = forecast.date
                tvHighTemp.text = UiUtils.formatTemperature(forecast.highTemp)
                tvLowTemp.text = UiUtils.formatTemperature(forecast.lowTemp)
                
                UiUtils.loadWeatherIcon(root.context, ivWeatherIcon, forecast.icon)
            }
        }
    }
}

/**
 * DiffUtil 回調
 */
class DailyForecastDiffCallback : DiffUtil.ItemCallback<DailyForecast>() {
    override fun areItemsTheSame(oldItem: DailyForecast, newItem: DailyForecast): Boolean {
        return oldItem.date == newItem.date
    }
    
    override fun areContentsTheSame(oldItem: DailyForecast, newItem: DailyForecast): Boolean {
        return oldItem == newItem
    }
}
