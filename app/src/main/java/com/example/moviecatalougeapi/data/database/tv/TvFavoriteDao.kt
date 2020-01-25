package com.example.moviecatalougeapi.data.database.tv

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.moviecatalougeapi.data.model.tv.ResultTv

@Dao
interface TvFavoriteDao {

    @Query("SELECT * FROM tv_favorite")
    fun loadTvFavorites() : LiveData<List<ResultTv>>

    @Query("SELECT id FROM tv_favorite WHERE id =:tvId")
    fun loadTvId(tvId: Int) : Int

    @Query("SELECT * FROM tv_favorite WHERE name =:Name")
    suspend fun searchTv(Name: String) : List<ResultTv>

    @Query("SELECT backdropPath FROM tv_favorite")
    fun loadFavorite() : List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTvFavorites(tv: ResultTv)

    @Delete
    suspend fun deleteTvFavorites(tv: ResultTv)

    @Query("SELECT * FROM tv_favorite")
    fun getAllFavoriteCursor(): Cursor

    @Query("SELECT * FROM tv_favorite WHERE id = :tvId")
    fun getFavoriteMovieCursor(tvId: Int): Cursor

}