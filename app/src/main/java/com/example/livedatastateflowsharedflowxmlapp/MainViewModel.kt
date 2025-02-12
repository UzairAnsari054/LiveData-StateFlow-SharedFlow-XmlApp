package com.example.livedatastateflowsharedflowxmlapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _liveData = MutableLiveData("Default Live Data")
    val liveData: LiveData<String> = _liveData

    private val _stateFlow = MutableStateFlow("Default State Flow")
    val stateFlow = _stateFlow.asStateFlow()

    private val _sharedFlow = MutableSharedFlow<String>()
    val sharedFlow = _sharedFlow.asSharedFlow()

    fun triggerLiveData() {
        _liveData.value = "This is Live Data bc"
    }

    fun triggerStateFlow() {
        _stateFlow.value = "This is State Flow bc"
    }

    fun triggerNormalFlow(): Flow<String> {
        return flow {
            repeat(5) {
                emit("Normal Flow: $it")
                delay(1000)
            }
        }
    }

    fun triggerSharedFlow() {
        viewModelScope.launch {
            Log.d("SharedFlow", "Emmiting: VALUE")
            _sharedFlow.emit("This is Shared Flow bc")
            Log.d("SharedFlow", "Emmited: VALUE")
        }
    }
}