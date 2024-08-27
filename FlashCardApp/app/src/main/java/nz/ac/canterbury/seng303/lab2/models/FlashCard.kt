package nz.ac.canterbury.seng303.lab2.models

class FlashCard (
    val id: Int,
    val title: String,
    val answers: List<String>,
    val correctAnswerIndex: Int,
    val isFlashCard: Boolean
    ): Identifiable {

    override fun getIdentifier(): Int {
        return id;
    }

    fun getShuffledFlashCard(): FlashCard {
        // Shuffle the answers and keep track of the new correct index
        var shuffledAnswers = answers
        var newCorrectAnswerIndex = correctAnswerIndex

        println(title)
        if (isFlashCard) {
            shuffledAnswers = answers.shuffled()
            newCorrectAnswerIndex = shuffledAnswers.indexOf(answers[correctAnswerIndex])
        }

        return FlashCard(
            id = this.id,
            title = this.title,
            answers = shuffledAnswers,
            correctAnswerIndex = newCorrectAnswerIndex,
            isFlashCard = this.isFlashCard
        )
    }
}

