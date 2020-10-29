package com.app.kotlinapp.ui.base

import androidx.lifecycle.*
import com.app.kotlinapp.data.api.*
import com.app.kotlinapp.data.repository.*
import com.app.kotlinapp.ui.main.viewmodel.*
import java.lang.IllegalArgumentException

/**
 * Created by santosh on 10/10/20.
 */
class ViewModelFactory(private var apiHelper: ApiHelper) :ViewModelProvider.Factory{
    @Throws(IllegalArgumentException::class)
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(MainRepository((apiHelper))) as T
        }
        throw IllegalArgumentException("unknow")
    }
}