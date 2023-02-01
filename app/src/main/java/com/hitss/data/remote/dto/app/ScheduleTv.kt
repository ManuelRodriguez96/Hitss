package com.hitss.data.remote.dto.app

import com.google.gson.annotations.SerializedName

data class ScheduleTv(
    var time: String,
    var days: List<String>
) {
    fun getSchedule(): String? {
        var separator = if(time.isNullOrEmpty())"" else " | "
        return time+separator+days.joinToString (", " )
    }
}