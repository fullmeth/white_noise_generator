package com.books.app.common

import com.books.app.details.DetailsRepository
import com.books.app.details.DetailsViewModel
import com.books.app.home.HomeRepository
import com.books.app.home.HomeViewModel
import com.books.app.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val modules = module {
    singleOf(::HomeRepository)
    singleOf(::DetailsRepository)
    viewModelOf(::HomeViewModel)
    viewModelOf(::SplashViewModel)
    viewModelOf(::DetailsViewModel)
}