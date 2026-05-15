package ru.d3rvich.jetgames.navigation.detail

import com.arkivanov.decompose.ComponentContext

class DefaultGameDetailComponent(
    componentContext: ComponentContext,
    override val gameId: Int,
    private val onClose: () -> Unit
) : GameDetailComponent, ComponentContext by componentContext {
    override fun onBackClick() {
        onClose()
    }
}