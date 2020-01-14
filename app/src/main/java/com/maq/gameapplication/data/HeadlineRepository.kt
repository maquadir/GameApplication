package com.maq.gameapplication.data

import com.maq.gameapplication.database.Headlinedao
import com.maq.gameapplication.network.SafeApiRequest
import com.maq.propertyapp.network.HeadlineApi


class HeadlineRepository(
    private val api: HeadlineApi,
    private val headlinedao: Headlinedao
) : SafeApiRequest() {


    suspend fun getHeadlines() = apiRequest { api.getHeadlines("media","e36c1a14-25d9-4467-8383-a53f57ba6bfe") }

    fun insertHeadlines(headline: Headline) =  headlinedao.insertAll(headline)

    fun getHeadlinesDb() =  headlinedao.getAll()


}