package com.example.lululemonassessment.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.lululemonassessment.database.GarmentRepository
import com.example.lululemonassessment.models.Garment
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val garmentRepo: GarmentRepository) : ViewModel() {
    private val dbGarmentListByTitle: LiveData<List<Garment>> = garmentRepo.allGarmentsByTitle()
    private val dbGarmentListByCreationDate: LiveData<List<Garment>> = garmentRepo.allGarmentsByCreationDate()

    val garmentList = MediatorLiveData<List<Garment>>()
    var currentSort = mutableStateOf(SortOrder.Alpha)
    val currentScreen = mutableStateOf(Screens.List)
    val newGarmentName = mutableStateOf("")

    init {
        garmentList.addSource(dbGarmentListByTitle) { result ->
            if (currentSort.value == SortOrder.Alpha)
                result?.let { garmentList.value == it }
        }
        garmentList.addSource(dbGarmentListByCreationDate) { result ->
            if (currentSort.value == SortOrder.Creation)
                result?.let { garmentList.value == it }
        }
    }

    enum class SortOrder { Alpha, Creation }
    enum class Screens { List, Add }

    fun changeSort(newOrder: SortOrder) {
        when (newOrder) {
            SortOrder.Alpha -> dbGarmentListByTitle.value?.let { garmentList.value = it }
            SortOrder.Creation -> dbGarmentListByCreationDate.value?.let { garmentList.value = it }
        }.also {
            currentSort.value = newOrder
        }
    }
    fun addNewGarment() {
        if (newGarmentName.value.isNotBlank()) {
            garmentRepo.insertGarment((Garment(0, newGarmentName.value, LocalDateTime.now())))
            newGarmentName.value = ""
        }
    }
}
