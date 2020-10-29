package com.app.kotlinapp.data.repository

import androidx.lifecycle.*
import com.app.kotlinapp.data.api.*
import com.app.kotlinapp.data.model.*
import com.app.kotlinapp.utils.*
import retrofit2.*

/**
 * Created by santosh on 10/10/20.
 */
class MainRepository(private var apiHelper: ApiHelper) {
    fun getSearchMovieList(searchMovieName: String,
        currentPage: String): MutableLiveData<Resource<SearchMovieModel>> {
        val searchMovieModelLiveData = MutableLiveData<Resource<SearchMovieModel>>()
        searchMovieModelLiveData.postValue(Resource.loading(null))
        apiHelper.getSearchMovieList(searchMovieName, currentPage)
            .enqueue(object : Callback<SearchMovieModel> {
                override fun onResponse(call: Call<SearchMovieModel>,
                    response: Response<SearchMovieModel>) {
                    if (response.body()?.response.equals("true",true)) {
                        searchMovieModelLiveData.postValue(Resource.success(response.body()))
                    } else {
                        searchMovieModelLiveData.postValue(Resource.error("movie is not found",
                            null))
                    }
                }

                override fun onFailure(call: Call<SearchMovieModel>, t: Throwable) {
                    searchMovieModelLiveData.postValue(Resource.error(t.message, null))
                }
            })
        return searchMovieModelLiveData
    }

    fun getMovieListDetails(movieId: String): MutableLiveData<Resource<MovieDetailsModel>> {
        val movieListDetailsLiveData = MutableLiveData<Resource<MovieDetailsModel>>()
        movieListDetailsLiveData.postValue(Resource.loading(null))
        apiHelper.getMovieListDetails(movieId)
            .enqueue(object : Callback<MovieDetailsModel> {
                override fun onResponse(call: Call<MovieDetailsModel>,
                    response: Response<MovieDetailsModel>) {
                    movieListDetailsLiveData.postValue(Resource.success(response.body()))
                }

                override fun onFailure(call: Call<MovieDetailsModel>, t: Throwable) {
                    movieListDetailsLiveData.postValue(Resource.error(t.message, null))
                }
            })
        return movieListDetailsLiveData
    }
}