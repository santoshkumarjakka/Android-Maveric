package com.app.kotlinapp.utils

/**
 * Created by santosh on 10/10/20.
 */
data class Resource<out T> (val status:Status,val data:T?,val message:String?){

    companion object{
        fun <T> success(data:T?): Resource<T> {
            return Resource(status = Status.SUCCESS,data,null)
        }
        fun <T> error(message: String?,data:T?): Resource<T> {
            return Resource(status = Status.ERROR,data,message)
        }
        fun <T> loading(data:T?): Resource<T> {
            return Resource(status = Status.LOADING,data,null)
        }
    }
}
