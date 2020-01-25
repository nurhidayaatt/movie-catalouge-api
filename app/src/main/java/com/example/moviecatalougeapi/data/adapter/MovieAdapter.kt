package com.example.moviecatalougeapi.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviecatalougeapi.R
import com.example.moviecatalougeapi.data.model.movie.ResultMovie
import kotlinx.android.synthetic.main.item_layout_list.view.*

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    companion object {
        const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w154"
    }

    private val mData = ArrayList<ResultMovie>()

    fun setData(items: ArrayList<ResultMovie>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    fun setData() {
        mData.clear()
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(resultMovie: ResultMovie) {
            with(itemView) {
                Glide.with(this).load(String.format(resources.getString(R.string.format_url), POSTER_BASE_URL, resultMovie.posterPath)).placeholder(R.drawable.placeholder).into(iv_poster)
                iv_poster.contentDescription = String.format(resources.getString(R.string.format_content_description), resources.getString(R.string.image_description), resultMovie.title)
                tv_title.text = resultMovie.title
                tv_release_date.text = resultMovie.releaseDate

                when {
                    resultMovie.overview==null -> {
                        tv_overview.text = resources.getString(R.string.empty_string)
                    }
                    resultMovie.overview!!.trim().isEmpty() -> {
                        tv_overview.text = context.getString(R.string.empty_string)
                    }
                    else -> {
                        tv_overview.text = resultMovie.overview
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_list, parent, false)
        return MovieViewHolder(mView)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
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
        fun onItemClicked(data: ResultMovie)
    }
}