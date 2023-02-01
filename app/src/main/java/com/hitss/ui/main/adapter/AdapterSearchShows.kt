package com.hitss.ui.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hitss.data.remote.dto.app.Hits
import com.hitss.data.remote.dto.app.SearchTv
import com.hitss.databinding.ItemSearchShowBinding
import com.hitss.databinding.ItemTvShowBinding
import com.hitss.utils.loadImageFromPicasso

class AdapterSearchShows (var list : MutableList<SearchTv>) : RecyclerView.Adapter<AdapterSearchShows.MyViewHolder>() {

    lateinit var actionOpenDetail : ((position : Int) -> Unit)

    private var holderList = HashMap<Int, MyViewHolder>()
    inner class  MyViewHolder( var binding : ItemSearchShowBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item : SearchTv, position: Int) {
            binding.apply {
                textName.text = item.show.name ?: ""
                textNetworkName.text = item.show.network?.name ?: ""
                if(item.show.schedule?.getSchedule().isNullOrEmpty()){
                    containerAirtime.visibility = View.INVISIBLE
                }else {
                    containerAirtime.visibility = View.VISIBLE
                    textAirtime.text = item.show.schedule?.getSchedule()
                }
                imgShow.loadImageFromPicasso(item.show.image?.medium ?: "")

                containerShow.setOnClickListener {
                    actionOpenDetail.invoke(position)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if(!holderList.containsKey(position)) {
            holderList[position] = holder
        }
        holder.bind(list[position],position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemSearchShowBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }
    override fun getItemCount() = list.size

    fun getViewByPosition( position : Int ) : MyViewHolder {
        return holderList[position]!!
    }

    fun update() {
        notifyDataSetChanged()
    }
}