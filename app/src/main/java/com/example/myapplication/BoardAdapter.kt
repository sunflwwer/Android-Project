package com.example.myapplication

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ItemCommentBinding

class BoardViewHolder(val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root)

class BoardAdapter(
    val context: Context,
    private var itemList: List<ItemData>,
    private val textSize: Float
) : RecyclerView.Adapter<BoardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return BoardViewHolder(ItemCommentBinding.inflate(layoutInflater, parent, false))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        val data = itemList[position]

        holder.binding.run {
            idTextView.text = data.email
            dateTextView.text = data.date_time
            contentsTextView.text = data.comments
            ratingBar.rating = data.star.toFloat()

            idTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize)
            dateTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize)
            contentsTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize)

            val imageRef = MyApplication.storage.reference.child("images/${data.docId}.jpg")
            imageRef.downloadUrl.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    holder.binding.itemImageView.visibility = View.VISIBLE
                    Glide.with(context)
                        .load(task.result)
                        .into(holder.binding.itemImageView)
                }
            }
        }
    }
}
