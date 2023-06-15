package com.sdevprem.mynotes.ui.notes.editor

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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteEditorViewModel @Inject constructor(
    private val notesRepository: NoteRepository
) : ViewModel() {

    private val _networkResult: MutableStateFlow<NetworkResult<*>> =
        MutableStateFlow(NetworkResult.Idle)
    val networkResult = _networkResult.toReadOnly()

    fun createNote(noteRequest: Note) {
        viewModelScope.launch {
            notesRepository.createNote(noteRequest)
                .onEach { _networkResult.value = it }
                .launchIn(viewModelScope)
        }
    }

    fun updateNote(id: Int, noteRequest: Note) {
        viewModelScope.launch {
            notesRepository.updateNote(id, noteRequest)
                .onEach { _networkResult.value = it }
                .launchIn(viewModelScope)
        }
    }

    fun deleteNote(noteId: Int) {
        viewModelScope.launch {
            notesRepository.deleteNote(noteId)
                .onEach { _networkResult.value = it }
                .launchIn(viewModelScope)
        }
    }

    fun resetNetworkResult() {
        _networkResult.value = NetworkResult.Idle
    }
}