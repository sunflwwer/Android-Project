// RetrofitConnection2.kt
package com.example.myapplication

import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// https://apis.data.go.kr/B554620/mdcnlPrntInfoService/getMdcnlPrntInfoList?serviceKey=DOC5gbTFMW4MJMNNLIeRJFhmq1nECkXilEccND7rm7J2cWnynmM9m3%2BwoMjaLxQ5Mq30uzQI%2BdvZogaW5RqtMw%3D%3D&pageNo=1&numOfRows=10&type=xml


class RetrofitConnection2 {
    companion object {

        private const val BASE_URL_Php = "http://192.168.123.104/" // 자신의 ipv4 주소 넣기
        var phpNetworkService: NetworkService2
        val phpRetrofit: Retrofit
            get() = Retrofit.Builder()
                .baseUrl(BASE_URL_Php)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        private const val BASE_URL = "https://apis.data.go.kr/B554620/mdcnlPrntInfoService/"

        var xmlNetworkService2: NetworkService2
        val parser2 = TikXml.Builder().exceptionOnUnreadXml(false).build()
        val xmlRetrofit2: Retrofit
            get() = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(TikXmlConverterFactory.create(parser2))
                .build()

        init {
            phpNetworkService = phpRetrofit.create(NetworkService2::class.java)
            xmlNetworkService2 = xmlRetrofit2.create(NetworkService2::class.java)
        }
    }


}
