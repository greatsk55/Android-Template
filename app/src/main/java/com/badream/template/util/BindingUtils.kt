package com.badream.template.util

import android.databinding.BindingAdapter
import android.databinding.InverseBindingAdapter
import android.widget.TextView


@BindingAdapter("longText")
fun setLong(view : TextView, value : Long?) {
    if( value == null ){ view.text = "" }
    else { view.text =  value.toString()}
}

//@InverseBindingAdapter(attribute = "android:text")
//fun getFloat( view : TextView) : Long {
//    var num = view.text.toString()
//
//    if(num.isEmpty()) return 0
//    try {
//        return num.toLong()
//    } catch (e : Exception) {
//        return 0
//    }
//}