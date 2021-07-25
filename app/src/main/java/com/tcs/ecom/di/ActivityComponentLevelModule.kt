package com.tcs.ecom.di

import com.tcs.ecom.BuildConfig
import com.tcs.ecom.api.AuthenticationApi
import com.tcs.ecom.api.CartApi
import com.tcs.ecom.api.OrderApi
import com.tcs.ecom.api.ProductApi
import com.tcs.ecom.repository.AuthenticationRepository
import com.tcs.ecom.repository.CartRepository
import com.tcs.ecom.repository.OrderRepository
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
    private val BASE_URL =
        listOf("http://192.168.1.100:5000/api/", "https://bhuvancom.herokuapp.com/api/")

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
            .baseUrl(BASE_URL[0])
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
    fun providesAuthRepo(authenticationApi: AuthenticationApi): AuthenticationRepository =
        AuthenticationRepository(authenticationApi)

    @Provides
    @Singleton
    fun providesProductApi(retrofit: Retrofit): ProductApi = retrofit.create(ProductApi::class.java)

    @Provides
    @Singleton
    fun providesProductRepo(productApi: ProductApi): ProductRepository =
        ProductRepository(productApi)

    @Provides
    @Singleton
    fun providesCartApi(retrofit: Retrofit): CartApi = retrofit.create(CartApi::class.java)

    @Provides
    @Singleton
    fun providesCartRepository(cartApi: CartApi): CartRepository = CartRepository(cartApi)

    @Provides
    @Singleton
    fun providesOrderApi(retrofit: Retrofit): OrderApi = retrofit.create(OrderApi::class.java)

    @Provides
    @Singleton
    fun providesOrderRepo(orderApi: OrderApi) = OrderRepository(orderApi)
}
