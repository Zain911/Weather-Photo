package com.example.weatherphoto.ui.camera

import android.app.Activity
import android.content.Intent
import android.graphics.*
import android.location.Geocoder
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.weatherphoto.R
import com.example.weatherphoto.databinding.FragmentCameraBinding
import com.example.weatherphoto.domain.usecase.LocationModel
import com.example.weatherphoto.domain.util.LoadingDialog
import com.example.weatherphoto.domain.util.Util
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import kotlin.math.roundToInt

@AndroidEntryPoint
class CameraFragment : Fragment() {

    private val viewModel: CameraViewModel by viewModels()

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private var imagePath = MutableLiveData<String>()
    private lateinit var loadingDialog: LoadingDialog
    private val imagePathObserver = Observer<String> {
        Log.d("ImagePath", imagePath.value.toString())
        viewModel.getLocationData().observe(viewLifecycleOwner) { locationModel ->
            requestLocationWeatherStatus(locationModel)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        val root: View = binding.root
        loadingDialog = LoadingDialog(requireContext())
        startCamera()

        viewModel.weatherResponse.observe(viewLifecycleOwner) {
            binding.temperatureTextView.text = it.current.temp.roundToInt()
                .toString() + requireContext().getString(R.string.degree)
            binding.highestAndLowestTempTextView.text =
                it.daily?.get(0)?.temp?.min?.roundToInt()
                    .toString() + requireContext().getString(R.string.degree) + " / " + it.daily?.get(
                    0
                )?.temp?.max?.roundToInt() + requireContext().getString(
                    R.string.degree
                )
            binding.weatherStatusTextView.text = it.current.weather[0].description

            val fullImageUrl =
                "https://openweathermap.org/img/wn/" + it.current.weather[0].icon + "@2x.png"

            Glide.with(requireContext())
                .load(fullImageUrl)
                .placeholder(R.drawable.ic_baseline_cloud_24)
                .into(binding.weatherStatusImageView)

            drawWeatherStatusOnImage()
        }


        return root
    }

    private fun startCamera() {
        val fileName = "photo"
        val storageDirectory = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        val imageFile = File.createTempFile(fileName, ".jpg", storageDirectory)
        imagePath.value = imageFile.absolutePath

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val uri = FileProvider.getUriForFile(
            requireContext(),
            "com.example.weatherphoto.fileprovider",
            imageFile
        )

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        resultLauncher.launch(intent)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                setImage()
            } else {
                val file = imagePath.value?.let {
                    File(it)
                }
                file?.delete()
                imagePath.value?.let { viewModel.deleteImage(it) }
                findNavController().popBackStack()
            }

        }

    private fun setImage() {
        val imageBitmapFromDevice = BitmapFactory.decodeFile(imagePath.value)
        binding.imgViewer.setImageBitmap(imageBitmapFromDevice)
        imagePath.observe(viewLifecycleOwner, imagePathObserver)
    }

    private fun requestLocationWeatherStatus(locationModel: LocationModel) {
        loadingDialog.showDialog()
        val geoCoder = Geocoder(context, Locale.getDefault())

        val addresses = lifecycleScope.async {

            val address = geoCoder.getFromLocation(
                locationModel.latitude,
                locationModel.longitude,
                1
            )
            binding.placeNameTextView.text = address[0].subAdminArea + ", " + address[0].adminArea
            binding.placeNameTextView.visibility = View.INVISIBLE
            return@async address
        }


        lifecycleScope.launch {
            if (Util.checkForInternet(requireContext())) {
                viewModel.getLocationWeatherStatus(
                    locationModel.latitude,
                    locationModel.longitude,
                    imagePath.value!!,
                    addresses.await()[0].subAdminArea + ", " + addresses.await()[0].adminArea
                )
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.no_internet_connection),
                    Toast.LENGTH_SHORT
                ).show()
                loadingDialog.hideDialog()
                findNavController().popBackStack()
            }
        }
    }

    private fun drawWeatherStatusOnImage() {

        val newBitmap = Bitmap.createBitmap(
            binding.mainContainer.width,
            binding.mainContainer.height,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(newBitmap)

        binding.mainContainer.draw(canvas)
        canvas.drawText(binding.placeNameTextView.text.toString(), 40f, 200f, createPaint())
        canvas.drawText(binding.temperatureTextView.text.toString(), 80f, 300f, createPaint(40f))
        canvas.drawText(
            binding.highestAndLowestTempTextView.text.toString(),
            40f,
            400f,
            createPaint()
        )
        canvas.drawText(
            binding.weatherStatusTextView.text.toString(),
            (binding.mainContainer.width - 220f),
            400f,
            createPaint()
        )

        viewModel.saveImageWithWeatherOverlay(newBitmap, imagePath.value!!)

        binding.imgViewer.setImageBitmap(newBitmap)

        hideViews()
    }

    private fun createPaint(textSize: Float = 30f): Paint {
        val paint = Paint()
        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
        paint.textSize = textSize
        paint.setShadowLayer(0f, 0f, 0f, Color.BLACK)
        return paint
    }

    private fun hideViews() {
        binding.placeNameTextView.visibility = View.GONE
        binding.temperatureTextView.visibility = View.GONE
        binding.highestAndLowestTempTextView.visibility = View.GONE
        binding.weatherStatusImageView.visibility = View.GONE
        binding.weatherStatusTextView.visibility = View.GONE
        loadingDialog.hideDialog()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        imagePath.removeObserver(imagePathObserver)
    }
}