package com.books.app.details

import com.books.app.common.model.JsonData
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DetailsRepository(private val remoteConfig: FirebaseRemoteConfig) {

    private val _detailsCarousel = MutableStateFlow(JsonData(emptyList(), emptyList(), emptyList()))
    val detailsCarousel = _detailsCarousel.asStateFlow()

    fun fetchDetailsCarousel() {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) null
                val detailsCarousel = Gson().fromJson(
                    remoteConfig.getValue("json_data").asString(),
                    JsonData::class.java
                )
                _detailsCarousel.update { detailsCarousel }
            }
    }
}