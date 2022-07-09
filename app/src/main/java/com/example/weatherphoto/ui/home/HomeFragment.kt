package com.example.weatherphoto.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherphoto.R
import com.example.weatherphoto.data.model.ImageItem
import com.example.weatherphoto.databinding.FragmentHomeBinding
import com.example.weatherphoto.domain.util.Util
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private lateinit var imagesAdapter: ImagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.openCameraFloatingActionButton.setOnClickListener {
            if (Util.checkPermissions(requireContext())) {
                findNavController().navigate(R.id.cameraFragment)
            } else {
                Util.requestPermissions(requireActivity())
                Toast.makeText(requireContext(), "Permissions denied", Toast.LENGTH_SHORT).show()
            }
        }

        imagesAdapter = ImagesAdapter(arrayListOf()) {
            findNavController().navigate(
                HomeFragmentDirections.actionNavigationHomeToImagePreviewFragment(
                    it.imageUrl
                )
            )
        }

        binding.imagesRecyclerView.adapter = imagesAdapter
        binding.imagesRecyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )

        viewModel.imagesList.observe(viewLifecycleOwner) {
            imagesAdapter.changeList(it as MutableList<ImageItem>)
        }

        return root
    }

}