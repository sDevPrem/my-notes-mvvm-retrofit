package com.sdevprem.mynotes.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sdevprem.mynotes.data.model.notes.Note
import com.sdevprem.mynotes.data.repository.NoteRepository
import com.sdevprem.mynotes.utils.NetworkResult
import com.sdevprem.mynotes.utils.toReadOnly
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val notesRepository: NoteRepository
) : ViewModel() {

    private val _notes = MutableStateFlow<NetworkResult<List<Note>>>(NetworkResult.Idle)
    val notes = _notes.toReadOnly()

    fun fetchNotes(force: Boolean = false) {
        if (force || _notes.value is NetworkResult.Idle)
            notesRepository.getNotes()
                .onEach { _notes.value = it }
                .launchIn(viewModelScope)
    }

}