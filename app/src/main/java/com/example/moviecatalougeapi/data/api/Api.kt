package com.example.moviecatalougeapi.data.api

import com.example.moviecatalougeapi.data.model.movie.MovieList
import com.example.moviecatalougeapi.data.model.tv.TvList
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://api.themoviedb.org/3/"

interface Api {
    @GET("discover/movie")
    suspend fun getMovie(@Query("api_key") key: String, @Query("language") language: String): MovieList

    @GET("discover/tv")
    suspend fun getTV(@Query("api_key") key: String, @Query("language") language: String): TvList

    @GET("search/movie")
    suspend fun searchMovie(@Query("api_key") key: String, @Query("language") language: String, @Query("query") query: String): MovieList

    @GET("search/tv")
    suspend fun searchTV(@Query("api_key") key: String, @Query("language") language: String, @Query("query") query: String): TvList

    @GET("discover/movie")
    suspend fun releaseMovie(@Query("api_key") key: String, @Query("primary_release_date.gte") gte: String, @Query("primary_release_date.lte") tle: String): MovieList

    @GET("discover/tv")
    suspend fun releaseTv(@Query("api_key") key: String, @Query("primary_release_date.gte") gte: String, @Query("primary_release_date.lte") tle: String): TvList
}

object ApiService {
    val retrofitService: Api = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).build().create(Api::class.java)
}
