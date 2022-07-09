package com.example.weatherphoto

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.NavHostFragment
import com.example.weatherphoto.databinding.ActivityMainBinding
import com.example.weatherphoto.domain.util.Util
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        if (!Util.isGPSEnabled(this)) {
            Snackbar.make(
                binding.root,
                getString(R.string.turnOn_gps),
                Snackbar.LENGTH_LONG
            )
                .setAction(
                    getString(R.string.open_gps)
                ) {
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }
                .show()
        }

        viewModel.connectionLiveData.observe(this) { isAvailable ->
            when (isAvailable) {
                true -> {
                    val id = navController.currentDestination!!.id
                    if (id == R.id.noConnectionFragment) {
                        navController.navigateUp()
                    }
                }

                false -> {
                    navController.navigate(R.id.noConnectionFragment)
                }
            }
        }
        supportActionBar!!.hide()
    }


}