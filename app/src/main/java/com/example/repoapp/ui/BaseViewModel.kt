package com.example.repoapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

open class BaseViewModel<T>: ViewModel() {

    fun uiState(): LiveData<T> = repositoriesUiState
    protected val repositoriesUiState: MutableLiveData<T> = MutableLiveData()

    val uiDispatcher: CoroutineDispatcher = Dispatchers.Main
    val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
}