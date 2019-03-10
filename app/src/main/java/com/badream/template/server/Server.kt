package com.badream.template.server

import android.app.Activity
import android.os.Build
import android.widget.Toast
import com.badream.template.BaseActivity
import com.badream.template.BuildConfig
import com.badream.template.R
import com.badream.template.dto.ResultDto
import com.badream.template.util.LogUtil
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object Server {
    val BASE_URL = if (BuildConfig.DEBUG) {
        "https://api.github.com/"
    } else {
        "https://api.github.com/"
    }

    val api: InterfaceAPI
        get() {

            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(createOkHttpClient())
                    .build()

            val mInterfaceService = retrofit.create<InterfaceAPI>(InterfaceAPI::class.java)
            return mInterfaceService
        }


    //OkHttp를 이용해 로그를 받아보기
    private fun createOkHttpClient(): OkHttpClient {

//        val cacheSize = 10 * 1024 * 1024L // 10 MB
//        val cache = Cache(getCacheDir(), cacheSize)

        val builder = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor()

        interceptor.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }

        builder.addInterceptor(interceptor)
        builder.readTimeout(30, TimeUnit.SECONDS)
        builder.connectTimeout(15, TimeUnit.SECONDS)
//        builder.cache(cache)

        builder.addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain?): Response {
                try {
                    val original = chain!!.request()


                    val request = original.newBuilder()
                            .header("model", Build.MODEL)
                            .header("osver", Build.VERSION.RELEASE)
                            .header("appver", BuildConfig.VERSION_NAME)
                            .header("apptype", "android")
                            .method(original.method(), original.body())
                            .build()

                    return chain.proceed(request)
                } catch (e: Exception) {
                    e.toString()
                }
                return Response.Builder().build()
            }
        })

        return builder.build()
    }

    fun <T : ResultDto> callAPI(context: Activity, api: Flowable<T>, completion: (result: T) -> Boolean, failed: (err: Throwable) -> Boolean) {

        if (context is BaseActivity) {
            context.showProgressDialog()
        }

        api.subscribeOn(Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .retry(3)
                .subscribe(
                        { res ->
                            if (context is BaseActivity) {
                                context.hideProgressDialog()
                            }

                            if ((res as? ResultDto)?.res ?: false) {
                                completion(res)
                            } else {
                                Toast.makeText(context, (res as ResultDto).msg, Toast.LENGTH_SHORT).show()
                                failed(Throwable("error"))
                            }
                        },
                        { e ->
                            if (context is BaseActivity) {
                                context.hideProgressDialog()
                            }
                            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                            failed(e)
                        }
                )
    }


    fun <T> callListAPI(context: Activity, api: Flowable<ArrayList<T>>, completion: (result: ArrayList<T>) -> Boolean, failed: (err: Throwable) -> Boolean) {

        if (context is BaseActivity) {
            context.showProgressDialog()
        }

        api.subscribeOn(Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .retry(3)
                .subscribe(
                        { res ->
                            if (context is BaseActivity) {
                                context.hideProgressDialog()
                            }

                            if (res.size > 0 ) {
                                completion(res)
                            } else {
                                failed(Throwable("error"))
                            }
                        },
                        { e ->
                            if (context is BaseActivity) {
                                context.hideProgressDialog()
                            }
                            LogUtil.d("AAA", e.message.toString())
                            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                            failed(e)
                        }
                )
    }
}