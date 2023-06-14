package com.sdevprem.mynotes.data.repository

import com.sdevprem.mynotes.data.api.NotesAPI
import com.sdevprem.mynotes.data.model.notes.Note
import com.sdevprem.mynotes.utils.NetworkResult
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

const val ERROR_MESSAGE = "Something went wrong"

class NoteRepository @Inject constructor(
    private val notesAPI: NotesAPI
) {

    fun getNotes() = sendRequest { notesAPI.getNotes() }

    fun createNote(note: Note) = sendRequest { notesAPI.createNote(note) }

    fun updateNote(noteId: Int, note: Note) =
        sendRequest { notesAPI.updateNote(noteId, note) }

    fun deleteNote(noteId: Int) =
        sendRequest { notesAPI.deleteNote(noteId) }

    private inline fun <T> sendRequest(crossinline request: suspend () -> Response<T>) = flow {
        try {
            handleResponse(request())
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: ERROR_MESSAGE))
        }
    }

    private suspend fun <T> FlowCollector<NetworkResult<T>>.handleResponse(response: Response<T>) {
        if (response.isSuccessful) {
            emit(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            emit(NetworkResult.Error(response.errorBody()!!.charStream().readText()))
        } else emit(NetworkResult.Error(ERROR_MESSAGE))
    }
}