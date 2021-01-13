package com.cesoft.feature_poi.repo

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.firebase.database.IgnoreExtraProperties
import java.util.*


@Keep
@IgnoreExtraProperties
class Lugar() : Parcelable {

    //
    //NOTE: Firebase needs public field or public getter/setter, if use @Exclude that's like private...
    //
    var id: String = ""
    var nombre: String = ""
    var descripcion: String = ""
    var fecha = Date() //TODO:DEL: Old field, dont remove for old objects stored on firebase...
    var fechaLong: Long = Date().time
    var latitud = 0.0
    var longitud= 0.0

    fun checkDateAndCorrect() {
        val fechaTmp = Date(fechaLong)
        if (fechaTmp.compareTo(fecha) != 0) {
            fechaLong = fecha.time
        }
    }

    constructor(parcel: Parcel) : this() {
        id = parcel.readString().toString()
        nombre = parcel.readString().toString()
        descripcion = parcel.readString().toString()
        latitud = parcel.readDouble()
        longitud = parcel.readDouble()
        fechaLong = parcel.readLong()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(nombre)
        parcel.writeString(descripcion)
        parcel.writeDouble(latitud)
        parcel.writeDouble(longitud)
        parcel.writeLong(fechaLong)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Lugar> {
        override fun createFromParcel(parcel: Parcel): Lugar {
            return Lugar(parcel)
        }

        override fun newArray(size: Int): Array<Lugar?> {
            return arrayOfNulls(size)
        }
    }
}