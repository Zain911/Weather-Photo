package com.example.weatherphoto.domain.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import com.example.weatherphoto.R
import com.example.weatherphoto.databinding.DialogLoadingBinding
import com.example.weatherphoto.databinding.FragmentCameraBinding

class LoadingDialog(private val context: Context) {
    lateinit var dialog: Dialog

    fun showDialog() {
        dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_loading)
        dialog.setCancelable(false)
        dialog.create()
        dialog.show()
    }

    fun hideDialog() {
        dialog.dismiss()
    }
}