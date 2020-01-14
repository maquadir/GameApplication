package com.maq.propertyapp.network

import com.maq.gameapplication.data.Headline
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URl = "https://firebasestorage.googleapis.com/v0/b/nca-dna-apps-dev.appspot.com/o/"

//to read data from JSON url

interface HeadlineApi {


      @GET("game.json?")
      suspend fun getHeadlines(@Query(value = "alt")  alt:String,
                               @Query(value = "token")   token:String) : Response<Headline>

    companion object  {

        operator fun invoke(): HeadlineApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URl)
                .addConverterFactory(MoshiConverterFactory.create())
                .build().create(HeadlineApi::class.java)
        }
    }
}