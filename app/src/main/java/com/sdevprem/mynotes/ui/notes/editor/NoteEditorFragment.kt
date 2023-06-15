package com.sdevprem.mynotes.ui.notes.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.sdevprem.mynotes.data.model.notes.Note
import com.sdevprem.mynotes.databinding.FragmentNoteEditorBinding
import com.sdevprem.mynotes.ui.notes.NotesFragment
import com.sdevprem.mynotes.utils.NetworkResult
import com.sdevprem.mynotes.utils.launchInLifeCycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class NoteEditorFragment : Fragment() {

    private lateinit var binding: FragmentNoteEditorBinding
    private val args by navArgs<NoteEditorFragmentArgs>()
    private var note: Note? = null
    private val viewModel by viewModels<NoteEditorViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentNoteEditorBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {

        parseJsonNote(args.note)
        launchInLifeCycle {
            viewModel.networkResult.collectLatest {
                when (it) {
                    is NetworkResult.Error -> {
                        progressBar.isVisible = false
                        Toast.makeText(
                            requireContext(),
                            it.msg,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    NetworkResult.Idle -> {
                        progressBar.isVisible = false
                    }

                    NetworkResult.Loading -> {
                        progressBar.isVisible = true
                    }

                    is NetworkResult.Success -> {
                        findNavController().previousBackStackEntry?.savedStateHandle?.set(
                            NotesFragment.SHOULD_RELOAD_NOTE_LIST,
                            true
                        )
                        findNavController().popBackStack()
                    }
                }
            }
            viewModel.resetNetworkResult()
        }

        return@with
    }

    private fun parseJsonNote(jsonNote: String?) = with(binding) {
        jsonNote?.let {
            Gson().fromJson(it, Note::class.java).let { note ->
                this@NoteEditorFragment.note = note
                txtTitle.setText(note.title)
                txtDescription.setText(note.description)
                btnDelete.setOnClickListener {
                    viewModel.deleteNote(note.id)
                }
                btnSubmit.setOnClickListener {
                    viewModel.updateNote(
                        note.id, Note(
                            title = txtTitle.text.toString(),
                            description = txtDescription.text.toString()
                        )
                    )
                }
            }
        } ?: run {
            addEditText.text = "Add Note"
            btnDelete.isVisible = false
            btnSubmit.setOnClickListener {
                viewModel.createNote(
                    Note(
                        title = txtTitle.text.toString(),
                        description = txtDescription.text.toString()
                    )
                )
            }
        }
    }
}