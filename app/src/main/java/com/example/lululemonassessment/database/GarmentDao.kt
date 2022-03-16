package com.example.lululemonassessment.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.lululemonassessment.models.Garment
import kotlinx.coroutines.flow.Flow

@Dao
interface GarmentDao {
    @Query("SELECT * FROM garment")
    fun fetchAllGarments(): Flow<List<Garment>>

    @Insert
    fun insertGarment(garment: Garment)
}
