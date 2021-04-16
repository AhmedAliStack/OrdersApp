package com.ahmedrafat.ordersapp.di

import android.app.Application
import android.content.Context
import com.ahmedrafat.ordersapp.model.DOMAIN
import com.ahmedrafat.ordersapp.model.NetworkConnectionInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provide custom instance of [OkHttpClient].
     */
    @Provides
    @Singleton
    fun provideClient(context: Context): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)

        builder.addInterceptor(providesInterceptor(context))
        return builder.build()
    }

    /**
     * Provide instance of [ApiService].
     */
    @Provides
    @Singleton
    fun provideApiService(context: Context): Retrofit =
        Retrofit.Builder()
            .baseUrl(DOMAIN)
            .client(provideClient(context))
            .addCallAdapterFactory(provideCoroutineFactory())
            .addConverterFactory(provideMoshiFactory())
            .build()
//            .create(ApiService::class.java)


    /**
     * Provide instance of [CoroutineCallAdapterFactory].
     */
    @Provides
    @Singleton
    fun provideCoroutineFactory(): CoroutineCallAdapterFactory = CoroutineCallAdapterFactory()

    /**
     * Provide instance of [ProvidesInterceptor].
     */
    @Provides
    @Singleton
    fun providesInterceptor(context: Context): NetworkConnectionInterceptor = NetworkConnectionInterceptor(context)

    /**
     * Provide instance of [GsonConverterFactory].
     */
    @Provides
    @Singleton
    fun provideMoshiFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application
}