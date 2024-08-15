package nz.ac.canterbury.seng303.lab2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import nz.ac.canterbury.seng303.lab2.screens.CreateFlashCard
//import nz.ac.canterbury.seng303.lab2.screens.NoteCard
//import nz.ac.canterbury.seng303.lab2.screens.NoteGrid
//import nz.ac.canterbury.seng303.lab2.screens.NoteList
import nz.ac.canterbury.seng303.lab2.ui.theme.Lab1Theme
import nz.ac.canterbury.seng303.lab2.viewmodels.EditNoteViewModel
import nz.ac.canterbury.seng303.lab2.viewmodels.FlashViewModel
import nz.ac.canterbury.seng303.lab2.viewmodels.FlashRepository
import org.koin.androidx.viewmodel.ext.android.viewModel as koinViewModel

class MainActivity : ComponentActivity() {

    private val flashRepository: FlashRepository by koinViewModel()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        noteViewModel.loadDefaultNotesIfNoneExist()

        setContent {
            Lab1Theme {
                val navController = rememberNavController()
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = currentBackStackEntry?.destination
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Joels Flash Card App") },
                            navigationIcon = if (currentDestination?.route != "Home") {
                                {
                                    IconButton(onClick = { navController.popBackStack() }) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowBack,
                                            contentDescription = "Back"
                                        )
                                    }
                                }
                            } else {
                                {}
                            }
                        )
                    }
                ) {

                    Box(modifier = Modifier.padding(it)) {
                        val flashViewModel: FlashViewModel = viewModel()
//                        val editNoteViewModel: EditNoteViewModel = viewModel()
                        NavHost(navController = navController, startDestination = "Home") {
                            composable("Home") {
                                Home(navController = navController)
                            }
//                            composable(
//                                "NoteCard/{noteId}",
//                                arguments = listOf(navArgument("noteId") {
//                                    type = NavType.StringType
//                                })
//                            ) { backStackEntry ->
//                                val noteId = backStackEntry.arguments?.getString("noteId")
//                                noteId?.let { noteIdParam: String -> NoteCard(noteIdParam, flashRepository)
//                            }}
//                            composable("EditNote/{noteId}", arguments = listOf(navArgument("noteId") {
//                                type = NavType.StringType
//                            })
//                            ) { backStackEntry ->
//                                val noteId = backStackEntry.arguments?.getString("noteId")
//                                noteId?.let { noteIdParam: String -> EditNote(noteIdParam, editNoteViewModel, flashRepository, navController = navController)}
//                            }
//                            composable("NoteList") {
//                                NoteList(navController, flashRepository)
//                            }
//                            composable("NoteGrid") {
//                                NoteGrid(navController, flashRepository)
//                            }
                            composable("CreateFlashCard") {
                                CreateFlashCard(navController = navController, flashViewModel = flashViewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun Home(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to Flash Card App")
        Button(onClick = { navController.navigate("CreateFlashCard") }) {
            Text("Create Flash Card")
        }
//        Button(onClick = { navController.navigate("NoteList") }) {
//            Text("View Flash Cards")
//        }
//        Button(onClick = { navController.navigate("NoteGrid") }) {
//            Text("Play Flash Cards")
//        }
    }
}
