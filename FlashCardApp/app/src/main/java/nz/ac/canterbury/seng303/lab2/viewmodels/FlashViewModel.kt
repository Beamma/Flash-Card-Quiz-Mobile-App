package nz.ac.canterbury.seng303.lab2.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import nz.ac.canterbury.seng303.lab2.models.FlashCard

class FlashViewModel : ViewModel() {
    var title by mutableStateOf("")
        private set

    var answers by mutableStateOf(listOf("", ""))
        private set

    var correctAnswerIndex by mutableStateOf(-1)
        private set

    fun updateTitle(newTitle: String) {
        title = newTitle
    }

    fun updateAnswers(newContent: String, index: Int) {
        answers = answers.toMutableList().also {
            it[index] = newContent
        }
    }

    fun resetViewModel() {
        answers = listOf("", "")
        title = ""
        correctAnswerIndex = -1
    }

    fun addAnswers(newContent: String) {
        answers = answers + newContent
    }

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

    fun isCorrectAnswer(index: Int): Boolean {
        return (index == correctAnswerIndex)
    }

    fun setDefaultValues(selectedFlashCard: FlashCard?) {
        selectedFlashCard?.let {
            title = it.title
            answers = it.answers
            correctAnswerIndex = it.correctAnswerIndex
        }
    }

}