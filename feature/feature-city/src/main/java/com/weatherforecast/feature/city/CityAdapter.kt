package com.weatherforecast.feature.city

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weatherforecast.core.common.City
import com.weatherforecast.feature.city.databinding.ItemCityBinding

/**
 * 城市適配器
 */
class CityAdapter(
    private val onCityClick: (City) -> Unit,
    private val onCityDelete: (City) -> Unit
) : ListAdapter<City, CityAdapter.CityViewHolder>(CityDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val binding = ItemCityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CityViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class CityViewHolder(
        private val binding: ItemCityBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(city: City) {
            binding.apply {
                tvCityName.text = city.name
                tvCountry.text = city.country
                
                // 顯示選中狀態
                if (city.isSelected) {
                    ivSelected.visibility = android.view.View.VISIBLE
                    root.setBackgroundColor(
                        root.context.getColor(android.R.color.holo_blue_light)
                    )
                } else {
                    ivSelected.visibility = android.view.View.GONE
                    root.setBackgroundColor(
                        root.context.getColor(android.R.color.white)
                    )
                }
                
                // 點擊事件
                root.setOnClickListener {
                    onCityClick(city)
                }
                
                // 長按刪除
                root.setOnLongClickListener {
                    onCityDelete(city)
                    true
                }
            }
        }
    }
}

/**
 * DiffUtil 回調
 */
class CityDiffCallback : DiffUtil.ItemCallback<City>() {
    override fun areItemsTheSame(oldItem: City, newItem: City): Boolean {
        return oldItem.id == newItem.id
    }
    
    override fun areContentsTheSame(oldItem: City, newItem: City): Boolean {
        return oldItem == newItem
    }
}
