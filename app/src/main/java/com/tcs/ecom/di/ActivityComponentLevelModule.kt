package com.tcs.ecom.di

import com.tcs.ecom.BuildConfig
import com.tcs.ecom.api.AuthenticationApi
import com.tcs.ecom.api.ProductApi
import com.tcs.ecom.repository.AuthenticationRepository
import com.tcs.ecom.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ActivityComponentLevelModule {
    private const val BASE_URL = "http://192.168.1.100:5000/api/"

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG)
            logging.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder().apply {
            addInterceptor(logging)
            connectTimeout(10000L, TimeUnit.MILLISECONDS)
            readTimeout(10000L, TimeUnit.MILLISECONDS)
            writeTimeout(10000L, TimeUnit.MILLISECONDS)
            connectTimeout(10000L, TimeUnit.MILLISECONDS)
        }.build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun providesAuthenticationApi(retrofit: Retrofit): AuthenticationApi =
        retrofit.create(AuthenticationApi::class.java)

    @Provides
    @Singleton
    fun providesAuthRepo(authenticationApi: AuthenticationApi) =
        AuthenticationRepository(authenticationApi)

    @Provides
    @Singleton
    fun providesProductApi(retrofit: Retrofit): ProductApi = retrofit.create(ProductApi::class.java)

    @Provides
    @Singleton
    fun providesProductRepo(productApi: ProductApi) = ProductRepository(productApi)
}