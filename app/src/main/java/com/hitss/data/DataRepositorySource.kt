package com.hitss.data

import com.hitss.data.remote.dto.request.RequestCast
import com.hitss.data.remote.dto.request.RequestDetail
import com.hitss.data.remote.dto.request.RequestScheduleTv
import com.hitss.data.remote.dto.request.RequestSearchQuery
import com.hitss.data.remote.dto.response.ResponseCastTv
import com.hitss.data.remote.dto.response.ResponseDetailShow
import com.hitss.data.remote.dto.response.ResponseScheduleTv
import com.hitss.data.remote.dto.response.ResponseSearchShows
import kotlinx.coroutines.flow.Flow

interface DataRepositorySource {
    suspend fun fetchScheduleTv(request : RequestScheduleTv) : Flow<Resource<ResponseScheduleTv>>
    suspend fun fetchDetailShow(request : RequestDetail) : Flow<Resource<ResponseDetailShow>>
    suspend fun fetchCastTv(request : RequestCast) : Flow<Resource<ResponseCastTv>>
    suspend fun searchShowsByQuery(request : RequestSearchQuery) : Flow<Resource<ResponseSearchShows>>
}