package com.sdevprem.mynotes.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sdevprem.mynotes.data.repository.NoteRepository
import com.sdevprem.mynotes.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val notesRepository: NoteRepository
) : ViewModel() {

    val notes = notesRepository
        .getNotes()
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            NetworkResult.Idle
        )

}