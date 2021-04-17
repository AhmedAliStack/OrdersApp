package com.ahmedrafat.ordersapp.model.apimodels


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

class ReviewModel : ArrayList<ReviewModel.ReviewModelItem>(){
    @JsonClass(generateAdapter = true)
    data class ReviewModelItem(
        @Json(name = "id")
        val id: String? = "",
        @Json(name = "message")
        val message: String? = "",
        @Json(name = "rateValue")
        val rateValue : Any?= 0,
        @Json(name = "rating")
        val rating: Any? = 0,
        @Json(name = "Review")
        val review: Any? = 0
    )
}