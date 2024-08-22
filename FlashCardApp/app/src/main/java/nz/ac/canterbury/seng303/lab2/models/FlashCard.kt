package nz.ac.canterbury.seng303.lab2.models

class FlashCard (
    val id: Int,
    val title: String,
    val answers: List<String>,
    val correctAnswerIndex: Int
    ): Identifiable {

    override fun getIdentifier(): Int {
        return id;
    }

    fun getShuffledFlashCard(): FlashCard {
        // Shuffle the answers and keep track of the new correct index
        val shuffledAnswers = answers.shuffled()
        val newCorrectAnswerIndex = shuffledAnswers.indexOf(answers[correctAnswerIndex])

        return FlashCard(
            id = this.id,
            title = this.title,
            answers = shuffledAnswers,
            correctAnswerIndex = newCorrectAnswerIndex
        )
    }
}

