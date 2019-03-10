package com.badream.template.util

import android.util.Log
import com.badream.template.BuildConfig


class LogUtil{
    companion object {
        fun d(tag: String, str: String) {
            if (BuildConfig.DEBUG) {
                Log.d(tag, str)
            }
        }
        fun w(tag:String, str:String, e:Throwable){
            if( BuildConfig.DEBUG){
                Log.w(tag, str, e)
            }
        }
    }
}