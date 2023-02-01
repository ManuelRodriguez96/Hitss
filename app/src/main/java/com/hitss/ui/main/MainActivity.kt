package com.hitss.ui.main

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.hitss.R
import com.hitss.data.Resource
import com.hitss.data.remote.dto.request.RequestScheduleTv
import com.hitss.data.remote.dto.response.ResponseScheduleTv
import com.hitss.data.remote.dto.response.ResponseSearchShows
import com.hitss.databinding.ActivityMainBinding
import com.hitss.ui.detail.DetailActivity
import com.hitss.ui.dialogs.ProgressDialog
import com.hitss.ui.main.adapter.AdapterSearchShows
import com.hitss.ui.main.adapter.AdapterShows
import com.hitss.utils.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels()

    private val adapterShows: AdapterShows by lazy {
        AdapterShows(mainViewModel.getListShow())
    }

    private val adapterSearchShow: AdapterSearchShows by lazy {
        AdapterSearchShows(mainViewModel.getListSearchShow())
    }

    private val progress : ProgressDialog by lazy {
        ProgressDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setOrientation()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getData()
        setDateView()
        bindAdapters()
        setObservers()
        setClickListeners()
        setSearchListener()
    }

    private fun getData() {
        mainViewModel.fetchScheduleTv(RequestScheduleTv("US", getToday()))
    }
    private fun setClickListeners() {
        binding.btnShowSearch.setOnClickListener {
            binding.btnShowSearch.setBounce {
                showSearchMode()
            }
        }

        binding.btnHideSearch.setOnClickListener {
            binding.btnHideSearch.setBounce {
                hideSearchMode()
            }
        }
    }

    /**
     * setSearchListener - sets the listener for the search action to trigger a search of shows
     */
    private fun setSearchListener() {
        binding.edtSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.root.hideKeyboard()
                searchByQuery()
                true
            } else {
                false
            }
        }
    }

    private fun searchByQuery() {
        mainViewModel.searchShowsByQuery(binding.edtSearch.text.toString())
    }

    private fun setObservers() {
        observe(mainViewModel.scheduleTv, ::handleSchedule)
        observe(mainViewModel.searchShowsResponse, ::handleSearchShows)
    }

    /**
     * handleSearchShows - handles the search shows resource and updates the search shows list or shows an error message
     *
     * @param resource - the resource containing the updated search show data
     */
    private fun handleSearchShows(resource: Resource<ResponseSearchShows>) {
        when (resource) {
            is Resource.Loading -> {
                progress.show()
            }
            is Resource.Success -> {
                updateSearchShows(resource)
                progress.dismiss()
            }
            is Resource.DataError -> {
                Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show()
                progress.dismiss()
            }
        }
    }
    /**
     * Once the new list of shows searched for by query is obtained, the adapter is updated.
     * If the list is empty, a message is displayed that there are no matches.
     * If it is the first search, the main list of shows is hidden and the search list is shown.
     * @param resource - the resource containing the updated search show data
     */
    private fun updateSearchShows(resource: Resource<ResponseSearchShows>) {
        mainViewModel.setListSearchShow(resource.data!!)
        adapterSearchShow.list = mainViewModel.getListSearchShow()
        adapterSearchShow.update()

        binding.containerEmptySearch.visibility = View.GONE
        binding.rvShows.visibility = View.GONE
        binding.rvSearch.visibility = View.VISIBLE
        binding.rvSearch.entryAnimation()

        if (mainViewModel.getListSearchShow().isEmpty()) {
            binding.rvSearch.visibility = View.GONE
            binding.containerEmptySearch.visibility = View.VISIBLE
            binding.containerEmptySearch.entryAnimation()
        }
    }

    /**
     * The adapterSearchShow is initialized with an initial empty list obtained
     * from the mainviewmodel, once the search api obtains a new list it will
     * be updated, also providing the method to open the detail of any show
     */
    private fun bindAdapterSearchShows() {
        adapterSearchShow.list = mainViewModel.getListSearchShow()
        binding.rvSearch.adapter = adapterSearchShow
        adapterSearchShow.actionOpenDetail = {
            openDetailShowBySearch(it)
        }
    }

    /**
     * handleSchedule - handles the schedule resource and updates the shows list or shows an error message
     *
     * @param resource - the resource containing the updated schedule data
     */
    private fun handleSchedule(resource: Resource<ResponseScheduleTv>) {
        when (resource) {
            is Resource.Loading -> {
                progress.show()
            }
            is Resource.Success -> {
                updateShows(resource)
                progress.dismiss()
            }
            is Resource.DataError -> {
                Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show()
                progress.dismiss()
            }
        }
    }

    /**
     * updateShows - updates the shows list with the given [resource]
     *
     * @param resource - the resource containing the updated show data
     */
    private fun updateShows(resource: Resource<ResponseScheduleTv>) {
        mainViewModel.setListHits(resource.data!!)
        adapterShows.list = mainViewModel.getListShow()
        adapterShows.update()
    }

    private fun bindAdapters() {
        bindAdapterShows()
        bindAdapterSearchShows()
    }
    /**
     * Sets the date view: text displays current date and imgDate opens a date picker.
     * When a date is chosen, schedule is fetched with "fetchScheduleTv" and new date.
     * Binds adapters for shows and search shows.
     */
    private fun setDateView() {
        binding.textToday.text = getTodayLiteral()
        binding.imgDate.setOnClickListener {
            binding.imgDate.setBounce {
                showDatePicker(supportFragmentManager) { date, dateLiteral ->
                    binding.textToday.text = dateLiteral
                    mainViewModel.fetchScheduleTv(RequestScheduleTv("US", date))
                }
            }
        }
    }

    /**
     * The adapterShows is initialized with an initial empty list obtained
     * from the mainviewmodel, once the search api obtains a new list it will
     * be updated, also providing the method to open the detail of any show
     */
    private fun bindAdapterShows() {
        adapterShows.list = mainViewModel.getListShow()
        binding.rvShows.adapter = adapterShows
        adapterShows.actionOpenDetail = {
            openDetailShow(it)
        }
    }

    /**
     * It places the view in search mode, that is, it hides the date text,
     * and shows the search box through an animation, also provides
     * the appearance of the keyboard for the search
     */
    private fun showSearchMode() {
        binding.containerDate.visibility = View.GONE
        binding.containerSearch.visibility = View.VISIBLE
        binding.containerSearch.entryAnimation()
        binding.edtSearch.requestFocus()
        binding.edtSearch.showKeyboard()
    }

    /**
     * Sets an onEditorActionListener for the `edtSearch` widget in the `binding` object.
     * The listener listens for the "IME_ACTION_DONE" action, which is triggered when the user finishes typing in the input field.
     * When the action is triggered, the keyboard is hidden and the `searchByQuery` function is called.
     * The listener returns `true` if the action is "IME_ACTION_DONE", and `false` otherwise.
     */
    private fun hideSearchMode() {
        binding.edtSearch.setText("")
        binding.containerEmptySearch.visibility = View.GONE
        binding.containerDate.visibility = View.VISIBLE
        binding.containerSearch.visibility = View.GONE
        binding.containerDate.entryAnimation()
        binding.rvSearch.visibility = View.GONE
        adapterSearchShow.list = mainViewModel.getListSearchShow()
        adapterSearchShow.update()
        binding.rvShows.visibility = View.VISIBLE
        binding.rvShows.entryAnimation()
        setDateView()
        bindAdapters()
        mainViewModel.fetchScheduleTv(RequestScheduleTv("US", getToday()))
    }

    /**
     * This function opens a detail activity for the selected item.
     *
     * It creates an Intent for the DetailActivity, adds animation options for the transition,
     * adds extra data to the Intent for the selected show's href and image,
     * and starts the DetailActivity with the provided options.
     *
     * @param it The position of the selected item in the list of shows.
     */
    private fun openDetailShow(it: Int) {
        var intent = Intent(this, DetailActivity::class.java)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            Pair(adapterShows.getViewByPosition(it).binding.imgShow, "img_detail")
        )
        intent.putExtra("hfr", mainViewModel.getListShow()[it].show._links.self.href)
        intent.putExtra("imgDetail", mainViewModel.getListShow()[it].show.image?.medium ?: "")
        startActivity(intent, options.toBundle())
    }


    /**
     * This function opens a detail activity for the selected item from search results.
     *
     * It creates an Intent for the DetailActivity, adds animation options for the transition,
     * adds extra data to the Intent for the selected show's href and image from search results,
     * and starts the DetailActivity with the provided options.
     *
     * @param it The position of the selected item in the list of search results shows.
     */
    private fun openDetailShowBySearch(it: Int) {
        var intent = Intent(this, DetailActivity::class.java)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            Pair(adapterSearchShow.getViewByPosition(it).binding.imgShow, "img_detail")
        )
        intent.putExtra("hfr", mainViewModel.getListSearchShow()[it].show._links.self.href)
        intent.putExtra("imgDetail", mainViewModel.getListSearchShow()[it].show.image?.medium ?: "")
        startActivity(intent, options.toBundle())
    }

    /**
     * This function sets the orientation of the screen in the app.
     * It checks the screen layout size and sets the orientation to sensor
     * if it is a large or extra-large screen. Otherwise, it sets the orientation to portrait.
     */
    private fun setOrientation() {
        requestedOrientation = when (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) {
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