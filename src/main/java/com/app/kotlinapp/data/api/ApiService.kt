package com.app.kotlinapp.data.api

import com.app.kotlinapp.*
import com.app.kotlinapp.data.model.*
import retrofit2.*
import retrofit2.http.*

/**
 * Created by santosh on 20/10/20.
 */
interface ApiService {
    @GET("/")
    fun getSearchMovieList(
        @Query("s") searchMovieName: String,@Query("page")currentPage:String,@Query("apikey") apikey: String = BuildConfig.API_KEY,@Query("type") searchType: String = "movie"): Call<SearchMovieModel>

    @GET("/")
    fun getMovieListDetails(
        @Query("i") movieID: String,@Query("apikey") apikey: String = BuildConfig.API_KEY): Call<MovieDetailsModel>
}