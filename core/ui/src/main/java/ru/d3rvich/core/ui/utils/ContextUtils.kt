package ru.d3rvich.core.ui.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

fun Context.findActivity(): Activity = when (this) {
    !is ContextWrapper -> throw IllegalArgumentException("Activity not found.")
    is Activity -> this
    else -> this.baseContext.findActivity()
}