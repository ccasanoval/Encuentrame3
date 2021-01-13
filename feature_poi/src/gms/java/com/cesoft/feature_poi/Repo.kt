package com.cesoft.feature_poi

import android.util.Log
import com.cesoft.feature_geocommon.Cloud
import com.cesoft.feature_poi.model.Poi
import com.cesoft.feature_poi.repo.Lugar
import com.firebase.geofire.GeoFire
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

typealias ResList = Pair<List<Poi>?, Exception?>
object Repo {
    private const val tag = "Repo"
    private const val NAME = "lugar"
    private const val GEO = "geo"

    private fun getDatabase(): DatabaseReference {
        return Cloud.getDatabase().child(NAME)
    }
    private fun getGeoDatabase(): GeoFire {
        return GeoFire(Cloud.getDatabase().child(GEO).child(NAME))
    }
    private val data: DatabaseReference? = null

    private fun toLocal(lugar: Lugar): Poi =
        Poi(lugar.id, lugar.nombre, lugar.descripcion, lugar.fechaLong, lugar.latitud, lugar.longitud)

    suspend fun list(): ResList {
        return suspendCoroutine { continuation ->
            val ref: DatabaseReference = getDatabase()
            val vel = object : ValueEventListener {
                override fun onDataChange(data: DataSnapshot) {
                    val n = data.childrenCount
                    val lugares = ArrayList<Poi>(n.toInt())
                    for(o in data.children) {
                        try {
                            val lugar: Lugar? = o.getValue(Lugar::class.java)
                            if (lugar != null) {
                                lugar.checkDateAndCorrect()
                                //Log.e(tag, "list:-----${lugar.nombre}---------${lugar.fechaLong} : ${lugar.fecha}")
                                lugares.add(toLocal(lugar))
                            }
                        } catch (e: Exception) {
                            Log.e(tag, "list:onDataChange:e:-----------------------------------", e)
                        }
                    }
                    try {
                        continuation.resume(Pair(lugares.asReversed(), null))
                    }
                    catch(e: Exception) {
                        Log.e(tag, "list:e:----------------------------------------------------", e)
                        continuation.resume(Pair(null, e))
                    }
                }
                override fun onCancelled(err: DatabaseError) {
                    Log.e(tag, "list:onCancelled:$err")
                    continuation.resume(Pair(null, Exception(err.message)))
                }
            }
            ref.addValueEventListener(vel)
        }
    }
}