package com.example.lululemonassessment
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import com.example.lululemonassessment.database.GarmentRepository
import com.example.lululemonassessment.viewmodels.MainViewModel

@Composable
fun AddScreen(viewModel: MainViewModel) {
    val focusRequester = remember { FocusRequester() }
    TextField(
        value = viewModel.newGarmentName.value,
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        onValueChange = { viewModel.newGarmentName.value = it },
        label = { Text("Garment Name:") }
    )
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun AddScreenPreview() {
    AddScreen(MainViewModel(GarmentRepository(LuluApplication())))
}
