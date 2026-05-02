package ru.d3rvich.jetgames.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.contains
import androidx.navigation3.runtime.metadata
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope

data class OverlayScene<T : Any>(
    override val key: Any,
    override val previousEntries: List<NavEntry<T>>,
    val hostEntry: NavEntry<T>,
    val overlayEntry: NavEntry<T>?,
) : Scene<T> {
    override val entries: List<NavEntry<T>>
        get() = overlayEntry?.let { listOf(hostEntry, overlayEntry) } ?: listOf(hostEntry)
    override val content: @Composable (() -> Unit) = {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            hostEntry.Content()

            AnimatedContent(
                targetState = overlayEntry,
                transitionSpec = { fadeIn() togetherWith fadeOut() }) { entry ->
                Box(Modifier.fillMaxSize()) {
                    entry?.Content()
                }
            }
        }
    }

    companion object {
        fun hostKey() = metadata {
            put(HostKey, true)
        }

        fun overlayKey() = metadata {
            put(OverlayKey, true)
        }
    }

    object HostKey : NavMetadataKey<Boolean>

    object OverlayKey : NavMetadataKey<Boolean>
}

@Composable
fun <T : Any> rememberOverlaySceneStrategy(): OverlaySceneStrategy<T> =
    remember {
        OverlaySceneStrategy()
    }

class OverlaySceneStrategy<T : Any> : SceneStrategy<T> {
    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        val hostEntry =
            entries.findLast { it.metadata.contains(OverlayScene.HostKey) } ?: return null
        val overlayEntry =
            entries.lastOrNull { it.metadata.contains(OverlayScene.OverlayKey) }
        val key = hostEntry.contentKey
        return OverlayScene(
            key = key,
            previousEntries = entries.dropLast(1),
            hostEntry = hostEntry,
            overlayEntry = overlayEntry
        )
    }
}