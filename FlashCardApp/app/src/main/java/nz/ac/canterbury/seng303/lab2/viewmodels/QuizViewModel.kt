package nz.ac.canterbury.seng303.lab2.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng303.lab2.models.FlashCard

class QuizViewModel(private val flashRepository: FlashRepository) : ViewModel() {
    // State variables
    private val _flashCards = MutableStateFlow<List<FlashCard>>(emptyList())
    val flashCards: StateFlow<List<FlashCard>> = _flashCards

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    private val _selectedAnswer = MutableStateFlow<String?>(null)
    val selectedAnswer: StateFlow<String?> = _selectedAnswer

    private val _isAnswerCorrect = MutableStateFlow<Boolean?>(null)
    val isAnswerCorrect: StateFlow<Boolean?> = _isAnswerCorrect

    private val _showSummary = MutableStateFlow(false)
    val showSummary: StateFlow<Boolean> = _showSummary

    private val _userAnswers = MutableStateFlow(mutableListOf<Pair<String, Boolean>>())
    val userAnswers: StateFlow<List<Pair<String, Boolean>>> = _userAnswers

    private val _questionAnswers = MutableStateFlow(mutableListOf<Pair<String, Boolean>>())
    val questionAnswers: StateFlow<List<Pair<String, Boolean>>> = _questionAnswers

    init {
        getFlashCards()
    }

    fun getFlashCards() {
        flashRepository.getFlashCards()
        viewModelScope.launch {
            flashRepository.flashCards.collect { cards ->
                val shuffledCards = cards.map { it.getShuffledFlashCard() }
                _flashCards.value = shuffledCards.shuffled()
            }
        }

    }

    // Handle answer selection
    fun onAnswerSelected(answer: String, correctAnswerIndex: Int, index: Int, question: String) {
        _selectedAnswer.value = answer
        val isCorrect = (index == correctAnswerIndex)
        _isAnswerCorrect.value = isCorrect
        _userAnswers.value.add(answer to isCorrect)
        _questionAnswers.value.add(question to isCorrect)

        viewModelScope.launch {
            delay(1000)
            if (_currentIndex.value < _flashCards.value.size - 1) {
                _currentIndex.value += 1
                _selectedAnswer.value = null
                _isAnswerCorrect.value = null
            } else {
                _showSummary.value = true
            }
        }
    }

    fun resetViewModel() {
        _currentIndex.value = 0;
        _showSummary.value = false;
        _selectedAnswer.value = null
        _isAnswerCorrect.value = null
        _userAnswers.value = mutableListOf()
        _questionAnswers.value = mutableListOf()
        getFlashCards()
    }
}