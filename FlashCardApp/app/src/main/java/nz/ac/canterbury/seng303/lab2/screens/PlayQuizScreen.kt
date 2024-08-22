package nz.ac.canterbury.seng303.lab2.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.compose.material3.Typography
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.lab2.models.FlashCard

import nz.ac.canterbury.seng303.lab2.viewmodels.FlashRepository
import nz.ac.canterbury.seng303.lab2.viewmodels.QuizViewModel
import org.koin.core.logger.Logger

@Composable
fun PlayQuizScreen(navController: NavController, quizViewModel: QuizViewModel = viewModel()) {

    // Observe ViewModel state
    val flashCards by quizViewModel.flashCards.collectAsState()
    val currentIndex by quizViewModel.currentIndex.collectAsState()
    val selectedAnswer by quizViewModel.selectedAnswer.collectAsState()
    val isAnswerCorrect by quizViewModel.isAnswerCorrect.collectAsState()
    val showSummary by quizViewModel.showSummary.collectAsState()
    val userAnswers by quizViewModel.userAnswers.collectAsState()

    // Display the current flashcard or the summary
    if (!showSummary) {
        val currentFlashcard = flashCards.getOrNull(currentIndex)
        currentFlashcard?.let {
            Column(modifier = Modifier.padding(16.dp)) {
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