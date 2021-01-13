package com.cesoft.feature_geocommon

import java.text.SimpleDateFormat
import java.util.*

object Util {
    fun getDate(milliSeconds: Long, dateFormat: String="dd/MM/yyyy hh:mm"): String? {
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }
}