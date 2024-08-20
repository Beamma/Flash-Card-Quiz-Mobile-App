package nz.ac.canterbury.seng303.lab2

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import nz.ac.canterbury.seng303.lab2.screens.CreateFlashCard
import nz.ac.canterbury.seng303.lab2.screens.EditFlashCard
import nz.ac.canterbury.seng303.lab2.screens.FlashCardList
import nz.ac.canterbury.seng303.lab2.screens.PlayQuizScreen
import nz.ac.canterbury.seng303.lab2.ui.theme.Lab1Theme
import nz.ac.canterbury.seng303.lab2.viewmodels.FlashRepository
import nz.ac.canterbury.seng303.lab2.viewmodels.FlashViewModel
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
                            },
                            colors = TopAppBarDefaults.smallTopAppBarColors(
                                containerColor = Color.Red, // Background color for the TopAppBar
                                titleContentColor = Color.White, // Title text color
                                navigationIconContentColor = Color.White // Icon color
                            )
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
                            composable("FlashCard/{noteId}", arguments = listOf(navArgument("noteId") {
                                type = NavType.StringType
                            })
                            ) { backStackEntry ->
                                val noteId = backStackEntry.arguments?.getString("noteId")
                                noteId?.let { noteIdParam: String -> EditFlashCard(navController = navController, noteIdParam, flashViewModel, flashRepository)}
                            }
                            composable("FlashCardList") {
                                FlashCardList(navController, flashRepository)
                            }
                            composable("Play") {
                                PlayQuizScreen(navController, flashRepository)
                            }
                            composable("CreateFlashCard") {
                                CreateFlashCard(navController = navController, flashViewModel = flashViewModel, flashRepository = flashRepository)
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
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isPortrait) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "App Icon",
                    modifier = Modifier.size(200.dp),
                    colorFilter = ColorFilter.tint(Color.Red)
                )
            }
            Button(
                onClick = { navController.navigate("CreateFlashCard") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Create Flash Card", style = TextStyle(fontSize = 20.sp))
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Button(
                onClick = { navController.navigate("FlashCardList") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("View Flash Cards", style = TextStyle(fontSize = 20.sp))
                    Icon(
                        imageVector = Icons.Filled.Visibility,
                        contentDescription = "View",
                        modifier = Modifier.size(24.dp)
                    )
                }

            }

            Button(
                onClick = { navController.navigate("Play") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Play Flash Cards", style = TextStyle(fontSize = 20.sp))
                    Icon(
                        imageVector = Icons.Filled.Lightbulb,
                        contentDescription = "View",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
