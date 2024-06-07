package com.books.app.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.books.app.R
import com.books.app.common.model.BannerSlide
import com.books.app.ui.theme.TestTaskBookAppTheme
import com.books.app.ui.theme.nunitoSansFontFamily
import kotlinx.coroutines.delay

private const val INFINITE_SCROLL_COUNT = 100_000
private const val INITIAL_PAGE = 50_001

@Composable
fun HomeScreen(
    state: HomeUiState,
    onBookClicked: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 24.dp)
    ) {
        item {
            Text(
                text = "Library",
                color = MaterialTheme.colorScheme.onSecondary,
                fontFamily = nunitoSansFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(28.dp))
            Banner(
                state.bannerSlides ?: emptyList(),
                onBannerCLick = { onBookClicked(it.toString()) })
            Spacer(modifier = Modifier.height(40.dp))
        }
        state.booksGroupedByGenre?.let {
            items(state.booksGroupedByGenre.keys.size) {
                val currentKey = state.booksGroupedByGenre.keys.elementAt(it)
                Text(
                    text = currentKey,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = nunitoSansFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(14.dp))
                LazyRow(contentPadding = PaddingValues(end = 8.dp)) {
                    state.booksGroupedByGenre.get(currentKey)?.let { value ->
                        items(value.size) {
                            BookItem(
                                coverUrl = value[it].coverUrl,
                                bookId = value[it].id.toString(),
                                name = value[it].name,
                                onBookClicked = onBookClicked
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

        }

    }
}

@Composable
fun BookItem(coverUrl: String, bookId: String, name: String, onBookClicked: (String) -> Unit) {
    Column(
        modifier = Modifier
            .width(120.dp)
            .padding(end = 8.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(coverUrl)
                .memoryCacheKey(coverUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.bg_placeholder),
            error = painterResource(R.drawable.bg_placeholder),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(150.dp)
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .clickable { onBookClicked(bookId) }
        )
        Text(
            text = name,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = .7f),
            fontFamily = nunitoSansFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Banner(bannerSlide: List<BannerSlide>, onBannerCLick: (Int) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
    ) {
        val pageCount = bannerSlide.count()
        val pagerState =
            rememberPagerState(pageCount = { INFINITE_SCROLL_COUNT }, initialPage = INITIAL_PAGE)
        CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
            HorizontalPager(
                modifier = Modifier.fillMaxWidth(),
                beyondBoundsPageCount = 2,
                pageSpacing = 16.dp,
                state = pagerState
            ) { index ->
                if (bannerSlide.isNotEmpty()) {
                    bannerSlide.getOrNull(
                        index % (bannerSlide.size)
                    )?.let {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(it.cover)
                                .memoryCacheKey(it.cover)
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(R.drawable.bg_placeholder),
                            error = painterResource(R.drawable.bg_placeholder),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { onBannerCLick(it.bookId) }
                        )
                    }
                }
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pageCount) { iteration ->
                val color =
                    if (pagerState.currentPage % (bannerSlide.size) == iteration) Color(0xFFD0006E) else Color(
                        0xFFC1C2CA
                    )
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(7.dp)
                )
            }
        }
        LaunchedEffect(key1 = Unit, block = {
            if (bannerSlide.isEmpty()) return@LaunchedEffect
            var initPage = INFINITE_SCROLL_COUNT / 2
            while (initPage % bannerSlide.size != 0) {
                initPage++
            }
            pagerState.scrollToPage(initPage)

        })
        LaunchedEffect(key1 = pagerState.currentPage) {
            delay(3000)
            pagerState.scrollToPage(pagerState.currentPage + 1)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
    TestTaskBookAppTheme {
        HomeScreen(
            HomeUiState(emptyList(), emptyMap(), null),
            {}
        )
    }
}
