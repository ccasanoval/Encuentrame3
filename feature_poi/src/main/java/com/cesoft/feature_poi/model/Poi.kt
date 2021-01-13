package com.cesoft.feature_poi.model


data class Poi(
    val id: String,
    val name: String,
    val description: String,
    val timestamp: Long,
    val latitude: Float,
    val longitude: Float
) {

}