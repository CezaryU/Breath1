package com.wel.wat.breathalyser.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MeasurementDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addMeasurement(measurement: Measurement)

    @Query("DELETE FROM last_measurement")
    fun deleteAllData()

    @Query("SELECT * FROM last_measurement ORDER BY id")
    fun readAllData(): LiveData<List<Measurement>>
}