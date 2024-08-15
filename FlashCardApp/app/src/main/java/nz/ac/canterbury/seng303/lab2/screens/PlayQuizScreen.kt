package nz.ac.canterbury.seng303.lab2.screens

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
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.lab2.models.FlashCard

import nz.ac.canterbury.seng303.lab2.viewmodels.FlashRepository

@Composable
fun PlayQuizScreen(navController: NavController, flashRepository: FlashRepository) {
    // Retrieve flashcards from the repository
    flashRepository.getFlashCards()
    val flashCards: List<FlashCard> by flashRepository.flashCards.collectAsState(emptyList())
    var currentIndex by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var isAnswerCorrect by remember { mutableStateOf<Boolean?>(null) }
    var showSummary by remember { mutableStateOf(false) }
    var userAnswers by remember { mutableStateOf(mutableListOf<Pair<String, Boolean>>()) }


    // Launch a coroutine to fetch flashcards
    LaunchedEffect(Unit) {
        flashRepository.getFlashCards()
    }

    LaunchedEffect(selectedAnswer) {
        if (selectedAnswer != null && currentIndex < flashCards.size) {
            delay(1000) // Wait for 1 second
            currentIndex++
            selectedAnswer = null
            isAnswerCorrect = null
            // Check if we've reached the end of the flashcards
            if (currentIndex >= flashCards.size) {
                showSummary = true
            }
        }
    }

    // Display the current flashcard
    if (!showSummary) {
        val currentFlashcard = flashCards.getOrNull(currentIndex)
        currentFlashcard?.let {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = it.title)
                Spacer(modifier = Modifier.height(16.dp))

                it.answers.forEachIndexed {index,  answer ->
                    Button(
                        onClick = {
                            selectedAnswer = answer
                            isAnswerCorrect = (index == it.correctAnswerIndex)
                            userAnswers.add(answer to isAnswerCorrect!!)
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