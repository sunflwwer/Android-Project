package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemHinfoBinding

class PhpViewHolder(val binding: ItemHinfoBinding) : RecyclerView.ViewHolder(binding.root)

class PhpAdapter(val context: Context, val itemList: ArrayList<HinfoData>) :
    RecyclerView.Adapter<PhpViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhpViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PhpViewHolder(ItemHinfoBinding.inflate(layoutInflater))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: PhpViewHolder, position: Int) {
        val data = itemList.get(position)

        holder.binding.run {
            tvSort.text = data.sort
            tvFamilyN.text = data.FamilyN
            tvScienceN.text = data.ScienceN
            tvName.text = data.name

            root.setOnClickListener {

                Intent(context, DetailActivity::class.java).apply {
                    putExtra("name", tvName.text)
                    putExtra("FamilyN", tvFamilyN.text)
                    putExtra("ScienceN", tvScienceN.text)
                    putExtra("sort", tvSort.text)
                }.run {
                    context.startActivity(this) // 앞에 context 넣어주기
                }
            }
            tvName.setOnClickListener {

            }
            imageView.setOnClickListener {

            }
        }
    }
}

