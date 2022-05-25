package com.wel.wat.breathalyser.database

import androidx.lifecycle.LiveData

class MeasurementRepository(private val measurementDao: MeasurementDao) {

    val readAllData: LiveData<List<Measurement>> = measurementDao.readAllData()

    fun addMeasurement(measurement: Measurement){
        measurementDao.addMeasurement(measurement)
    }

    fun deleteAllData(){
        measurementDao.deleteAllData()
    }
}