package com.hitss.data

import com.hitss.data.remote.RemoteData
import com.hitss.data.remote.dto.request.RequestCast
import com.hitss.data.remote.dto.request.RequestDetail
import com.hitss.data.remote.dto.request.RequestScheduleTv
import com.hitss.data.remote.dto.request.RequestSearchQuery
import com.hitss.data.remote.dto.response.ResponseCastTv
import com.hitss.data.remote.dto.response.ResponseDetailShow
import com.hitss.data.remote.dto.response.ResponseScheduleTv
import com.hitss.data.remote.dto.response.ResponseSearchShows
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DataRepository @Inject constructor(
    private val remoteRepository: RemoteData,
    private val ioDispatcher: CoroutineContext
) : DataRepositorySource {

    override suspend fun searchShowsByQuery(request: RequestSearchQuery): Flow<Resource<ResponseSearchShows>> {
        return flow { emit(remoteRepository.searchShowByQuery(request)) }.flowOn(ioDispatcher)
    }

    override suspend fun fetchCastTv(request: RequestCast): Flow<Resource<ResponseCastTv>> {
        return flow { emit(remoteRepository.fetchCastTv(request)) }.flowOn(ioDispatcher)
    }

    override suspend fun fetchDetailShow(request: RequestDetail): Flow<Resource<ResponseDetailShow>> {
        return flow { emit(remoteRepository.fetchDetailShow(request)) }.flowOn(ioDispatcher)
    }

    override suspend fun fetchScheduleTv(request: RequestScheduleTv): Flow<Resource<ResponseScheduleTv>> {
        return flow { emit(remoteRepository.fetchScheduleTv(request)) }.flowOn(ioDispatcher)
    }
}