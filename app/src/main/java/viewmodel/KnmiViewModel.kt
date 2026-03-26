package com.example.myapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.EarthquakeNL
import com.example.myapp.data.KnmiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class KnmiViewModel : ViewModel() {

    private val repo = KnmiRepository()

    private val _quakes = MutableStateFlow<List<EarthquakeNL>>(emptyList())
    val quakes: StateFlow<List<EarthquakeNL>> = _quakes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadEarthquakes() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            runCatching {
                repo.getQuakes()
            }.onSuccess { data ->
                _quakes.value = data
                if (data.isEmpty()) {
                    _errorMessage.value = "Geen KNMI-aardbevingen gevonden op dit moment."
                }
            }.onFailure {
                _errorMessage.value = "Kon KNMI-data niet laden."
            }

            _isLoading.value = false
        }
    }
}
