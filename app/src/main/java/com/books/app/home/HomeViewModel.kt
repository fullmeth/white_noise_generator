package com.books.app.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.books.app.common.Consumable
import com.books.app.common.model.BannerSlide
import com.books.app.common.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    private var navigate: Unit? = null
        set(value) {
            field = value
            updateState()
        }

    private var bannerSlides: List<BannerSlide> = emptyList()
        set(value) {
            field = value
            updateState()
        }

    private var booksGroupedByGenre: Map<String, List<Book>> = emptyMap()
        set(value) {
            field = value
            updateState()
        }

    private val state
        get() = HomeUiState(
            bannerSlides = bannerSlides,
            booksGroupedByGenre = booksGroupedByGenre,
            navigate = navigate?.let { Consumable(it) { navigate = null } },
        )

    private val _homeUiState = MutableStateFlow(state)
    val homeUiState = _homeUiState.asStateFlow()

    private fun updateState() {
        _homeUiState.update { state }
    }

    fun fetchBooks() {
        repository.fetchBooks()
    }

    init {
        fetchBooks()
        viewModelScope.launch {
            repository.jsonData.collect {
                val books = it.books.groupBy { it.genre }
                booksGroupedByGenre = books
                bannerSlides = it.topBannerSlides
            }
        }
    }
}

data class HomeUiState(
    val bannerSlides: List<BannerSlide>?,
    val booksGroupedByGenre: Map<String, List<Book>>?,
    val navigate: Consumable<Unit>?,
)
