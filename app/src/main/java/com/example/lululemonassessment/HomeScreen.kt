package com.example.lululemonassessment

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.lululemonassessment.database.IGarmentRepository
import com.example.lululemonassessment.models.Garment
import com.example.lululemonassessment.models.SortOrder
import com.example.lululemonassessment.ui.theme.LululemonAssessmentTheme
import com.example.lululemonassessment.viewmodels.MainViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val garmentList by viewModel.garmentList.collectAsState(initial = listOf())
    Column {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(Modifier.weight(1f)) { RoundCornerButton("Alpha", 50.dp, 0.dp, 0.dp, 50.dp, SortOrder.Alpha, viewModel) }
            Column(Modifier.weight(1f)) { RoundCornerButton("Creation Date", 0.dp, 50.dp, 50.dp, 0.dp, SortOrder.Creation, viewModel) }
        }
        Row(modifier = Modifier.padding(16.dp)) {
            if (garmentList.isEmpty()) {
                Text("")
            } else {

                Box(Modifier.border(1.dp, Color.Black, RectangleShape)) {
                    ItemList(garmentList)
                }
            }
        }
    }
}

@Composable
fun RoundCornerButton(text: String, topStart: Dp, topEnd: Dp, bottomEnd: Dp, bottomStart: Dp, sortOrder: SortOrder, viewModel: MainViewModel) {
    Button(
        colors = ButtonDefaults.buttonColors(backgroundColor = if (sortOrder == viewModel.currentSort) MaterialTheme.colors.primary else MaterialTheme.colors.secondary),
        onClick = {
            if (sortOrder != viewModel.currentSort) viewModel.currentSort = sortOrder
            viewModel.sortGarments()
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

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    LululemonAssessmentTheme {
        HomeScreen(
            MainViewModel(object : IGarmentRepository {
                override fun allGarments(): Flow<List<Garment>> = flow { emit(listOf()) }
                override fun insertGarment(garment: Garment) {}
            })
        )
    }
}
