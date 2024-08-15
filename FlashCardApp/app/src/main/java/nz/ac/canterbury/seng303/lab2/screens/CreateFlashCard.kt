package nz.ac.canterbury.seng303.lab2.screens

import android.app.AlertDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.lab2.viewmodels.FlashViewModel
import nz.ac.canterbury.seng303.lab2.viewmodels.NoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateFlashCard(
    navController: NavController,
    flashViewModel: FlashViewModel,
) {
    val context = LocalContext.current
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
                OutlinedTextField(
                    value = answer,
                    onValueChange = { flashViewModel.updateAnswers(it, index) },
                    label = { Text("Answer ${index + 1}") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(start = 8.dp)
                )
                RadioButton(
                    selected = flashViewModel.correctAnswerIndex == index,
                    onClick = { flashViewModel.setCorrectAnswer(index) }
                )
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
//                flashViewModel.createNote(createFlashViewModel.title, "")
//                val builder = AlertDialog.Builder(context)
//                builder.setMessage("Created note!")
//                    .setCancelable(false)
//                    .setPositiveButton("Ok") { dialog, id ->
//                        createFlashViewModel.updateTitle("")
//                        navController.navigate("noteList")
//                    }
//                    .setNegativeButton("Cancel") { dialog, id -> dialog.dismiss() }
//                val alert = builder.create()
//                alert.show()

            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "Save")
        }
    }
}
