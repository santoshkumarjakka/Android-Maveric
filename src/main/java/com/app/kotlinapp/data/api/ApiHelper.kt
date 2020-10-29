package com.app.kotlinapp.data.api

/**
 * Created by santosh on 10/10/20.
 */
class ApiHelper(private var apiHelper: ApiService) {
    fun getSearchMovieList(searchMovieName: String, currentPage: String) = apiHelper.getSearchMovieList(searchMovieName,currentPage)
    fun getMovieListDetails(endpoint: String) = apiHelper.getMovieListDetails(endpoint)
}