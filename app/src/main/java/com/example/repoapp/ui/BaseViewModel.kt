package com.example.repoapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel<T>: ViewModel() {

    fun uiState(): LiveData<T> = repositoriesUiState
    protected val repositoriesUiState: MutableLiveData<T> = MutableLiveData()
}