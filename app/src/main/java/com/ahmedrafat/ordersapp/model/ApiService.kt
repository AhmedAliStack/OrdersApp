package com.ahmedrafat.ordersapp.model

import com.ahmedrafat.ordersapp.model.apimodels.OrdersModel
import com.ahmedrafat.ordersapp.model.apimodels.ReviewModel
import com.ahmedrafat.ordersapp.model.apimodels.ShopperModel
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("api/Orders")
    fun getOrders(): Deferred<Response<List<OrdersModel>>>

    @GET("api/Shoppers")
    fun getShoppers(): Deferred<Response<List<ShopperModel>>>

    @GET("api/Reviews")
    fun getReviews(): Deferred<Response<ReviewModel>>

    @FormUrlEncoded
    @POST("api/Reviews")
    fun submitReview(@FieldMap params:HashMap<String,String>):Deferred<Response<ReviewModel.ReviewModelItem>>
}