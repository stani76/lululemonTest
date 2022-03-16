package com.example.lululemonassessment.database

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.lululemonassessment.models.Garment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GarmentRepository @Inject constructor(application: Application) {
    private var garmentDao: GarmentDao
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    init {
        val database = AppDatabase.getDatabase(application)
        garmentDao = database.garmentDao
    }

    fun allGarmentsByTitle(): LiveData<List<Garment>> = garmentDao.fetchAllGarmentsByTitle()
    fun allGarmentsByCreationDate(): LiveData<List<Garment>> = garmentDao.fetchAllGarmentsByCreationDate()

    fun insertGarment(garment: Garment) {
        garmentDao.insertGarment(garment)
    }
}
