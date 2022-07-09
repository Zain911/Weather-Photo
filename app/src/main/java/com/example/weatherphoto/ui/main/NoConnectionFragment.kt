package com.example.weatherphoto.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.weatherphoto.databinding.FragmentConnectionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoConnectionFragment : Fragment() {


    private var _binding: FragmentConnectionBinding? = null
    private val binding get() = _binding!!


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentConnectionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finish()
                }
            })
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}