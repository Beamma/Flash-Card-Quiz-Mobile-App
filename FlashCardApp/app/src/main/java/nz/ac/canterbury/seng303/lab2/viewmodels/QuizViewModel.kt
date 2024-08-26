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

    private val _index = MutableStateFlow(-1)
    val index: StateFlow<Int> = _index

    private val _correctAnswerIndex = MutableStateFlow<Int?>(null)
    val correctAnswerIndex: StateFlow<Int?> = _correctAnswerIndex

    private val _selectedAnswer = MutableStateFlow<String?>(null)
    val selectedAnswer: StateFlow<String?> = _selectedAnswer

    private val _question = MutableStateFlow<String?>(null)
    val question: StateFlow<String?> = _question

    private val _isAnswerCorrect = MutableStateFlow<Boolean?>(null)
    val isAnswerCorrect: StateFlow<Boolean?> = _isAnswerCorrect

    private val _showSummary = MutableStateFlow(false)
    val showSummary: StateFlow<Boolean> = _showSummary

    private val _userAnswers = MutableStateFlow(mutableListOf<Pair<String, Boolean>>())
    val userAnswers: StateFlow<List<Pair<String, Boolean>>> = _userAnswers

    private val _questionAnswers = MutableStateFlow(mutableListOf<Pair<String, Boolean>>())
    val questionAnswers: StateFlow<List<Pair<String, Boolean>>> = _questionAnswers

    private val _correctAnswers = MutableStateFlow(mutableListOf<String>())
    val correctAnswers: StateFlow<List<String>> = _correctAnswers

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
        _correctAnswerIndex.value = correctAnswerIndex
        _index.value = index
        _question.value = question
    }

    fun onSubmit() {
        val selectedAnswer = _selectedAnswer.value
        val correctAnswerIndex = _correctAnswerIndex.value

        if (selectedAnswer != null && correctAnswerIndex != null) {
            val flashCard = _flashCards.value.getOrNull(_currentIndex.value)
            if (flashCard != null) {
                val isCorrect = flashCard.answers[correctAnswerIndex] == selectedAnswer
                _correctAnswers.value.add(flashCard.answers[correctAnswerIndex])
                _isAnswerCorrect.value = isCorrect
                _userAnswers.value.add(selectedAnswer to isCorrect)
                _questionAnswers.value.add((_question.value to isCorrect) as Pair<String, Boolean>)

                viewModelScope.launch {
                    delay(1000) // Wait for 1 second
                    if (_currentIndex.value < _flashCards.value.size - 1) {
                        _index.value = -1
                        _currentIndex.value += 1
                        _selectedAnswer.value = null
                        _isAnswerCorrect.value = null
                    } else {
                        _showSummary.value = true
                    }
                }
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
        _index.value = -1
        _correctAnswerIndex.value = null
        _question.value = null
        getFlashCards()
    }
}