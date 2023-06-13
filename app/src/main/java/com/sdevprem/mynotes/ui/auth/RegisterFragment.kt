package com.sdevprem.mynotes.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sdevprem.mynotes.R
import com.sdevprem.mynotes.RegisterFragmentDirections
import com.sdevprem.mynotes.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment(R.layout.fragment_register){
    private lateinit var binding : FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentRegisterBinding.inflate(
            LayoutInflater.from(requireContext()),
            container,
            false
        ).apply {
            binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        btnLogin.setOnClickListener{
            findNavController()
                .navigate(
                    RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                )
        }
        btnSignUp.setOnClickListener{
            findNavController()
                .navigate(
                    RegisterFragmentDirections.actionRegisterFragmentToNotesFragment()
                )
        }
    }
}