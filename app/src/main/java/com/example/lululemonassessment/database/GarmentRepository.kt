package com.example.lululemonassessment.database

import android.app.Application
import com.example.lululemonassessment.models.Garment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface IGarmentRepository {
    fun allGarments(): Flow<List<Garment>>
    fun insertGarment(garment: Garment)
}

class GarmentRepository @Inject constructor(application: Application) : IGarmentRepository {
    private var garmentDao: GarmentDao
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        val database = AppDatabase.getDatabase(application)
        garmentDao = database.garmentDao
    }

    override fun allGarments(): Flow<List<Garment>> = garmentDao.fetchAllGarments()

    override fun insertGarment(garment: Garment) {
        coroutineScope.launch {
            garmentDao.insertGarment(garment)
        }
    }
}
