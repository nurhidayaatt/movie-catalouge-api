package com.example.moviecatalougeapi.data.api

import com.example.moviecatalougeapi.BuildConfig
import com.example.moviecatalougeapi.data.model.movie.MovieList
import com.example.moviecatalougeapi.data.model.tv.TvList
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


const val BASE_URL = "https://api.themoviedb.org/3/"

interface ApiInterface {
    @GET("discover/movie")
    suspend fun tes(@Query("page") page: Int): MovieList


    @GET("discover/movie")
    suspend fun getMovie(@Query("language") language: String): MovieList

    @GET("discover/tv")
    suspend fun getTV(@Query("language") language: String): TvList

    @GET("search/movie")
    suspend fun searchMovie(@Query("language") language: String, @Query("query") query: String): MovieList

    @GET("search/tv")
    suspend fun searchTV(@Query("language") language: String, @Query("query") query: String): TvList

    @GET("discover/movie")
    suspend fun releaseMovie(@Query("language") language: String, @Query("primary_release_date.gte") gte: String, @Query("primary_release_date.lte") tle: String): MovieList

    @GET("discover/tv")
    suspend fun releaseTv(@Query("language") language: String, @Query("primary_release_date.gte") gte: String, @Query("primary_release_date.lte") tle: String): TvList
}

object ApiClient {
    fun getClient(): ApiInterface {
        val httpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addNetworkInterceptor { chain ->
                val request = chain.request().url.newBuilder()
                    .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY).build()
                chain.proceed(chain.request().newBuilder().url(request).build())
            }
            .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        return Retrofit.Builder()
            .client(httpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(ApiInterface::class.java)
    }
}
