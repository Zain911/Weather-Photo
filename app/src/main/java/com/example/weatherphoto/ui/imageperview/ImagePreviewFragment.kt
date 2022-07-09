package com.example.weatherphoto.ui.imageperview

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.weatherphoto.R
import com.example.weatherphoto.databinding.FragmentImagePreviewBinding
import java.io.File

class ImagePreviewFragment : Fragment() {

    private val args: ImagePreviewFragmentArgs by navArgs()
    private var _binding: FragmentImagePreviewBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImagePreviewBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val imageBitmap = BitmapFactory.decodeFile(args.imageUrl)
        binding.imagePreviewImageView.setImageBitmap(imageBitmap)

        val file = File(args.imageUrl)
        val uri = FileProvider.getUriForFile(requireContext() ,"com.example.weatherphoto.fileprovider", file )

        binding.shareImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/jpeg"
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            startActivity(Intent.createChooser(intent, getString(R.string.share_image)))
        }

        return root
    }
}