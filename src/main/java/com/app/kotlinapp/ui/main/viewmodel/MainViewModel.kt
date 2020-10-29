package com.app.kotlinapp.ui.main.viewmodel

import androidx.lifecycle.*
import com.app.kotlinapp.data.model.*
import com.app.kotlinapp.data.repository.*
import com.app.kotlinapp.utils.*

/**
 * Created by santosh on 28/10/20.
 */
class MainViewModel(private var mainRepository: MainRepository) : ViewModel() {
    fun searchMovieList(searchMovieName: String, currentPage: String): MutableLiveData<Resource<SearchMovieModel>> =
        mainRepository.getSearchMovieList(searchMovieName,currentPage)

    fun fetchMovieLDetails(searchMovieName: String) =
        mainRepository.getMovieListDetails(searchMovieName)
}