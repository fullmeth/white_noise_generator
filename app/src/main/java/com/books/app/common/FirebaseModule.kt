package com.books.app.common

import org.koin.dsl.module

val firebaseModule = module {
    single { makeFirebaseRemoteConfigClient(get()) }
}