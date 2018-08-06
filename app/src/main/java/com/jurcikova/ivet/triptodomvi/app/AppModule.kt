package com.jurcikova.ivet.triptodomvi.app

import android.app.Application
import android.arch.persistence.room.Room
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.jurcikova.ivet.triptodomvi.business.api.CountryApi
import com.jurcikova.ivet.triptodomvi.business.db.GlobalDatabase
import com.jurcikova.ivet.triptodomvi.business.interactor.CountryListInteractor
import com.jurcikova.ivet.triptodomvi.business.interactor.CountrySearchInteractor
import com.jurcikova.ivet.triptodomvi.business.interactor.MyCountryListInteractor
import com.jurcikova.ivet.triptodomvi.business.repository.CountryRepository
import com.jurcikova.ivet.triptodomvi.business.repository.CountryRepositoryImpl
import com.jurcikova.ivet.triptodomvi.ui.countryList.CountryAdapter
import com.jurcikova.ivet.triptodomvi.ui.myCountries.MyCountryAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.strv.ktools.DIModule
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class AppModule(private val application: Application) : DIModule() {

    private val moshi by lazy {
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
                .baseUrl("https://restcountries.eu/rest/v2/")
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    private val database by lazy {
        Room.databaseBuilder(application, GlobalDatabase::class.java, "global-db")
                .apply { fallbackToDestructiveMigration() }
                .build()
    }

    override fun onProvide() {
        provideSingleton { application }

        onProvideApi()
        onProvideDao()
        onProvideRepo()
        onProvideAdapters()
        onProvideInteractors()
    }

    private fun onProvideDao() {
        provideSingleton {
            database.countryDao()
        }
    }

    private fun onProvideInteractors() {
        provideSingleton { CountryListInteractor() }
        provideSingleton { CountrySearchInteractor() }
        provideSingleton { MyCountryListInteractor() }
    }

    private fun onProvideAdapters() {
        provide {
            CountryAdapter()
        }
        provide {
            MyCountryAdapter()
        }
    }

    private fun onProvideRepo() {
        provideSingleton<CountryRepository> { CountryRepositoryImpl() }
    }

    private fun onProvideApi() {
        provideSingleton<CountryApi> { retrofit.create(CountryApi::class.java) }
    }
}




