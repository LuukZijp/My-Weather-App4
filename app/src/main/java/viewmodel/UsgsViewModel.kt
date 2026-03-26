package com.example.myapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.EarthquakeGlobal
import com.example.myapp.data.UsgsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UsgsViewModel : ViewModel() {

    private val repo = UsgsRepository()

    private val _quakes = MutableStateFlow<List<EarthquakeGlobal>>(emptyList())
    val quakes: StateFlow<List<EarthquakeGlobal>> = _quakes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadEarthquakes() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            runCatching {
                repo.getGlobalQuakes()
            }.onSuccess { data ->
                _quakes.value = data
                if (data.isEmpty()) {
                    _errorMessage.value = "Geen wereldwijde aardbevingen gevonden."
                }
            }.onFailure { throwable ->
                val detail = throwable.message?.takeIf { it.isNotBlank() } ?: "onbekende fout"
                _errorMessage.value = "Kon USGS-data niet laden: $detail"
            }

            _isLoading.value = false
        }
    }
}
