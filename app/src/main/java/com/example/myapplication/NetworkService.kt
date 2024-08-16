package com.example.myapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {
    @GET("getPrtcPrntInfoList")
    fun getXmlList(
        @Query("srchKrnm") name: String,
        @Query("pageNo") pageNo: Int,
        @Query("numOfRows") numOfRows: Int,
        @Query("type") returnType: String,
        @Query("serviceKey") apiKey: String
    ): Call<XmlResponse>
}