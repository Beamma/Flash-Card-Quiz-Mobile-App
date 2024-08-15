package nz.ac.canterbury.seng303.lab2.screens

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Divider
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

@Composable
fun FlashCardList(navController: NavController, flashRepository: FlashRepository) {
    flashRepository.getFlashCards()
    val flashCards: List<FlashCard> by flashRepository.flashCards.collectAsState(emptyList())
    if (flashCards.isEmpty()) {
        Text(text = "No Flash Cards Try Creating One")
    } else {
        LazyColumn {
            items(flashCards) { flashCard ->
                FlashCardItem(navController = navController, flashCard = flashCard, flashRepository = flashRepository)
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), // Inner padding of the card
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Row to include search icon and title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.weight(3f)
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
                        contentDescription = "Search"
                    )
                }
                Spacer(modifier = Modifier.width(8.dp)) // Add some space between icon and text
                Text(
                    text = flashCard.title,
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Action Buttons Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.weight(1f)
            ) {
                IconButton(onClick = {
                    navController.navigate("FlashCard/${flashCard.id}")
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Edit",
                        tint = Color.Blue
                    )
                }
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