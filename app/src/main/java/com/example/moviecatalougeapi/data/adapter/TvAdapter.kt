package com.example.moviecatalougeapi.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviecatalougeapi.R
import com.example.moviecatalougeapi.data.model.tv.ResultTv
import kotlinx.android.synthetic.main.item_layout_list.view.*

class TvAdapter : RecyclerView.Adapter<TvAdapter.TvViewHolder>() {

    companion object {
        const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w154"
    }

    private val mData = ArrayList<ResultTv>()

    fun setData(items: ArrayList<ResultTv>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    fun setData() {
        mData.clear()
        notifyDataSetChanged()
    }

    inner class TvViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(resultTv: ResultTv) {
            with(itemView) {
                Glide.with(this).load(String.format(resources.getString(R.string.format_url), POSTER_BASE_URL, resultTv.posterPath)).placeholder(R.drawable.placeholder).into(iv_poster)
                iv_poster.contentDescription = String.format(resources.getString(R.string.format_content_description), resources.getString(R.string.image_description), resultTv.name)
                tv_title.text = resultTv.name
                tv_release_date.text = resultTv.firstAirDate

                when {
                    resultTv.overview==null -> {
                        tv_overview.text = resources.getString(R.string.empty_string)
                    }
                    resultTv.overview!!.trim().isEmpty() -> {
                        tv_overview.text = context.getString(R.string.empty_string)
                    }
                    else -> {
                        tv_overview.text = resultTv.overview
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_list, parent, false)
        return TvViewHolder(mView)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: TvViewHolder, position: Int) {
        holder.bind(mData[position])

        holder.itemView.setOnClickListener{
            onItemClickCallback.onItemClicked(mData[holder.adapterPosition])
        }
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ResultTv)
    }
}