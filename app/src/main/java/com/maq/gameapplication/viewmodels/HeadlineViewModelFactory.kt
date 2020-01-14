package com.maq.propertyapp.properties

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.maq.gameapplication.HeadlineViewModel
import com.maq.gameapplication.data.HeadlineRepository

//viewmodel factory class to create view models

@Suppress("UNCHECKED_CAST")
class HeadlineViewModelFactory (
    private val repository: HeadlineRepository
) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HeadlineViewModel(repository) as T
    }

}