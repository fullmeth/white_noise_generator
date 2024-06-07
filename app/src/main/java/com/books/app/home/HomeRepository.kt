package com.books.app.home

import com.books.app.common.model.JsonData
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeRepository(private val remoteConfig: FirebaseRemoteConfig) {

    private val _jsonData = MutableStateFlow(JsonData(emptyList(), emptyList(), emptyList()))
    val jsonData = _jsonData.asStateFlow()

    fun fetchBooks() {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) null
                val jsonData = Gson().fromJson(
                    remoteConfig.getValue("json_data").asString(),
                    JsonData::class.java
                )
                _jsonData.update { jsonData }
            }
    }
}