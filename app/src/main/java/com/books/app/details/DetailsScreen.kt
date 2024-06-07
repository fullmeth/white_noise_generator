@file:OptIn(ExperimentalFoundationApi::class)

package com.books.app.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.books.app.R
import com.books.app.common.model.Book
import com.books.app.ui.theme.TestTaskBookAppTheme
import com.books.app.ui.theme.nunitoSansFontFamily
import kotlin.math.absoluteValue

@Composable
fun DetailsScreen(
    state: DetailsUiState,
    onBookClicked: (String) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_details),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        if (state.books.isNotEmpty()) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back_arrow),
                contentDescription = "Back",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Carousel(state.currentBookId, state.books, state.onNewBookSelected)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = state.books[state.currentBookId].name,
                color = MaterialTheme.colorScheme.onPrimary,
                fontFamily = nunitoSansFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Text(
                text = state.books[state.currentBookId].author,
                color = MaterialTheme.colorScheme.onPrimary.copy(.8f),
                fontFamily = nunitoSansFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(18.dp))
            Column(
                Modifier
                    .background(
                        shape = RoundedCornerShape(
                            topStart = 25.dp,
                            topEnd = 25.dp
                        ), color = Color.White
                    )
                    .fillMaxHeight()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    TwoStoreyText(
                        upper = state.books[state.currentBookId].views,
                        lower = "Readers"
                    )
                    TwoStoreyText(
                        upper = state.books[state.currentBookId].likes,
                        lower = "Likes"
                    )
                    TwoStoreyText(
                        upper = state.books[state.currentBookId].quotes,
                        lower = "Quotes"
                    )
                    TwoStoreyText(
                        upper = state.books[state.currentBookId].genre,
                        lower = "Genre"
                    )
                }
                Divider(
                    thickness = 0.5.dp,
                    color = Color(0xFFD9D5D6),
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = "Summary",
                    color = Color(0xFF0B080F),
                    fontFamily = nunitoSansFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
                Text(
                    text = state.books[state.currentBookId].summary,
                    color = Color(0xFF393637),
                    fontFamily = nunitoSansFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(16.dp)
                )
                Divider(
                    thickness = 0.5.dp,
                    color = Color(0xFFD9D5D6),
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = "You will also like",
                    color = Color(0xFF0B080F),
                    fontFamily = nunitoSansFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
                LazyRow(contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 8.dp)) {
                    items(state.recommendation.size) {
                        RecommendedBookItem(
                            coverUrl = state.books[state.currentBookId].coverUrl,
                            onBookClicked = onBookClicked,
                            bookId = state.books[state.currentBookId].id.toString(),
                            name = state.books[state.currentBookId].name,
                        )
                    }
                }
                Button(
                    onClick = {},
                    modifier = Modifier
                        .padding(
                            top = 24.dp,
                            start = 48.dp,
                            end = 48.dp,
                            bottom = 48.dp
                        )
                        .fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = "Read Now",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = nunitoSansFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun RecommendedBookItem(
    coverUrl: String,
    onBookClicked: (String) -> Unit,
    bookId: String,
    name: String
) {
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
            color = Color(0xFF393637),
            fontFamily = nunitoSansFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
    }
}

@Composable
fun TwoStoreyText(upper: String, lower: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = upper,
            color = Color(0xFF0B080F),
            fontFamily = nunitoSansFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = lower,
            color = Color(0xFFD9D5D6),
            fontFamily = nunitoSansFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Carousel(currentBookId: Int, books: List<Book>, onNewBookSelected: (Int) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
    ) {
        val pagerState =
            rememberPagerState(pageCount = { books.size }, initialPage = currentBookId)
        CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
            HorizontalPager(
                modifier = Modifier.fillMaxWidth(),
                beyondBoundsPageCount = 2,
                pageSpacing = 16.dp,
                contentPadding = PaddingValues(horizontal = 100.dp),
                state = pagerState,
                pageSize = PageSize.Fixed(200.dp)
            ) { index ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(books[index].coverUrl)
                        .memoryCacheKey(books[index].coverUrl)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.bg_placeholder),
                    error = painterResource(R.drawable.bg_placeholder),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(width = 200.dp, height = 250.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .carouselTransition(index, pagerState)
                )
            }
        }
        LaunchedEffect(key1 = pagerState.currentPage) {
            onNewBookSelected(pagerState.currentPage)
        }
    }
}

fun Modifier.carouselTransition(page: Int, pagerState: PagerState) =
    graphicsLayer {
        val pageOffset =
            ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue

        val transformation =
            lerp(
                start = 0.7f,
                stop = 1f,
                fraction = 1f - pageOffset.coerceIn(0f, 1f)
            )
        alpha = transformation
        scaleY = transformation
    }


@Preview(showBackground = true)
@Composable
fun DetailsPreview() {
    TestTaskBookAppTheme {
        DetailsScreen(DetailsUiState(-1, emptyList(), emptyList(), {}), {})
    }
}