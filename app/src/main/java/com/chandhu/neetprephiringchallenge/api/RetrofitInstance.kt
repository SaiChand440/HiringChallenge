package com.chandhu.neetprephiringchallenge.api

import android.util.Log
import com.chandhu.neetprephiringchallenge.NewsApplication
import com.chandhu.neetprephiringchallenge.utils.Constant.Companion.BASE_URL
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitInstance {

    companion object {
        private val retrofit by lazy {

            val log = HttpLoggingInterceptor()
            log.setLevel(HttpLoggingInterceptor.Level.BODY)
            Log.e("TAG",log.toString())
            val client = OkHttpClient.Builder()
                .cache(cache())
                .addInterceptor(log)
                .addNetworkInterceptor(CacheInterceptor())
                .addInterceptor(ForceCacheInterceptor())
                .build()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        private fun cache(): Cache {
            val cached = NewsApplication().getAppInstance().cacheDir
            return Cache(cached,(5*1024*1024).toLong())
        }

        val api by lazy {
            retrofit.create(NewsAPI::class.java)
        }
    }

    class CacheInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val response: Response = chain.proceed(chain.request())
            val cacheControl = CacheControl.Builder()
                .maxAge(1, TimeUnit.MINUTES)
                .build()
            return response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", cacheControl.toString())
                .build()
        }
    }

    class ForceCacheInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val builder: Request.Builder = chain.request().newBuilder()
            if (!NewsApplication().hasNetwork()) {
                builder.cacheControl(CacheControl.FORCE_CACHE)
            }
            return chain.proceed(builder.build())
        }
    }
}


