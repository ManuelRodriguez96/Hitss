package com.hitss.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hitss.data.DataRepository
import com.hitss.data.Resource
import com.hitss.data.remote.dto.app.Hits
import com.hitss.data.remote.dto.app.SearchTv
import com.hitss.data.remote.dto.app.ShowTv
import com.hitss.data.remote.dto.request.RequestScheduleTv
import com.hitss.data.remote.dto.request.RequestSearchQuery
import com.hitss.data.remote.dto.response.ResponseScheduleTv
import com.hitss.data.remote.dto.response.ResponseSearchShows
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val dataRepository: DataRepository) : ViewModel() {

    private val scheduleTvResponsePrivate = MutableLiveData<Resource<ResponseScheduleTv>>()
    val scheduleTv: LiveData<Resource<ResponseScheduleTv>> get() = scheduleTvResponsePrivate

    private val searchShowsResponsePrivate = MutableLiveData<Resource<ResponseSearchShows>>()
    val searchShowsResponse : LiveData<Resource<ResponseSearchShows>> get() = searchShowsResponsePrivate

    // List current shows
    private var listHits : MutableList<Hits> = mutableListOf()

    // List shows searched
    private var listSearchShow : MutableList<SearchTv> = mutableListOf()

    fun setListHits(data : List<Hits>) {
        listHits = data.toMutableList()
    }

    fun getListShow() = listHits

    fun setListSearchShow(data : List<SearchTv>){
        listSearchShow = data.toMutableList()
    }

    fun getListSearchShow() = listSearchShow

    /**
     * fetchScheduleTv - launches a coroutine to fetch schedule tv and
     * updates the schedule tv response value
     * @param request - the request to fetch schedule tv
     */
    fun fetchScheduleTv(request: RequestScheduleTv) {
        viewModelScope.launch {
            scheduleTvResponsePrivate.postValue(Resource.Loading())
            dataRepository.fetchScheduleTv(request).collect() {
                scheduleTvResponsePrivate.postValue(it)
            }
        }
    }

    /**
     * searchShowsByQuery - launches a coroutine to search shows by query and updates the search
     * shows response value
     * @param query - the search query
     */
    fun searchShowsByQuery(query : String) {
        viewModelScope.launch {
            searchShowsResponsePrivate.postValue(Resource.Loading())
            dataRepository.searchShowsByQuery(RequestSearchQuery(query)).collect() {
                searchShowsResponsePrivate.postValue(it)
            }
        }
    }
}