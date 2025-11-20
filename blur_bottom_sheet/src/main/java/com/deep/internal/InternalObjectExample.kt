package com.deep.internal

import android.util.Log

/**
 * This class should not be visible to other
 * consumers
 * */
internal object InternalObjectExample {

    fun log(message: String){
        Log.i("InternalObjectExample", message)
    }
}