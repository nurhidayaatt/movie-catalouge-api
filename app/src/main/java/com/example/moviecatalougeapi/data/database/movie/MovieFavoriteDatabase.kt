package com.example.moviecatalougeapi.data.database.movie

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.moviecatalougeapi.data.model.movie.ResultMovie

@Database(version = 1, entities = [ResultMovie::class])
abstract class MovieFavoriteDatabase : RoomDatabase() {

    abstract fun movieFavoriteDao() : MovieFavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: MovieFavoriteDatabase? = null

        fun getInstance(context: Context) : MovieFavoriteDatabase {
            synchronized(this) {
                var instance =
                    INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext, MovieFavoriteDatabase::class.java, "movie_favorite_database").fallbackToDestructiveMigration().build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }

}