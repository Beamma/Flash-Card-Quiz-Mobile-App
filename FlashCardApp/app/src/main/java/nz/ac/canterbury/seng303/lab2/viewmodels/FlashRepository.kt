package nz.ac.canterbury.seng303.lab2.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng303.lab2.datastore.Storage
import nz.ac.canterbury.seng303.lab2.models.FlashCard
import nz.ac.canterbury.seng303.lab2.models.Note
import kotlin.random.Random

class FlashRepository(
    private val flashStorage: Storage<FlashCard>
) : ViewModel() {

    private val _notes = MutableStateFlow<List<FlashCard>>(emptyList())

    private val _selectedFlashCard = MutableStateFlow<FlashCard?>(null)
    val selectedFlashCard: StateFlow<FlashCard?> = _selectedFlashCard

    fun getNotes() = viewModelScope.launch {
        flashStorage.getAll().catch { Log.e("NOTE_VIEW_MODEL", it.toString()) }
            .collect { _notes.emit(it) }
    }

//    fun loadDefaultNotesIfNoneExist() = viewModelScope.launch {
//        val currentNotes = noteStorage.getAll().first()
//        if (currentNotes.isEmpty()) {
//            Log.d("NOTE_VIEW_MODEL", "Inserting default notes...")
//            noteStorage.insertAll(Note.getNotes())
//                .catch { Log.w("NOTE_VIEW_MODEL", "Could not insert default notes") }.collect {
//                Log.d("NOTE_VIEW_MODEL", "Default notes inserted successfully")
//                _notes.emit(Note.getNotes())
//            }
//        }
//    }

    fun createFlashCard(title: String, answers: List<String>, correctAnswerIndex: Int) = viewModelScope.launch {
        val flashCard = FlashCard(
            id = Random.nextInt(0, Int.MAX_VALUE),
            title = title,
            answers = answers,
            correctAnswerIndex = correctAnswerIndex
        )
        flashStorage.insert(flashCard).catch { Log.e("NOTE_VIEW_MODEL", "Could not insert note") }
            .collect()
        flashStorage.getAll().catch { Log.e("NOTE_VIEW_MODEL", it.toString()) }
            .collect { _notes.emit(it) }
    }

    fun getNoteById(noteId: Int?) = viewModelScope.launch {
        if (noteId != null) {
            _selectedFlashCard.value = flashStorage.get { it.getIdentifier() == noteId }.first()
        } else {
            _selectedFlashCard.value = null
        }
    }

    fun deleteNoteById(flashCardId: Int?) = viewModelScope.launch {
        Log.d("NOTE_VIEW_MODEL", "Deleting note: $flashCardId")
        if (flashCardId != null) {
            flashStorage.delete(flashCardId).collect()
            flashStorage.getAll().catch { Log.e("NOTE_VIEW_MODEL", it.toString()) }
                .collect { _notes.emit(it) }
        }
    }

    fun editNoteById(flashCardId: Int?, flashCard: FlashCard) = viewModelScope.launch {
        Log.d("NOTE_VIEW_MODEL", "Editing note: $flashCardId")
        if (flashCardId != null) {
            flashStorage.edit(flashCardId, flashCard).collect()
            flashStorage.getAll().catch { Log.e("NOTE_VIEW_MODEL", it.toString()) }
                .collect { _notes.emit(it) }
        }
    }
}