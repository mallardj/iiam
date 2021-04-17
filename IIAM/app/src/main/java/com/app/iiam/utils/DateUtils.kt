package com.app.iiam.utils


import android.app.DatePickerDialog
import android.content.Context
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

val SIMPLE_DATE = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
val FORMATTED_DATE = "EEEE, dd/MM/yyyy, hh:mm a"
val NOTE_SIMPLE_DATE = "yyyy-MM-dd"
val NOTE_FORMATTED_DATE = "MM/dd/yyyy"
val YEAR_FORMATTED = "yyyy"
val MONTH_FORMATTED = "MM"
val DAY_FORMATTED = "dd"

fun String.convertDate(inFormat: String, outFormat: String, setDate: String): String {
    val inDateFormat = SimpleDateFormat(inFormat, Locale.getDefault())
    val outputFormat = SimpleDateFormat(outFormat, Locale.getDefault())
    var date = Date()
    var output = ""
    try {
        date = inDateFormat.parse(setDate)
        output = outputFormat.format(date)
        return output
    } catch (pe: ParseException) {
        pe.printStackTrace()
    }
    return output
}

fun String.setDate(date: Long, inFormat: String): String {
    var output = ""
    try {
        val sdf = SimpleDateFormat(inFormat, Locale.getDefault())
        val netDate = Date(date)
        output = sdf.format(netDate)
    } catch (pe: ParseException) {
        pe.printStackTrace()
    }
    return output
}

fun String.getYear(date: Long): Int {
    var output = 0
    try {
        val sdf = SimpleDateFormat(YEAR_FORMATTED, Locale.getDefault())
        val netDate = Date(date)
        output = sdf.format(netDate).toInt()
    } catch (pe: ParseException) {
        pe.printStackTrace()
    }
    return output
}

fun String.getMonth(date: Long): Int {
    var output = 0
    try {
        val sdf = SimpleDateFormat(MONTH_FORMATTED, Locale.getDefault())
        val netDate = Date(date)
        output = sdf.format(netDate).toInt()
    } catch (pe: ParseException) {
        pe.printStackTrace()
    }
    return output
}

fun String.getDay(date: Long): Int {
    var output = 0
    try {
        val sdf = SimpleDateFormat(DAY_FORMATTED, Locale.getDefault())
        val netDate = Date(date)
        output = sdf.format(netDate).toInt()
    } catch (pe: ParseException) {
        pe.printStackTrace()
    }
    return output
}