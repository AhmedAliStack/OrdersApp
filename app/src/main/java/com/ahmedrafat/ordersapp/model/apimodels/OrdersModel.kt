package com.ahmedrafat.ordersapp.model.apimodels


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OrdersModel(
    @Json(name = "id")
    val id: Int? = 0,
    @Json(name = "items")
    val items: List<Item?>? = listOf(),
    @Json(name = "location")
    val location: Location? = Location(),
    @Json(name = "name")
    val name: String? = "",
    @Json(name = "phoneNumber")
    val phoneNumber: String? = "",
    @Json(name = "shopper")
    val shopper: String? = "",
    @Json(name = "timeToDeliver")
    val timeToDeliver: String? = ""
) {
    @JsonClass(generateAdapter = true)
    data class Item(
        @Json(name = "count")
        val count: Int? = 0,
        @Json(name = "name")
        val name: String? = ""
    )

    @JsonClass(generateAdapter = true)
    data class Location(
        @Json(name = "type")
        val type: Int? = 0,
        @Json(name = "value")
        val value: Value? = Value()
    ) {
        @JsonClass(generateAdapter = true)
        data class Value(
            @Json(name = "latitude")
            val latitude: Double? = 0.0,
            @Json(name = "longitude")
            val longitude: Double? = 0.0
        )
    }
}