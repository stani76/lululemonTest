package com.example.lululemonassessment
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import com.example.lululemonassessment.database.IGarmentRepository
import com.example.lululemonassessment.models.Garment
import com.example.lululemonassessment.viewmodels.MainViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
fun AddScreen(viewModel: MainViewModel) {
    val focusRequester = remember { FocusRequester() }
    TextField(
        value = viewModel.newGarmentName,
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        onValueChange = { viewModel.newGarmentName = it },
        label = { Text("Garment Name:") }
    )
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Preview(showBackground = true)
@Composable
fun AddScreenPreview() {
    AddScreen(
        MainViewModel(object : IGarmentRepository {
            override fun allGarments(): Flow<List<Garment>> = flow { emit(listOf()) }
            override fun insertGarment(garment: Garment) {}
        })
    )
}
