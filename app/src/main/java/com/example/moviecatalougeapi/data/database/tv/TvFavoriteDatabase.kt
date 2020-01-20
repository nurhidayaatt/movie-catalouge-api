package com.example.moviecatalougeapi.data.database.tv

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.moviecatalougeapi.data.model.tv.ResultTv

@Database(version = 1, entities = [ResultTv::class])
abstract class TvFavoriteDatabase : RoomDatabase() {

    abstract fun tvFavoriteDao() : TvFavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: TvFavoriteDatabase? = null

        fun getInstance(context: Context) : TvFavoriteDatabase {
            synchronized(this) {
                var instance =
                    INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext, TvFavoriteDatabase::class.java, "tv_favorite_database").fallbackToDestructiveMigration().build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }

}