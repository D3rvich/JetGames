package ru.d3rvich.feature.home.impl.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import ru.d3rvich.core.model.ListDisplayOption
import ru.d3rvich.core.ui.utils.asValue
import ru.d3rvich.feature.home.api.HomeComponent
import ru.d3rvich.feature.home.api.HomeComponent.Model
import ru.d3rvich.feature.home.impl.store.HomeStore
import ru.d3rvich.feature.home.impl.store.HomeStoreFactory

internal class DefaultHomeComponent(
    componentContext: ComponentContext,
    storeFactory: HomeStoreFactory,
    private val output: (HomeComponent.Output) -> Unit
) : HomeComponent, ComponentContext by componentContext {

    private val store: HomeStore = instanceKeeper.getStore { storeFactory.create() }

    override val model: Value<Model> = store.asValue().map(stateToModel)

    override fun openGameDetail(gameId: Int) {
        output(HomeComponent.Output.OpenGameDetail(gameId))
    }

    override fun openSettings() {
        output(HomeComponent.Output.OpenSettings)
    }

    override fun openFilter() {
        output(HomeComponent.Output.OpenFilter)
    }

    override fun refresh() {
        store.accept(HomeStore.Intent.Refresh)
    }

    override fun setSearch(text: String) {
        store.accept(HomeStore.Intent.SearchChange(text))
    }

    override fun setListVDisplayOption(listDisplayOption: ListDisplayOption) {
        store.accept(HomeStore.Intent.ListDisplayChange(listDisplayOption))
    }
}
