package com.hitss.data.remote.dto.response

import com.hitss.data.remote.dto.app.ImageTv
import com.hitss.data.remote.dto.app.NetworkTv
import com.hitss.data.remote.dto.app.RatingTv
import com.hitss.data.remote.dto.app.ScheduleTv

class ResponseDetailShow(
    val id: Long,
    val name: String,
    val genres: List<String>,
    val officialSite: String,
    val schedule: ScheduleTv,
    val rating: RatingTv,
    val network: NetworkTv,
    val image: ImageTv,
    val summary: String,
)