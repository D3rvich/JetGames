package ru.d3rvich.feature.favorites.store

import com.arkivanov.mvikotlin.core.store.Store

internal interface FavoritesStore : Store<Nothing, FavoritesState, Nothing>