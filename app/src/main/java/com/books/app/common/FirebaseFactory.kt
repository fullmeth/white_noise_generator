package com.books.app.common

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

fun makeFirebaseRemoteConfigClient(context: Context): FirebaseRemoteConfig {
    FirebaseApp.initializeApp(context)
    val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
    val configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = 3600
    }
    remoteConfig.setConfigSettingsAsync(configSettings)
    return remoteConfig
}