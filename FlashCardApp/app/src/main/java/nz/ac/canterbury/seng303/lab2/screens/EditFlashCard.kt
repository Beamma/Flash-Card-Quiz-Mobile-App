package nz.ac.canterbury.seng303.lab2.screens

import android.app.AlertDialog
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.lab2.models.FlashCard
import nz.ac.canterbury.seng303.lab2.viewmodels.FlashRepository
import nz.ac.canterbury.seng303.lab2.viewmodels.FlashViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFlashCard(
    navController: NavController,
    noteId: String,
    flashViewModel: FlashViewModel,
    flashRepository: FlashRepository
) {
    val context = LocalContext.current
    val selectedFlashCardState by flashRepository.selectedFlashCard.collectAsState(null)
    val flashCard: FlashCard? = selectedFlashCardState // we explicitly assign to note to help the compilers smart cast out

    LaunchedEffect(flashCard) {  // Get the default values for the note properties
        if (flashCard == null) {
            flashRepository.getNoteById(noteId.toIntOrNull())
        } else {
            flashViewModel.setDefaultValues(flashCard)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        OutlinedTextField(
            value = flashViewModel.title,
            onValueChange = { flashViewModel.updateTitle(it) },
            label = { Text("Question") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // Display answer fields
        flashViewModel.answers.forEachIndexed { index, answer ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                OutlinedTextField(
                    value = answer,
                    onValueChange = { flashViewModel.updateAnswers(it, index) },
                    label = { Text("Answer ${index + 1}") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(start = 8.dp)
                )
                Checkbox(
                    checked = flashViewModel.isCorrectAnswer(index),
                    onCheckedChange = { isChecked ->
                        flashViewModel.setCorrectAnswer(index)
                    }
                )
                if (flashViewModel.answers.size > 2) {
                    IconButton(
                        onClick = {
                            flashViewModel.removeAnswer(index)
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove Answer"
                        )
                    }
                }
            }
        }

        // Button to add more answer fields
        Button(
            onClick = { flashViewModel.addAnswers("") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Add Answer Field")
        }

        Button(
            onClick = {
                when {
                    flashViewModel.title.isEmpty() -> {
                        Toast.makeText(context, "Question cannot be empty", Toast.LENGTH_SHORT).show()
                    }
                    flashViewModel.answers.isEmpty() -> {
                        Toast.makeText(context, "All answers must be filled", Toast.LENGTH_SHORT).show()
                    }
                    flashViewModel.correctAnswerIndex == -1 -> {
                        Toast.makeText(context, "Please select at least one correct answer", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        flashRepository.createFlashCard(flashViewModel.title, flashViewModel.answers, flashViewModel.correctAnswerIndex)
                        val builder = AlertDialog.Builder(context)
                        builder.setMessage("Created note!")
                            .setCancelable(false)
                            .setPositiveButton("Ok") { dialog, id ->
                                flashViewModel.resetViewModel()
                                navController.navigate("Home")
                            }
                            .setNegativeButton("Cancel") { dialog, id -> dialog.dismiss() }
                        val alert = builder.create()
                        alert.show()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "Save")
        }
    }
}
