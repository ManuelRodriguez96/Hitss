package com.hitss.data.remote.dto.app

import android.media.Rating
import com.google.gson.annotations.SerializedName

data class ShowTv(
    var id: Int?,
    var url: String?,
    var name: String?,
    var type: String?,
    var language: String?,
    var genres: List<String>?,
    var status: String?,
    var runtime: Int?,
    var averageRuntime: Int?,
    var premiered: String?,
    var ended: String?,
    var officialSite: String?,
    var schedule: ScheduleTv?,
    var rating: RatingTv?,
    var weight: Int?,
    var network: NetworkTv?,
    var image: ImageTv?,
    var summary: String?,
    var updated: Int?,
    var _links : Links
)