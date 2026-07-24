package ru.d3rvich.core.di

import org.koin.core.KoinApplication
import org.koin.plugin.module.dsl.startKoin

fun initKoin(appDeclaration: (KoinApplication.() -> Unit)? = null) {
    startKoin<JetGamesApplication> {
        appDeclaration?.invoke(this)
    }
}