package nz.ac.canterbury.seng303.lab2.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
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
    val questionAnswers by quizViewModel.questionAnswers.collectAsState()
    val actualCorrectAnswers by quizViewModel.correctAnswers.collectAsState()

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Display current flashcard if available
            val currentFlashcard = flashCards.getOrNull(currentIndex)

            currentFlashcard?.let {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Display question number and title
                    Text(
                        text = "Question ${currentIndex + 1} of ${flashCards.size}",
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .fillMaxWidth()
                    )
                    Text(
                        text = it.title,
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .fillMaxWidth(),
                        style = TextStyle(
                                fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center)
                    )

                    // Display answer buttons
                    it.answers.forEachIndexed { index, answer ->
                        Button(
                            onClick = {
                                quizViewModel.onAnswerSelected(
                                    answer,
                                    it.correctAnswerIndex,
                                    index,
                                    it.title
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = when {
                                    selectedAnswer == answer && isAnswerCorrect == true -> Color.Green
                                    selectedAnswer == answer && isAnswerCorrect == false -> Color.Red
                                    selectedAnswer == answer -> Color.DarkGray
                                    else -> Color.LightGray
                                }
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text(text = answer)
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        quizViewModel.onSubmit()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(text = "Submit")
                }
            }
        }
    } else {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Quiz Completed!",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            val correctAnswers = userAnswers.count { it.second }
            val totalQuestions = flashCards.size
            Text(text = "You got $correctAnswers out of $totalQuestions correct!")
            Spacer(modifier = Modifier.height(16.dp))

            // Detailed answers summary
            Text(text = "Detailed Summary", style = TextStyle(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
            ))
            Spacer(modifier = Modifier.height(8.dp))

            questionAnswers.forEachIndexed { index, (answer, isCorrect) ->
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .border(
                            width = 2.dp, // Set the border width
                            color = if (isCorrect) Color.Green else Color.Red, // Green if correct, Red if incorrect
                            shape = MaterialTheme.shapes.medium // You can adjust the shape if needed
                        ),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = Color.LightGray
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        val answerText = if (isCorrect) {
                            "Correct"
                        } else {
                            "Incorrect"
                        }
                        Text(text = "${index + 1}: $answerText",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                            )
                        )
                        Divider(
                            color = if (isCorrect) Color.Green else Color.Red,
                            thickness = 2.dp,
                            modifier = Modifier.padding(horizontal = 16.dp)
                                .padding(4.dp)
                        )
                        Text(text = answer,
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        )
                        if (isCorrect) {
                            Text(
                                text = "You Correctly Answered: ${userAnswers[index].first}",
                                style = TextStyle(
                                    fontSize = 12.sp
                                )
                            )
                        } else {
                            Text(
                                text = "You Answered: ${userAnswers[index].first}",
                                style = TextStyle(
                                    fontSize = 12.sp
                                )
                            )
                            Text(
                                text = "Correct Answer: ${actualCorrectAnswers[index]}",
                                style = TextStyle(
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}