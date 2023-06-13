package com.sdevprem.mynotes.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sdevprem.mynotes.LoginFragmentDirections
import com.sdevprem.mynotes.R
import com.sdevprem.mynotes.databinding.FragmentLoginBinding

class LoginFragment : Fragment(R.layout.fragment_register) {
    lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentLoginBinding.inflate(
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
                    LoginFragmentDirections.actionLoginFragmentToNotesFragment()
                )
        }

        btnSignUp.setOnClickListener{
            findNavController()
                .navigate(
                    LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                )
        }
    }
}