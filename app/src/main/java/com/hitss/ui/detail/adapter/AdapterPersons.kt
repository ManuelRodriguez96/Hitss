package com.hitss.ui.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hitss.data.remote.dto.app.CastTv
import com.hitss.databinding.ItemPersonBinding
import com.hitss.utils.loadImageFromPicasso

class AdapterPersons (var list : MutableList<CastTv>) : RecyclerView.Adapter<AdapterPersons.MyViewHolder>() {

    inner class  MyViewHolder( var binding : ItemPersonBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : CastTv, position: Int) {
            binding.apply {
                textPerson.text = item.person.name
                imgPerson.loadImageFromPicasso(item.person.image?.medium?: "")
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(list[position],position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemPersonBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }
    override fun getItemCount() = list.size

}