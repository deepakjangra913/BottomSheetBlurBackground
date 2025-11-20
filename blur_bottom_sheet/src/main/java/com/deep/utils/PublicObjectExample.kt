package com.deep.utils

import android.util.Log

/**
 * This class should be visible to other
 * consumers
 * */
public object PublicObjectExample {

    public fun log(message: String){
        Log.i("PublicObjectExample: ", message)
    }
}