package com.hitss.ui.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hitss.databinding.ItemGenresBinding

class AdapterGenres (var list : MutableList<String>) : RecyclerView.Adapter<AdapterGenres.MyViewHolder>() {

    inner class  MyViewHolder( var binding : ItemGenresBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : String, position: Int) {
            binding.apply {
                textGenre.text = item
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(list[position],position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemGenresBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }
    override fun getItemCount() = list.size

}