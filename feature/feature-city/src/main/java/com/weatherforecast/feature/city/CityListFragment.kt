package com.weatherforecast.feature.city

import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.weatherforecast.core.common.City
import com.weatherforecast.core.utils.UiUtils
import com.weatherforecast.feature.city.databinding.FragmentCityListBinding
import com.weatherforecast.feature.city.R
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * 城市列表 Fragment
 */
class CityListFragment : Fragment() {
    
    private var _binding: FragmentCityListBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: CityListViewModel by viewModel()
    
    private lateinit var cityAdapter: CityAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCityListBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        setupClickListeners()
        viewModel.loadCities()
    }
    
    private fun setupRecyclerView() {
        cityAdapter = CityAdapter(
            onCityClick = { city ->
                viewModel.selectCity(city)
            },
            onCityDelete = { city ->
                viewModel.deleteCity(city)
            }
        )
        
        binding.rvCities.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cityAdapter
        }
    }
    
    private fun setupObservers() {
        viewModel.uiState.observe(viewLifecycleOwner, Observer { state ->
            when {
                state.isLoading -> showLoading()
                state.error != null -> showError(state.error)
                else -> showCities(state)
            }
        })
        
        viewModel.shouldNavigateBack.observe(viewLifecycleOwner, Observer { shouldNavigate ->
            if (shouldNavigate) {
                activity?.supportFragmentManager?.popBackStack()
            }
        })
    }
    
    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.rvCities.visibility = View.GONE
    }
    
    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.rvCities.visibility = View.VISIBLE
    }
    
    private fun showError(error: String) {
        hideLoading()
        UiUtils.showError(binding.root, error)
    }
    
    private fun showCities(state: CityListUiState) {
        hideLoading()
        cityAdapter.submitList(state.cities)
    }
    
    private fun setupClickListeners() {
        binding.fabAddCity.setOnClickListener {
            showAddCityDialog()
        }
        
        // 搜尋功能
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.searchCities(it) }
                return true
            }
            
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { viewModel.searchCities(it) }
                return true
            }
        })
    }
    
    private fun showAddCityDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.add_city)
        builder.setMessage(R.string.add_city_message)
        builder.setPositiveButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
        builder.show()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
