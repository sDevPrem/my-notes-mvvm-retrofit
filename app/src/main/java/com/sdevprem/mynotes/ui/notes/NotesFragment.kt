package com.sdevprem.mynotes.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.sdevprem.mynotes.R
import com.sdevprem.mynotes.databinding.FragmentNotesBinding
import com.sdevprem.mynotes.utils.NetworkResult
import com.sdevprem.mynotes.utils.launchInLifeCycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class NotesFragment : Fragment(R.layout.fragment_notes) {

    companion object {
        const val SHOULD_RELOAD_NOTE_LIST = "should_reload_note_list"
    }

    private lateinit var binding: FragmentNotesBinding
    private val viewModel by viewModels<NotesViewModel>()
    private val adapter: NoteAdapter = NoteAdapter {
        val noteJson = Gson().toJson(it)
        findNavController().navigate(
            NotesFragmentDirections
                .actionNotesFragmentToNoteEditorFragment(noteJson)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentNotesBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        noteList.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        noteList.adapter = adapter
        addNote.setOnClickListener {
            findNavController().navigate(
                NotesFragmentDirections
                    .actionNotesFragmentToNoteEditorFragment(null)
            )
        }

        launchInLifeCycle {
            findNavController().currentBackStackEntry?.savedStateHandle?.getStateFlow(
                SHOULD_RELOAD_NOTE_LIST,
                false
            )?.collectLatest {
                if (it) {
                    viewModel.fetchNotes(true)
                    findNavController().currentBackStackEntry?.savedStateHandle?.set(
                        SHOULD_RELOAD_NOTE_LIST,
                        false //to not reload force fully on every lifecycle state
                    )
                }
            }
        }

        viewModel.fetchNotes()

        launchInLifeCycle {
            viewModel.notes.collectLatest {
                when (it) {
                    is NetworkResult.Error -> {
                        progressBar.isVisible = false
                        Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()
                    }

                    NetworkResult.Idle -> {}
                    NetworkResult.Loading -> {
                        noteList.isVisible = false
                        progressBar.isVisible = true
                    }

                    is NetworkResult.Success -> {
                        progressBar.isVisible = false
                        noteList.isVisible = true
                        adapter.submitList(it.data)
                    }
                }
            }
        }
    }
}