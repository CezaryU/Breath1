package com.wel.wat.breathalyser.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MeasurementViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<Measurement>>
    private val repository: MeasurementRepository

    init {
        val measurementDao = MeasurementDatabase.getInstance(application).measurementDao()
        repository = MeasurementRepository(measurementDao)
        readAllData = repository.readAllData
    }

    fun addMeasurement(measurement: Measurement){
        viewModelScope.launch(Dispatchers.IO){
            repository.addMeasurement(measurement)
        }
    }

    fun deleteAllData(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllData()
        }
    }
}