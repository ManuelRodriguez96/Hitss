package com.hitss.ui.launch

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LaunchViewModel : ViewModel() {

    private val launchPrivate = MutableLiveData<Boolean>()
    val launch : LiveData<Boolean> get() = launchPrivate

     init {
         Handler(Looper.getMainLooper()).postDelayed({
             launchPrivate.value = true
         }, 2500.toLong())
    }
}