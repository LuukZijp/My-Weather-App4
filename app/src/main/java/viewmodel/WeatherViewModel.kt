package com.example.myapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.WeatherRepository
import com.example.myapp.data.WeatherResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val repo = WeatherRepository()

    private val _weatherState = MutableStateFlow<WeatherResponse?>(null)
    val weatherState: StateFlow<WeatherResponse?> = _weatherState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadWeather(city: String) {
        val query = city.trim()
        if (query.isEmpty()) {
            _errorMessage.value = "Voer eerst een stad in."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            runCatching {
                repo.getWeather(query)
            }.onSuccess { response ->
                _weatherState.value = response
            }.onFailure {
                _errorMessage.value = "Kon weerdata niet laden. Controleer je internet of stadsnaam."
            }

            _isLoading.value = false
        }
    }
}
