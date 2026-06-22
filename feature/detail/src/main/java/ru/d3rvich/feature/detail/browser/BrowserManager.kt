package ru.d3rvich.feature.detail.browser

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import org.koin.core.annotation.Factory

@Factory(binds = [BrowserManager::class])
class BrowserManager(private val context: Context) {
    private val intent = CustomTabsIntent.Builder().build().also {
        it.intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }

    fun launchUrl(url: Uri) {
        intent.launchUrl(context, url)
    }
}