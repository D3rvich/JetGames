package ru.d3rvich.detail.browser

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

internal class BrowserManager(private val context: Context) {
    private val intent = CustomTabsIntent.Builder().build().also {
        it.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    fun launchUrl(url: Uri) {
        intent.launchUrl(context, url)
    }
}