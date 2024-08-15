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
}

