package nz.ac.canterbury.seng303.lab2.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class FlashViewModel : ViewModel() {
    var title by mutableStateOf("")
        private set

    fun updateTitle(newTitle: String) {
        title = newTitle
    }

    var answers by mutableStateOf(listOf("", ""))
        private set

    fun updateAnswers(newContent: String, index: Int) {
        answers = answers.toMutableList().also {
            it[index] = newContent
        }
    }

    fun addAnswers(newContent: String) {
        answers = answers + newContent
    }

    var correctAnswerIndex by mutableStateOf(-1)
        private set

    fun setCorrectAnswer(index: Int) {
        correctAnswerIndex = index
    }

    fun removeAnswer(index: Int) {
        answers = answers.toMutableList().also {
            it.removeAt(index)
        }
        // Adjust correctAnswerIndex if needed
        if (correctAnswerIndex == index) {
            correctAnswerIndex = -1
        } else if (correctAnswerIndex > index) {
            correctAnswerIndex--
        }
    }


}