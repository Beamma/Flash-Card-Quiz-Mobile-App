package nz.ac.canterbury.seng303.lab2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import nz.ac.canterbury.seng303.lab2.viewmodels.FlashRepository
import nz.ac.canterbury.seng303.lab2.viewmodels.QuizViewModel

class QuizViewModelFactory(private val flashRepository: FlashRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            return QuizViewModel(flashRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}