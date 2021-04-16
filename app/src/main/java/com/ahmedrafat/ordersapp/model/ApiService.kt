package com.ahmedrafat.ordersapp.model

import com.ahmedrafat.ordersapp.model.apimodels.OrdersModel
import com.ahmedrafat.ordersapp.model.apimodels.ShopperModel
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("api/Orders")
    fun getOrders(): Deferred<Response<List<OrdersModel>>>

    @GET("api/Shoppers")
    fun getShoppers(): Deferred<Response<List<ShopperModel>>>
}