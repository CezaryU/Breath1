package com.wel.wat.breathalyser.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "last_measurement")
data class Measurement (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val alcoholMl: Double,
    val alcoholPr: Double,
    val percentage: Double,
    val drinkTime: Long = System.currentTimeMillis(),
)