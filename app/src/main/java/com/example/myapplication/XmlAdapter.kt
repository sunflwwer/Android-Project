package com.example.myapplication

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.myapplication.databinding.ItemMainBinding


class XmlViewHolder(val binding: ItemMainBinding) : RecyclerView.ViewHolder(binding.root)
class XmlAdapter(val datas: MutableList<myXmlItem>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {     // 1-1
    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return XmlViewHolder(
            ItemMainBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as XmlViewHolder).binding
        val model = datas!![position]           // 1-2


        binding.name.text = model.krnm
        binding.manufacture.text = model.kornFamlNm
        binding.nutrient.text = model.fturCn
        binding.ename.text = model.scnm
        binding.grade.text = model.rrtyPrntGrdCd
        binding.place.text = model.excptnMttrCn

        val rawUrl = model.imgUrl
        val decodedUrl = rawUrl?.replace("&amp;", "&")
        Log.d("XmlAdapter", "Raw Image URL: $rawUrl")
        Log.d("XmlAdapter", "Decoded Image URL: $decodedUrl")

        val requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(R.drawable.ic_launcher_background)
            .override(600, 600)

        Glide.with(binding.root)
            .setDefaultRequestOptions(requestOptions)
            .load(decodedUrl)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.e("Glide", "Image load failed: ${e?.message}")
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.d("Glide", "Image loaded successfully")
                    return false
                }
            })
            .into(binding.urlImage)
    }

}
