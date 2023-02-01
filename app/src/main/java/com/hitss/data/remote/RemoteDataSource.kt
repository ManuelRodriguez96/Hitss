package com.hitss.data.remote

import com.hitss.data.Resource
import com.hitss.data.remote.dto.request.RequestCast
import com.hitss.data.remote.dto.request.RequestDetail
import com.hitss.data.remote.dto.request.RequestScheduleTv
import com.hitss.data.remote.dto.request.RequestSearchQuery
import com.hitss.data.remote.dto.response.ResponseCastTv
import com.hitss.data.remote.dto.response.ResponseDetailShow
import com.hitss.data.remote.dto.response.ResponseScheduleTv
import com.hitss.data.remote.dto.response.ResponseSearchShows

internal interface RemoteDataSource {
    suspend fun fetchScheduleTv(request : RequestScheduleTv) : Resource<ResponseScheduleTv>
    suspend fun fetchDetailShow(request : RequestDetail) : Resource<ResponseDetailShow>
    suspend fun fetchCastTv(request : RequestCast) : Resource<ResponseCastTv>
    suspend fun searchShowByQuery(request: RequestSearchQuery) : Resource<ResponseSearchShows>
}