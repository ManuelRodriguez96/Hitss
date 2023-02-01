package com.hitss.ui.detail

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.hitss.R
import com.hitss.data.Resource
import com.hitss.data.remote.dto.response.ResponseCastTv
import com.hitss.data.remote.dto.response.ResponseDetailShow
import com.hitss.databinding.ActivityDetailBinding
import com.hitss.ui.detail.adapter.AdapterGenres
import com.hitss.ui.detail.adapter.AdapterPersons
import com.hitss.ui.dialogs.ProgressDialog
import com.hitss.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    private val detailViewModel: DetailViewModel by viewModels()

    private val progress: ProgressDialog by lazy {
        ProgressDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setOrientation()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getData()
        setObservers()
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun setObservers() {
        observe(detailViewModel.responseDetailShow, ::handleDetailShow)
        observe(detailViewModel.responseCastShow, ::handleCastShow)
    }

    /**
     * handleSearchShows - handles the detail shows resource and updates the view shows or shows an error message
     * @param resource - the resource containing the updated detail show data
     */
    private fun handleDetailShow(resource: Resource<ResponseDetailShow>) {
        when (resource) {
            is Resource.Loading -> {
                progress.show()
            }
            is Resource.Success -> {
                setDetailData(resource.data)
                progress.dismiss()
            }

            is Resource.DataError -> {
                Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show()
                progress.dismiss()
            }
        }
    }

    /**
     * handleSearchShows - handles the cast resource and updates the cast list or shows an error message
     *
     * @param resource - the resource containing the updated cast data
     */
    private fun handleCastShow(resource: Resource<ResponseCastTv>) {
        when (resource) {
            is Resource.Loading -> {
                progress.show()
            }
            is Resource.Success -> {
                bindAdapterCast(resource.data)
                progress.dismiss()
            }
            is Resource.DataError -> {
                Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show()
                progress.dismiss()
            }
        }
    }

    /**
     * once the cast list is obtained, the adapter list
     * is filled in and displayed on the screen
     * @param castTv - cast information of the show
     */
    private fun bindAdapterCast(castTv: ResponseCastTv?) {
        binding.rvPersons.adapter = AdapterPersons(castTv?.toMutableList() ?: mutableListOf())
    }


    /**
     * proceed to fill all the information of the show with
     * the data obtained from the api
     * @param data - the resource containing the show data
     */
    private fun setDetailData(data: ResponseDetailShow?) {
        binding.apply {
            binding.textSummary.entryAnimation()
            textSummary.text = getHtmlText(data?.summary ?: "")
            binding.textName.entryAnimation()
            textName.text = data?.name ?: ""
            textNetworkName.entryAnimation()
            textNetworkName.text = data?.network?.name ?: ""
            textSchedule.entryAnimation()
            textSchedule.text = data?.schedule?.getSchedule()
            textRating.entryAnimation()
            textRating.text = data?.rating?.average
            bindAdapterGenres(data?.genres)
            setActionUrl(data?.officialSite ?: "")
        }
    }


    /**
     * function that opens the browser
     * @param url - url website show
     */
    private fun setActionUrl(url: String) {
        binding.btnUrl.setOnClickListener {
            binding.btnUrl.setBounce {
                openUrlBrowser(url)
            }
        }
    }

    /**
     * proceed to fill in the list of genres to which the show belongs
     * @param genres list of genres
     */
    private fun bindAdapterGenres(genres: List<String>?) {
        val manager = FlexboxLayoutManager(this)
        manager.flexDirection = FlexDirection.ROW
        manager.justifyContent = JustifyContent.FLEX_START
        binding.rvGenres.layoutManager = manager
        binding.rvGenres.adapter = AdapterGenres(genres?.toMutableList() ?: mutableListOf())
    }

    /**
     * Get the data sent from the mainactivity
     */
    private fun getData() {
        detailViewModel.fetchDetailShow(intent.getStringExtra("hfr") as String)
        detailViewModel.fetchCastShow(intent.getStringExtra("hfr") as String)
        setImageDetail(intent.getStringExtra("imgDetail") as String)
    }

    /**
     * the image and its transition are shown in the corresponding view
     *  @param img image of show
     */
    private fun setImageDetail(img: String) {
        binding.imgShow.transitionName = "img_detail"
        binding.imgShow.loadImageFromPicasso(img)
    }

    /**
     * This function sets the orientation of the screen in the app.
     * It checks the screen layout size and sets the orientation to sensor
     * if it is a large or extra-large screen. Otherwise, it sets the orientation to portrait.
     */
    private fun setOrientation() {
        requestedOrientation =
            when (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) {
                Configuration.SCREENLAYOUT_SIZE_LARGE,
                Configuration.SCREENLAYOUT_SIZE_XLARGE -> {
                    ActivityInfo.SCREEN_ORIENTATION_SENSOR
                }
                else -> {
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }
            }
    }
}