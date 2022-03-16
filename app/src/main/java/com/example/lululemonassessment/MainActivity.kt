package com.example.lululemonassessment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lululemonassessment.database.GarmentRepository
import com.example.lululemonassessment.ui.theme.LululemonAssessmentTheme
import com.example.lululemonassessment.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: MainViewModel by viewModels()

        setContent {
            LaunchedEffect(Unit) {
                // Lame hack - This is a showstopper bug - something is going on with the DB and it's not loading the data in the right order so we have to delay for a sec.
                delay(500)
                viewModel.changeSort(MainViewModel.SortOrder.Alpha)
            }
            LululemonAssessmentTheme {
                MainView(viewModel)
            }
        }
    }
}

@Composable
fun MainView(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { TopBarText(viewModel.currentScreen.value.toString()) },
                actions = {
                    IconButton(
                        onClick = {
                            // Plus clicked
                            if (viewModel.currentScreen.value == MainViewModel.Screens.List) {
                                navController.navigate(MainViewModel.Screens.Add.toString())
                                viewModel.currentScreen.value = MainViewModel.Screens.Add
                                navController.clearBackStack(0)
                            } else {
                                // Save clicked
                                viewModel.addNewGarment()

                                navController.navigate(MainViewModel.Screens.List.toString())
                                viewModel.currentScreen.value = MainViewModel.Screens.List
                                navController.clearBackStack(0)
                                coroutineScope.launch {
                                    // Another Lame hack - This is a showstopper bug
                                    // Something is wrong with my implementation somewhere - data is not refreshing correctly from the database
                                    delay(500)
                                    viewModel.changeSort(viewModel.currentSort.value)
                                }
                            }
                        },
                    ) {
                        if (viewModel.currentScreen.value == MainViewModel.Screens.Add)
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
            NavHost(navController, startDestination = MainViewModel.Screens.List.toString()) {
                composable(MainViewModel.Screens.List.toString()) { HomeScreen(viewModel) }
                composable(MainViewModel.Screens.Add.toString()) { AddScreen(viewModel) }
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LululemonAssessmentTheme {
        MainView(MainViewModel(GarmentRepository(LuluApplication())))
    }
}
