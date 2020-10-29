package com.app.kotlinapp.data.api

import android.content.*
import com.app.kotlinapp.*
import com.google.gson.*
import okhttp3.*
import okhttp3.logging.*
import retrofit2.*
import retrofit2.converter.gson.*
import java.util.concurrent.*

/**
 * Created by santosh on 20/10/20.
 */
object ApiServiceImpl {
    fun provideApiService(): ApiService {
        return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(provideOkHttpClient()).build().create(ApiService::class.java)
    }

    private fun provideOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor=HttpLoggingInterceptor()
        httpLoggingInterceptor.level=HttpLoggingInterceptor.Level.BODY
        val okHttpClient =
            OkHttpClient.Builder().readTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES).connectTimeout(2,
                    TimeUnit.MINUTES).addInterceptor(httpLoggingInterceptor)
        return okHttpClient.build()
    }
}