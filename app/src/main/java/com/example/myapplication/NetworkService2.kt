package com.example.myapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// https://apis.data.go.kr/B554620/mdcnlPrntInfoService/getMdcnlPrntInfoList?serviceKey=DOC5gbTFMW4MJMNNLIeRJFhmq1nECkXilEccND7rm7J2cWnynmM9m3%2BwoMjaLxQ5Mq30uzQI%2BdvZogaW5RqtMw%3D%3D&pageNo=1&numOfRows=10&type=xml


interface NetworkService2 {

    @GET("PHP_plant_connection.php")
    fun getPhpList(
        @Query("name") name: String
    ): Call<PhpResponse>

    @GET("getMdcnlPrntInfoList")
    fun getXmlList(
        @Query("srchKrnm") name: String,
        @Query("serviceKey") apiKey: String,
        @Query("pageNo") pageNo: Int,
        @Query("numOfRows") numOfRows: Int,
        @Query("type") returnType: String,
    ): Call<XmlResponse2>

}
