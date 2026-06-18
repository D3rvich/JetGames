package ru.d3rvich.jetgames.navigation.filter

import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.jetpackcomponentcontext.JetpackComponentContext
import com.arkivanov.decompose.jetpackcomponentcontext.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.d3rvich.feature.filter.FilterViewModel

@OptIn(ExperimentalDecomposeApi::class)
class DefaultFilterComponent(componentContext: JetpackComponentContext, private val onClose: () -> Unit) :
    FilterComponent, KoinComponent, JetpackComponentContext by componentContext {
    override val filterViewModel: FilterViewModel = viewModel { get() }

    override fun onBackClick() {
        onClose()
    }
}