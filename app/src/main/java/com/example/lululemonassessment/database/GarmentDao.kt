package com.example.lululemonassessment.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.lululemonassessment.models.Garment

@Dao
interface GarmentDao {
    @Query("SELECT * FROM garment ORDER BY title")
    fun fetchAllGarmentsByTitle(): LiveData<List<Garment>>

    @Query("SELECT * FROM garment ORDER BY creationDate")
    fun fetchAllGarmentsByCreationDate(): LiveData<List<Garment>>

    @Insert
    fun insertGarment(garment: Garment)
}
