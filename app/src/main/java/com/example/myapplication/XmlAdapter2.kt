package com.example.myapplication

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.myapplication.databinding.MyItemMainBinding

class XmlViewHolder2(val binding: MyItemMainBinding) : RecyclerView.ViewHolder(binding.root)

class XmlAdapter2(val originalDatas: MutableList<MyXmlItem2>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var filteredDatas: MutableList<MyXmlItem2> = originalDatas.toMutableList()

    override fun getItemCount(): Int {
        return filteredDatas.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return XmlViewHolder2(
            MyItemMainBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as XmlViewHolder2).binding
        val model = filteredDatas[position]

        binding.name.text =
            model.krnm?.let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY).toString() }
                ?: "N/A"
        binding.ename.text =
            model.scnm?.let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY).toString() }
                ?: "N/A"
        binding.hanname.text = model.chchNm?.let {
            HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
        } ?: "N/A"
        binding.sort.text = model.kornFamlNm?.let {
            HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
        } ?: "N/A"
        binding.info.text = model.fturCn?.let {
            HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
        } ?: "N/A"
        binding.infode.text = model.mdcnlInfoCn?.let {
            HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
        } ?: "N/A"

        val rawUrl = model.imgUrl
        val decodedUrl = rawUrl?.replace("&amp;", "&")
        Log.d("XmlAdapter2", "Raw Image URL: $rawUrl")
        Log.d("XmlAdapter2", "Decoded Image URL: $decodedUrl")

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

    fun updateList(newItems: List<MyXmlItem2>) {
        filteredDatas = newItems.toMutableList()
        notifyDataSetChanged()
    }
}
