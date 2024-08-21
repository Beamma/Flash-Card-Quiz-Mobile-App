package nz.ac.canterbury.seng303.lab2.screens

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.lab2.models.FlashCard
import nz.ac.canterbury.seng303.lab2.viewmodels.FlashRepository
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun FlashCardList(navController: NavController, flashRepository: FlashRepository) {
    flashRepository.getFlashCards()
    val flashCards: List<FlashCard> by flashRepository.flashCards.collectAsState(emptyList())
    if (flashCards.isEmpty()) {
        val padding = 16.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "No Flash Cards Try Creating One",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(bottom = padding)
            )
            Button(
                onClick = { navController.navigate("CreateFlashCard") },
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Create Flash Card")
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(flashCards) { flashCard ->
                    FlashCardItem(navController = navController, flashCard = flashCard, flashRepository = flashRepository)
                }

                item {
                    Button(
                        onClick = {
                            navController.navigate("CreateFlashCard") // Navigate to create flash card screen
                        },
                        modifier = Modifier
                            .align(Alignment.BottomCenter) // Center horizontally and align to the bottom
                            .padding(16.dp) // Padding around the button
                            .fillMaxWidth() // Make the button fill the width
                            .padding(horizontal = 16.dp) // Horizontal padding to ensure it's not touching the screen edges
                    ) {
                        Text(text = "Create New Flash Card")
                    }
                }
            }
        }
    }
}

@Composable
fun FlashCardItem(navController: NavController, flashCard: FlashCard, flashRepository: FlashRepository) {
    val context = LocalContext.current

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { navController.navigate("FlashCard/${flashCard.id}") },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp), // Adjust elevation as needed
        shape = MaterialTheme.shapes.medium // Customize the shape if desired
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = flashCard.title,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.weight(1f),
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            }

//            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        val query = flashCard.title
                        if (query.isNotEmpty()) {
                            val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
                                putExtra(SearchManager.QUERY, query)
                            }
                            context.startActivity(intent)
                        } else {
                            Toast.makeText(context, "Question cannot be empty", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.Blue
                    )
                }
                IconButton(onClick = {
                    navController.navigate("FlashCard/${flashCard.id}")
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Edit",
                        tint = Color.Black
                    )
                }
//                Spacer(modifier = Modifier.width(16.dp))
                IconButton(onClick = {
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("Delete note \"${flashCard.title}\"?")
                        .setCancelable(false)
                        .setPositiveButton("Delete") { dialog, id ->
                            flashRepository.deleteNoteById(flashCard.id)
                            dialog.dismiss()
                        }
                        .setNegativeButton("Cancel") { dialog, id ->
                            dialog.dismiss()
                        }
                    val alert = builder.create()
                    alert.show()
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}
