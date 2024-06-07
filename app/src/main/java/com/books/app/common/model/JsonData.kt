package com.books.app.common.model

import com.google.gson.annotations.SerializedName

data class JsonData(
    val books: List<Book>,
    @SerializedName("top_banner_slides")
    val topBannerSlides: List<BannerSlide>,
    @SerializedName("you_will_like_section")
    val youWillLikeSection: List<Int>
)
