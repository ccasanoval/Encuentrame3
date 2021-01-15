package com.cesoft.feature_poi.repo

import android.util.Log
import com.cesoft.feature_geocommon.Cloud
import com.cesoft.feature_poi.model.Poi
import com.cesoft.feature_poi.repo.Lugar
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

typealias ResList = Pair<List<Poi>?, Exception?>
object Repo {
    private const val tag = "Repo"
    private const val NAME = "lugar"
    private const val GEO = "geo"

    private fun toLocal(lugar: Lugar): Poi =
        Poi(lugar.id, lugar.nombre, lugar.descripcion, lugar.fechaLong, lugar.latitud, lugar.longitud)

    suspend fun list(): ResList {
        return suspendCoroutine { continuation ->
            val list = listOf<Poi>(
                Poi("1", "POI 1", "DESC 1", Date().time-55000, 40.0, -3.40),
                Poi("2", "POI 2", "DESC 2", Date().time-44000, 41.0, -3.41),
                Poi("3", "POI 3", "DESC 3", Date().time-33000, 42.0, -3.42)
            )
            continuation.resume(ResList(list, null))
        }
    }
}