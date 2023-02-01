package com.hitss.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hitss.data.DataRepository
import com.hitss.data.Resource
import com.hitss.data.remote.dto.request.RequestCast
import com.hitss.data.remote.dto.request.RequestDetail
import com.hitss.data.remote.dto.response.ResponseCastTv
import com.hitss.data.remote.dto.response.ResponseDetailShow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val dataRepository: DataRepository) :
    ViewModel() {

    private val responseDetailShowPrivate = MutableLiveData<Resource<ResponseDetailShow>>()
    val responseDetailShow: LiveData<Resource<ResponseDetailShow>> get() = responseDetailShowPrivate

    private val responseCastShowPrivate = MutableLiveData<Resource<ResponseCastTv>>()
    val responseCastShow: LiveData<Resource<ResponseCastTv>> get() = responseCastShowPrivate


    /**
     * Fetch detail show information from the API
     * @param hfr ID of the show to fetch details
     * @return details of the show. The response is stored in responseDetailShowPrivate and can be observed for updates.
     */
    fun fetchDetailShow(hfr: String) {
        viewModelScope.launch {
            responseDetailShowPrivate.postValue(Resource.Loading())
            dataRepository.fetchDetailShow(RequestDetail(hfr)).collect() {
                responseDetailShowPrivate.postValue(it)
            }
        }
    }


    /**
     * Fetches cast details for a TV show from the data repository.
     * Sets responseCastShowPrivate to Resource.Loading and updates
     * it with the value collected from the data repository.
     *@param hfr - identifier for the TV show
     */
    fun fetchCastShow(hfr: String) {
        viewModelScope.launch {
            responseCastShowPrivate.postValue(Resource.Loading())
            dataRepository.fetchCastTv(RequestCast(hfr)).collect() {
                responseCastShowPrivate.postValue(it)
            }
        }
    }
}