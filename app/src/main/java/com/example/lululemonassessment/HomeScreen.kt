package com.example.lululemonassessment

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.lululemonassessment.models.Garment
import com.example.lululemonassessment.viewmodels.MainViewModel

@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val garmentList by viewModel.garmentList.observeAsState(initial = listOf())
    val alphaSort = remember { mutableStateOf(viewModel.currentSort.value == MainViewModel.SortOrder.Alpha) }
    val creationSort = remember { mutableStateOf(viewModel.currentSort.value == MainViewModel.SortOrder.Creation) }
    Column {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(Modifier.weight(1f)) { RoundCornerButton("Alpha", 50.dp, 0.dp, 0.dp, 50.dp, alphaSort, creationSort) { viewModel.changeSort(MainViewModel.SortOrder.Alpha) } }
            Column(Modifier.weight(1f)) { RoundCornerButton("Creation Date", 0.dp, 50.dp, 50.dp, 0.dp, creationSort, alphaSort) { viewModel.changeSort(MainViewModel.SortOrder.Creation) } }
        }
        Row(modifier = Modifier.padding(16.dp)) {
            if (garmentList.isEmpty()) {
                Text("Loading...")
            } else {

                Box(Modifier.border(1.dp, Color.Black, RectangleShape)) {
                    ItemList(garmentList)
                }
            }
        }
    }
}

@Composable
fun RoundCornerButton(text: String, topStart: Dp, topEnd: Dp, bottomEnd: Dp, bottomStart: Dp, self: MutableState<Boolean>, other: MutableState<Boolean>, sortRoutine: () -> Unit) {
    Button(
        colors = ButtonDefaults.buttonColors(backgroundColor = if (self.value) MaterialTheme.colors.primary else MaterialTheme.colors.secondary),
        onClick = {
            if (!self.value) {
                self.value = !self.value
                other.value = !self.value
            }
            sortRoutine()
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart, topEnd, bottomEnd, bottomStart)
    ) {
        Text(text)
    }
}

@Composable
fun TopBarText(title: String) {
    Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = title)
}

@Composable
fun ItemList(itemList: List<Garment>) {

    val listModifier = Modifier
        .padding(10.dp)

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp),
        modifier = listModifier
    ) {
        itemsIndexed(itemList) { index, item ->

            Row(
                Modifier
                    .padding(0.dp, 0.dp, 0.dp, 0.dp)
                    .fillMaxSize()
            ) { Text(item.title) }

            if (index < itemList.lastIndex)
                Divider(color = Color.Black)
        }
    }
}
