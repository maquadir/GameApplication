package com.maq.propertyapp.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


//kotlin coroutines

object Coroutines {

    //coroutines with a callback parameter
    fun<T: Any> ioThenMain(work: suspend (() -> T?), callback: ((T?)->Unit)) =
        CoroutineScope(Dispatchers.Main).launch {
            val data = CoroutineScope(Dispatchers.IO).async  rt@{
                return@rt work()
            }.await()
            callback(data)
        }


    //coroutines with a single parameter used during inserting data
    fun<T: Any> ioThenMain(work: suspend (() -> T?)) =
        CoroutineScope(Dispatchers.Main).launch {
            CoroutineScope(Dispatchers.IO).async  rt@{
                return@rt work()
            }.await()

        }



}