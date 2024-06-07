package com.books.app.details

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import com.books.app.common.Destination


fun NavController.bookDetails(bookId: String) {
    navigate(Destination.Details.route.replace(ID_PLACEHOLDER, bookId))
}

private const val BOOK_ID_ARG = "bookId"
const val ID_PLACEHOLDER = "{$BOOK_ID_ARG}"

internal class BookArgs(val bookId: String? = null) {
    constructor(savedStateHandle: SavedStateHandle) : this(savedStateHandle[BOOK_ID_ARG])
}
