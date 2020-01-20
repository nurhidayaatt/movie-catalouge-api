package com.example.moviecatalougeapi.data.database.tv

import com.example.moviecatalougeapi.data.model.tv.ResultTv

class TvFavoriteRepository(private val tvfavoriteDao : TvFavoriteDao) {

    val allTvFavorites = tvfavoriteDao.loadTvFavorites()

    fun tvFavoriteId(id: Int) = tvfavoriteDao.loadTvId(id)

    suspend fun searchTvFavorite(search: String) = tvfavoriteDao.searchTv(search)

    suspend fun insert(tv: ResultTv) {
        tvfavoriteDao.insertTvFavorites(tv)
    }

    suspend fun delete(tv: ResultTv) {
        tvfavoriteDao.deleteTvFavorites(tv)
    }

}