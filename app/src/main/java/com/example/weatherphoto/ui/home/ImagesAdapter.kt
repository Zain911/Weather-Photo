package com.example.weatherphoto.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherphoto.R
import com.example.weatherphoto.data.model.ImageItem
import com.example.weatherphoto.databinding.ItemImageBinding
import java.text.SimpleDateFormat


class ImagesAdapter(
    private var imagesList: MutableList<ImageItem>,
    private val itemClick: (ImageItem) -> Unit
) :
    RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun changeList(newList: MutableList<ImageItem>) {
        imagesList.clear()
        imagesList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    class ViewHolder(var view: ItemImageBinding) : RecyclerView.ViewHolder(view.root)

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.imageNameTextView.text = imagesList[position].name

        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val date = format.parse(imagesList[position].date.toString())

        holder.view.imageDateTextView.text = date?.toString()

        holder.view.imageTemperatureTextView.text =
            imagesList[position].temperature + holder.view.root.context.getString(
                R.string.degree
            )
        Glide.with(holder.view.imageItemImageView.context)
            .load(imagesList[position].imageUrl)
            .into(holder.view.imageItemImageView)

        holder.view.mainContainerConstraintLayout.setOnClickListener {
            itemClick(imagesList[position])
        }
    }


    override fun getItemCount() = imagesList.size
}
