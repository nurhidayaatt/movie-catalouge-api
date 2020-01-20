package com.example.moviecatalougeapi.data.database.tv

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.moviecatalougeapi.data.model.tv.ResultTv

@Dao
interface TvFavoriteDao {

    @Query("SELECT * FROM tv_favorite")
    fun loadTvFavorites() : LiveData<List<ResultTv>>

    @Query("SELECT id FROM tv_favorite WHERE id =:Id")
    fun loadTvId(Id: Int) : Int

    @Query("SELECT * FROM tv_favorite WHERE name =:Name")
    suspend fun searchTv(Name: String) : List<ResultTv>

    @Query("SELECT backdropPath FROM tv_favorite")
    fun loadFavorite() : List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTvFavorites(tv: ResultTv)

    @Delete
    suspend fun deleteTvFavorites(tv: ResultTv)

}