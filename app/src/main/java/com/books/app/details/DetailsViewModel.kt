package com.books.app.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.books.app.common.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val repository: DetailsRepository,
    savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    private var bookId = BookArgs(savedStateHandle).bookId?.toInt() ?: 0
        set(value) {
            field = value
            updateState()
        }

    private var books: List<Book> = emptyList()
        set(value) {
            field = value
            updateState()
        }

    private var recommendation: List<Int> = emptyList()
        set(value) {
            field = value
            updateState()
        }

    private val state
        get() = DetailsUiState(
            currentBookId = bookId,
            books = books,
            recommendation = recommendation,
            onNewBookSelected = { bookId = it }
        )

    private val _detailsUiState = MutableStateFlow(state)
    val detailsUiState = _detailsUiState.asStateFlow()

    private fun updateState() {
        _detailsUiState.update { state }
    }

    private fun fetchBooks() {
        repository.fetchDetailsCarousel()
    }

    init {
        fetchBooks()
        viewModelScope.launch {
            repository.detailsCarousel.collect {
                Log.e("tete", it.youWillLikeSection.toString())
                books = it.books
                recommendation = it.youWillLikeSection
            }
        }
    }
}

data class DetailsUiState(
    val currentBookId: Int,
    val books: List<Book>,
    val recommendation: List<Int>,
    val onNewBookSelected: (Int) -> Unit
)
