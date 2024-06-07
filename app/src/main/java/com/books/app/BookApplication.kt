package com.books.app

import android.app.Application
import com.books.app.common.firebaseModule
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class BookApplication : Application() {
    override fun onCreate() {
        FirebaseApp.initializeApp(this)
        super.onCreate()
        startKoin {
            androidContext(this@BookApplication)
            modules(com.books.app.common.modules, firebaseModule)
        }
    }
}