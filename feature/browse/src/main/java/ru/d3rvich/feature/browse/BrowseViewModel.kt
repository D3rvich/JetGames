package ru.d3rvich.feature.browse

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.koin.core.annotation.KoinViewModel
import ru.d3rvich.core.domain.model.LoadingResult
import ru.d3rvich.core.domain.usecases.GetGenresUseCase
import ru.d3rvich.core.domain.usecases.GetPlatformsUseCase

/**
 * Created by Ilya Deryabin at 05.06.2024
 */
@Stable
@KoinViewModel
class BrowseViewModel(
    getGenresUseCase: GetGenresUseCase,
    getPlatformsUseCase: GetPlatformsUseCase,
) : ViewModel() {

    val genres = getGenresUseCase.invoke().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LoadingResult.Loading
    )

    val platforms = getPlatformsUseCase.invoke().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LoadingResult.Loading
    )
}