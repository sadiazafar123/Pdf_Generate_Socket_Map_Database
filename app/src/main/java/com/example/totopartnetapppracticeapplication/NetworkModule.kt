package com.example.totopartnetapppracticeapplication

import com.example.totopartnetapppracticeapplication.api.ApiInterface
import com.example.totopartnetapppracticeapplication.util.Urls.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    factory { provideOkHttpClient() }
    factory { provideRetrofitInterface(get()) }
    single { provideRetrofit(get()) }
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun provideOkHttpClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
    return OkHttpClient.Builder()
        .addInterceptor(interceptor.setLevel(HttpLoggingInterceptor.Level.BODY))
        .addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
            chain.proceed(request.build())
        }
        .connectTimeout(90, TimeUnit.SECONDS)
        .writeTimeout(90, TimeUnit.SECONDS)
        .readTimeout(90, TimeUnit.SECONDS)
        .cache(null)
        .build()
}
fun provideRetrofitInterface(retrofit: Retrofit): ApiInterface= retrofit.create(ApiInterface::class.java)