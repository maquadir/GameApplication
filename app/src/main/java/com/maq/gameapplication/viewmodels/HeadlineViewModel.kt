package com.maq.gameapplication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maq.gameapplication.data.Headline
import com.maq.gameapplication.data.HeadlineRepository
import com.maq.propertyapp.util.Coroutines
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class HeadlineViewModel(val repository: HeadlineRepository) : ViewModel() {

    private lateinit var job: Job

    private val _headlines = MutableLiveData<Headline>()
    private val _headlinesfetch = MutableLiveData<Headline>()
    val headlines : LiveData<Headline>
                get() = _headlines

    val headlinesfetch : LiveData<Headline>
        get() = _headlinesfetch

    init{
        Log.i("View Model","View Model created")
    }

    //to keep track of lifetime of view model
    override fun onCleared() {
        super.onCleared()
        Log.i("View Model","View Model destroyed")
        if(::job.isInitialized) job.cancel()
    }

    //coroutines - get data from repository
     fun getHeadlines(){

        job =

         viewModelScope.launch {
            Coroutines.ioThenMain(
                { repository.getHeadlines() },
         { _headlines.value = it }
         )
            }
    }

    //function to insert data to Room database
    fun insertHeadlines(headline: Headline){

        job =

            viewModelScope.launch {
                Coroutines.ioThenMain(
                    { repository.insertHeadlines(headline) }
                )
            }
    }

    //function to get data from Room database
    fun getHeadlinesDb(){

        job =

            viewModelScope.launch {
                Coroutines.ioThenMain(
                    { repository.getHeadlinesDb() },
                    { _headlinesfetch.value = it }
                )
            }
    }






}