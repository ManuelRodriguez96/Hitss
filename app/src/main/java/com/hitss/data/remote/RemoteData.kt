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
import com.hitss.data.remote.service.ApiInterface
import com.hitss.data.remote.service.ServiceGenerator
import com.hitss.utils.errors.NETWORK_ERROR
import com.hitss.utils.errors.NO_INTERNET_CONNECTION
import com.hitss.utils.NetworkConnectivity
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class RemoteData @Inject constructor(
    private val serviceGenerator: ServiceGenerator,
    private val networkConnectivity: NetworkConnectivity,
) : RemoteDataSource {

    override suspend fun searchShowByQuery(request: RequestSearchQuery): Resource<ResponseSearchShows> {
        val service = serviceGenerator.createService(ApiInterface::class.java)
        return when (val response =
            processCall { (service::getShowsByQuery)(request.query) }) {
            is ResponseSearchShows -> {
                Resource.Success(response)
            }
            else -> {
                Resource.DataError(response as Int)
            }
        }
    }

    override suspend fun fetchCastTv(request: RequestCast): Resource<ResponseCastTv> {
        val service = serviceGenerator.createService(ApiInterface::class.java)
        return when (val response =
            processCall { (service::getCastShow)(request.url + "/cast") }) {
            is ResponseCastTv -> {
                Resource.Success(response)
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    override suspend fun fetchDetailShow(request: RequestDetail): Resource<ResponseDetailShow> {
        val service = serviceGenerator.createService(ApiInterface::class.java)
        return when (val response =
            processCall { (service::getDetailShow)(request.url) }) {
            is ResponseDetailShow -> {
                Resource.Success(response)
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    override suspend fun fetchScheduleTv(request: RequestScheduleTv): Resource<ResponseScheduleTv> {
        val service = serviceGenerator.createService(ApiInterface::class.java)
        return when (val response =
            processCall { (service::getScheduleTv)(request.country, request.date) }) {
            is ResponseScheduleTv -> {
                Resource.Success(response)
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    private suspend fun processCall(responseCall: suspend () -> Response<*>): Any? {
        if (!networkConnectivity.isConnected()) {
            return NO_INTERNET_CONNECTION
        }
        return try {
            val response = responseCall.invoke()
            val responseCode = response.code()
            if (response.isSuccessful) {
                response.body()
            } else {
                responseCode
            }
        } catch (e: IOException) {
            NETWORK_ERROR
        }
    }
}