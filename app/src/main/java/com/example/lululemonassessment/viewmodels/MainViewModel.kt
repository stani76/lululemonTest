package com.example.lululemonassessment.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lululemonassessment.database.IGarmentRepository
import com.example.lululemonassessment.models.Garment
import com.example.lululemonassessment.models.Screens
import com.example.lululemonassessment.models.SortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val garmentRepo: IGarmentRepository) : ViewModel() {
    private val _garmentList = MutableStateFlow<List<Garment>>(listOf())
    val garmentList = _garmentList.asSharedFlow()

    var currentSort by mutableStateOf(SortOrder.Alpha)
    var currentScreen by mutableStateOf(Screens.List)
    var newGarmentName by mutableStateOf("")

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                garmentRepo.allGarments().collect {
                    withContext(Dispatchers.Main) {
                        _garmentList.tryEmit(if (it.isNotEmpty()) sortList(it) else it)
                    }
                }
            }
        }
    }

    private fun sortList(list: List<Garment>): List<Garment> {
        return when (currentSort) {
            SortOrder.Alpha -> list.sortedBy { it.title.uppercase() }
            SortOrder.Creation -> list.sortedBy { it.creationDate }
        }
    }

    fun sortGarments(newOrder: SortOrder = currentSort) {
        currentSort = newOrder
        _garmentList.tryEmit(sortList(_garmentList.value))
    }
    fun addNewGarment() {
        if (newGarmentName.isNotBlank()) {
            garmentRepo.insertGarment((Garment(0, newGarmentName, LocalDateTime.now())))
            newGarmentName = ""
        }
    }
}
