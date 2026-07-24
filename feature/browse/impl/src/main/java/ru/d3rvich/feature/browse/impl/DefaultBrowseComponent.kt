package ru.d3rvich.feature.browse.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import ru.d3rvich.core.domain.usecases.GetGenresUseCase
import ru.d3rvich.core.domain.usecases.GetPlatformsUseCase
import ru.d3rvich.core.entity.GenreFullEntity
import ru.d3rvich.core.entity.PlatformEntity
import ru.d3rvich.core.model.Result
import ru.d3rvich.core.model.map
import ru.d3rvich.feature.browse.api.BrowseComponent

@Factory(binds = [DefaultBrowseComponent::class])
internal class DefaultBrowseComponent(
    @InjectedParam context: ComponentContext,
    getGenresUseCase: GetGenresUseCase,
    getPlatformsUseCase: GetPlatformsUseCase,
) : BrowseComponent, ComponentContext by context {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _genres: MutableValue<Result<ImmutableList<GenreFullEntity>>> =
        MutableValue(Result.Loading)
    private val _platforms: MutableValue<Result<ImmutableList<PlatformEntity>>> =
        MutableValue(Result.Loading)

    init {
        doOnDestroy(scope::cancel)

        getGenresUseCase().onEach { result ->
            _genres.value = result.map { it.toImmutableList() }
        }.launchIn(scope)

        getPlatformsUseCase().onEach { result ->
            _platforms.value = result.map { it.toImmutableList() }
        }.launchIn(scope)
    }

    override val genres: Value<Result<ImmutableList<GenreFullEntity>>> = _genres
    override val platforms: Value<Result<ImmutableList<PlatformEntity>>> = _platforms
}
