package com.books.app.common

import com.books.app.details.ID_PLACEHOLDER

sealed class Destination(val route: String) {
    data object Splash : Destination(route = "splash")
    data object Home : Destination(route = "home")
    data object Details : Destination(route = "details/$ID_PLACEHOLDER")
}