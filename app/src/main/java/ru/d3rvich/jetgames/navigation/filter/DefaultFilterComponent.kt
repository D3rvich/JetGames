package ru.d3rvich.jetgames.navigation.filter

import com.arkivanov.decompose.ComponentContext

class DefaultFilterComponent(componentContext: ComponentContext, private val onClose: () -> Unit) :
    FilterComponent, ComponentContext by componentContext {
    override fun onBackClick() {
        onClose()
    }
}