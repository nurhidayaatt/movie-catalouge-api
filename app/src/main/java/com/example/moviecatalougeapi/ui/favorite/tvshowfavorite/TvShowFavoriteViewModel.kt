package com.example.moviecatalougeapi.ui.favorite.tvshowfavorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.moviecatalougeapi.data.database.tv.TvFavoriteDao
import com.example.moviecatalougeapi.data.database.tv.TvFavoriteDatabase
import com.example.moviecatalougeapi.data.database.tv.TvFavoriteRepository
import com.example.moviecatalougeapi.data.model.tv.ResultTv

class TvShowFavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val tvFavoriteDao: TvFavoriteDao = TvFavoriteDatabase.getInstance(application).tvFavoriteDao()
    private val repository: TvFavoriteRepository =
        TvFavoriteRepository(
            tvFavoriteDao
        )

    private var _tvs : LiveData<List<ResultTv>> = repository.allTvFavorites
    val tvs : LiveData<List<ResultTv>> get() = _tvs

}
