package com.example.finalapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {
    //http://apis.data.go.kr/1390802/AgriFood/FdFoodCkryImage
    // getKoreanFoodFdFoodCkryImageList
    @GET("getKoreanFoodFdFoodCkryImageList")
    fun getList(
        @Query("food_Name") q:String,
        @Query("serviceKey") apikey: String,
        @Query("Page_No") page: Long,
        @Query("Page_Size") pageSize: Int,
        @Query("service_Type") returnType: String
    ) : Call<MyModel>

    @GET("youtube/v3/search")
    fun getList(
        @Query("key") key:String,
        @Query("q") search_query : String,
        @Query("type") returnType : String,
        @Query("part") returnData : String
    ): Call<SearchListResponse>


}