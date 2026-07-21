package ru.d3rvich.feature.detail.impl.browser

import android.content.Context
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import org.koin.core.annotation.Factory

@Factory
class BrowserManagerImpl(private val context: Context) : BrowserManager {
    private val intent = CustomTabsIntent.Builder().build().also {
        it.intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }

    override fun launchUrl(url: String) {
        intent.launchUrl(context, url.toUri())
    }
}