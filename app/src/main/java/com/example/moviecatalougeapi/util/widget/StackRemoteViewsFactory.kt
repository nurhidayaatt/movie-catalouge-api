package com.example.moviecatalougeapi.util.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.example.moviecatalougeapi.R
import com.example.moviecatalougeapi.data.database.movie.MovieFavoriteDao
import com.example.moviecatalougeapi.data.database.movie.MovieFavoriteDatabase
import com.example.moviecatalougeapi.data.database.tv.TvFavoriteDao
import com.example.moviecatalougeapi.data.database.tv.TvFavoriteDatabase

internal class StackRemoteViewsFactory(private val context: Context) : RemoteViewsService.RemoteViewsFactory{

    companion object {
        const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w154"
    }

    private val movieFavoriteDao: MovieFavoriteDao = MovieFavoriteDatabase.getInstance(context).movieFavoriteDao()
    private val tvFavoriteDao: TvFavoriteDao = TvFavoriteDatabase.getInstance(context).tvFavoriteDao()
    private val mWidgetItems = ArrayList<String>()

    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        mWidgetItems.addAll(movieFavoriteDao.loadFavorite())
        mWidgetItems.addAll(tvFavoriteDao.loadFavorite())
    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(p0: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.widget_item)
        val bitmap: Bitmap = Glide.with(context).asBitmap().load(String.format(context.getString(R.string.format_url), POSTER_BASE_URL, mWidgetItems[p0])).placeholder(R.drawable.placeholder).submit(200, 200).get()
        rv.setImageViewBitmap(R.id.imageView, bitmap)

        val extras = bundleOf(
            FavoriteWidget.EXTRA_ITEM to p0
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(p0: Int): Long = 0

    override fun hasStableIds(): Boolean = false

    override fun onDestroy() {

    }
}