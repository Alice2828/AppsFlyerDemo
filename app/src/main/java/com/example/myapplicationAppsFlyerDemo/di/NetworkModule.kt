package com.example.myapplicationAppsFlyerDemo.di

import com.example.myapplicationAppsFlyerDemo.DeepLinkViewModel
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.core.module.dsl.DefinitionOptions
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.new
import org.koin.core.module.dsl.onOptions
import org.koin.dsl.module


val appViewModelModule = module {
    factoryOf(::DeepLinkViewModel)
}

fun getAppDIModule() = arrayListOf(appViewModelModule)

inline fun <reified R, reified T1, reified T2, reified T3, reified T4, reified T5> Module.factoryOf(
    crossinline constructor: (T1, T2, T3, T4, T5) -> R,
    noinline options: DefinitionOptions<R>? = null,
): KoinDefinition<R> = factory { new(constructor) }.onOptions(options)