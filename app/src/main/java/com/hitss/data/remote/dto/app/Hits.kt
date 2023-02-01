package com.hitss.data.remote.dto.app

import android.media.Image
import android.media.Rating

data class Hits(
    val id: Long,
    val url: String,
    val name: String,
    val season: Long,
    val number: Long? = null,
    val airdate: String,
    val airtime: String,
    val airstamp: String,
    val runtime: Long,
    val rating: RatingTv,
    val image: ImageTv? = null,
    val summary: String? = null,
    val show: ShowTv
)