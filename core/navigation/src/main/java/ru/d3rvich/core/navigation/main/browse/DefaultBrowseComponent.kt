package ru.d3rvich.core.navigation.main.browse

import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.jetpackcomponentcontext.JetpackComponentContext
import com.arkivanov.decompose.jetpackcomponentcontext.viewModel
import org.koin.core.component.KoinComponent
import ru.d3rvich.feature.browse.BrowseViewModel

@OptIn(ExperimentalDecomposeApi::class)
class DefaultBrowseComponent(componentContext: JetpackComponentContext) : BrowseComponent,
    KoinComponent, JetpackComponentContext by componentContext {
    override val browseViewModel: BrowseViewModel = viewModel { getKoin().get() }
}