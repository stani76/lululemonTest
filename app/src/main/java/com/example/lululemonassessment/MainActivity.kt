package com.example.lululemonassessment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lululemonassessment.database.IGarmentRepository
import com.example.lululemonassessment.models.Garment
import com.example.lululemonassessment.models.Screens
import com.example.lululemonassessment.ui.theme.LululemonAssessmentTheme
import com.example.lululemonassessment.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: MainViewModel by viewModels()
        setContent {
            LululemonAssessmentTheme {
                MainView(viewModel)
            }
        }
    }
}

@Composable
fun MainView(viewModel: MainViewModel) {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { TopBarText(viewModel.currentScreen.toString()) },
                actions = {
                    IconButton(
                        onClick = {
                            // Plus clicked
                            if (viewModel.currentScreen == Screens.List) {
                                navController.navigate(Screens.Add.toString())
                                viewModel.currentScreen = Screens.Add
                            } else {
                                // Save clicked
                                viewModel.addNewGarment()
                                navController.navigate(Screens.List.toString())
                                navController.clearBackStack(Screens.List.toString())
                                viewModel.currentScreen = Screens.List
                            }
                        },
                    ) {
                        if (viewModel.currentScreen == Screens.Add)
                            Text("Save")
                        else {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add garment")
                        }
                    }
                },
                backgroundColor = MaterialTheme.colors.primary
            )
        },

        content = {
            NavHost(navController, startDestination = Screens.List.toString()) {
                composable(Screens.List.toString()) { HomeScreen(viewModel) }
                composable(Screens.Add.toString()) { AddScreen(viewModel) }
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LululemonAssessmentTheme {
        MainView(
            MainViewModel(object : IGarmentRepository {
                override fun allGarments(): Flow<List<Garment>> = flow { emit(listOf()) }
                override fun insertGarment(garment: Garment) {}
            })
        )
    }
}
