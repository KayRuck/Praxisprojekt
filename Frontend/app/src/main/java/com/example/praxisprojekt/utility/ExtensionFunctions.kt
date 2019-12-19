package com.example.praxisprojekt.utility

import androidx.lifecycle.MutableLiveData

fun MutableLiveData<Boolean>.dataAvailable() = this.postValue(true)
