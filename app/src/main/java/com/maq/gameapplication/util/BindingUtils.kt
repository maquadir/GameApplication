package com.maq.propertyapp.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide


//binding functions

//binding adapter to display image url
@BindingAdapter("image")
fun loadImage(view: ImageView, url: String) {

    var updatedUrl = url
    if(updatedUrl.contains("http")){
        updatedUrl = updatedUrl.replace("http","https",false)
    }

        Glide.with(view)
            .load(updatedUrl)
            .into(view)
}



