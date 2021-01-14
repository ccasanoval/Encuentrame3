package com.cesoft.feature_poi.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep

@Keep
data class Poi(
    val id: String,
    val name: String,
    val description: String,
    val timestamp: Long,
    val latitude: Double,
    val longitude: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readDouble(),
        parcel.readDouble()) {
    }
    override fun describeContents(): Int = 0
    override fun writeToParcel(p: Parcel?, p1: Int) {
        p?.writeString(id)
        p?.writeString(name)
        p?.writeString(description)
        p?.writeLong(timestamp)
        p?.writeDouble(latitude)
        p?.writeDouble(longitude)
    }

    companion object CREATOR : Parcelable.Creator<Poi> {
        override fun createFromParcel(parcel: Parcel): Poi {
            return Poi(parcel)
        }
        override fun newArray(size: Int): Array<Poi?> {
            return arrayOfNulls(size)
        }
    }

}