package nz.ac.canterbury.seng303.lab2.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

import nz.ac.canterbury.seng303.lab2.viewmodels.QuizViewModel

@Composable
fun PlayQuizScreen(navController: NavController, quizViewModel: QuizViewModel = viewModel()) {

    // Observe ViewModel state
    val flashCards by quizViewModel.flashCards.collectAsState()
    val currentIndex by quizViewModel.currentIndex.collectAsState()
    val selectedAnswer by quizViewModel.selectedAnswer.collectAsState()
    val isAnswerCorrect by quizViewModel.isAnswerCorrect.collectAsState()
    val showSummary by quizViewModel.showSummary.collectAsState()
    val userAnswers by quizViewModel.userAnswers.collectAsState()

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
    } else if (!showSummary) {
        val currentFlashcard = flashCards.getOrNull(currentIndex)
        currentFlashcard?.let {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Question ${currentIndex + 1} of ${flashCards.size}",
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(text = it.title)
                Spacer(modifier = Modifier.height(16.dp))

                it.answers.forEachIndexed { index, answer ->
                    Button(
                        onClick = {
                            quizViewModel.onAnswerSelected(answer, it.correctAnswerIndex, index)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when {
                                selectedAnswer == answer && isAnswerCorrect == true -> Color.Green
                                selectedAnswer == answer && isAnswerCorrect == false -> Color.Red
                                else -> Color.Gray
                            }
                        )
                    ) {
                        Text(text = answer)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    } else {
        // Display the summary of the user's performance
        val correctAnswers = userAnswers.count { it.second }
        val totalQuestions = flashCards.size
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Quiz Completed!")
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "You got $correctAnswers out of $totalQuestions correct!")
        }
    }
}