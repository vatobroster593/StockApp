package com.stockapp.ui.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Double.toDollarString(): String = "$%.2f".format(this)

fun Long.toDateString(): String =
    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(this))

fun Long.toDateTimeString(): String =
    SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(this))
