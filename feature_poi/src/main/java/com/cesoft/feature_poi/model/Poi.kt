package com.cesoft.feature_poi.model


data class Poi(
    val id: String,
    val name: String,
    val description: String,
    val timestamp: Long,
    val latitude: Double,
    val longitude: Double
) {

}