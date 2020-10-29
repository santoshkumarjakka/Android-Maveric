package com.app.kotlinapp.ui.main

import android.graphics.*
import android.graphics.drawable.*
import android.os.*
import android.text.*
import android.view.*
import androidx.appcompat.app.*
import androidx.appcompat.widget.*
import androidx.core.view.*
import androidx.lifecycle.*
import androidx.recyclerview.widget.*
import com.app.kotlinapp.R
import com.app.kotlinapp.data.api.*
import com.app.kotlinapp.ui.base.*
import com.app.kotlinapp.ui.main.adapter.*
import com.app.kotlinapp.ui.main.viewmodel.*
import com.app.kotlinapp.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.coroutines.*

class MainActivity : AppCompatActivity(), CoroutineScope {
    private var totalPages: String? = "0"
    private var currentPage: Int = 1
    private var isLastPage = false
    private var isLoading = false
    private lateinit var searchView: SearchView
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private lateinit var job: Job
    private lateinit var searchMoveAdapter: SearchMoveAdapter
    private lateinit var mainModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        job = Job()
        setUpUI()
        setupObserver()
    }

    private fun setUpUI() {
        setSupportActionBar(toolBar)
        setUpRecycleViewUi()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        val searchViewItem = menu!!.findItem(R.id.app_bar_search)
        searchView = MenuItemCompat.getActionView(searchViewItem) as SearchView
//        MenuItemCompat.setOnActionExpandListener(searchViewItem,
//            object : MenuItemCompat.OnActionExpandListener {
//                override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
//                    // Set styles for expanded state here
//                    if (supportActionBar != null) {
//                        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
//                    }
//                    return true
//                }
//
//                override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
//                    // Set styles for collapsed state here
//                    if (supportActionBar != null) {
//                        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#6200EE")))
//                    }
//                    return true
//                }
//            })
        setUpSearchStateFlow()
        return super.onCreateOptionsMenu(menu);
    }

    private fun setUpSearchStateFlow() {
        launch {
            searchView.getQueryTextChangeStateFlow()
                .debounce(300)
                .filter { query ->
                    return@filter query.isNotEmpty()
                }
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    dataFromNetwork(query)
                        .catch {
                            emitAll(flowOf(""))
                        }
                }
                .flowOn(Dispatchers.Default)
                .collect {
                    searchMovieName(it, currentPage.toString(), false)
                }
        }
    }

    private fun dataFromNetwork(query: String): Flow<String> {
        return flow {
            delay(2000)
            emit(query)
        }
    }

    private fun searchMovieName(query: String?, currentPage: String, loadMoreDataEnable: Boolean) {
        query?.let { it ->
            mainModel.searchMovieList(it, currentPage).observe(this, Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        pbLoading.visibility = View.GONE
                        searchView.clearFocus()
                        totalPages = it.data?.totalResults
                        if (loadMoreDataEnable) {
                            isLoading = false
                            searchMoveAdapter.removeLoading()
                        }
                        searchMoveAdapter.setMovieData(it.data?.search, loadMoreDataEnable)
                    }
                    Status.ERROR -> {
                        if (loadMoreDataEnable) {
                            isLoading = false
                            searchMoveAdapter.removeLoading()
                        }
                        pbLoading.visibility = View.GONE
                        it.message?.let { it1 -> showAlertErrorDialog(it1) }
                    }
                    Status.LOADING -> {
                        if (!loadMoreDataEnable) {
                            pbLoading.visibility = View.VISIBLE
                        }
                    }
                }
            })
        }
    }

    private fun setUpRecycleViewUi() {
        rvUserList.layoutManager = GridLayoutManager(this, 2)
        rvUserList.setHasFixedSize(true)
        searchMoveAdapter = SearchMoveAdapter(this@MainActivity, arrayListOf())
        rvUserList.addItemDecoration(RecycleViewSpacingItemDecoration(2, 30, false))
        rvUserList.adapter = searchMoveAdapter
        rvUserList.addOnScrollListener(object :
            PaginationListener(rvUserList.layoutManager as GridLayoutManager, currentPage) {
            override fun loadMoreItems() {
                if (totalPages != null && currentPage < totalPages?.toInt()!!) {
                    isLoading = true;
                    currentPage += 1
                    searchMoveAdapter.addLoading()
                    var searchText=searchView.query.toString()
                    if(TextUtils.isEmpty(searchText)){
                        searchText="film"
                    }
                    searchMovieName(searchText, currentPage.toString(), true)
                }
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }
        })
    }

    private fun setupObserver() {
        mainModel = ViewModelProvider(this@MainActivity,
            ViewModelFactory(ApiHelper((ApiServiceImpl.provideApiService())))).get(MainViewModel::class.java)
        searchMovieName("film", "1", false)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun showAlertErrorDialog(error: String) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setMessage(error)
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("ok"
        ) { p0, p1 -> p0?.dismiss() }
        alertDialog.show()
    }
}

fun SearchView.getQueryTextChangeStateFlow(): StateFlow<String> {
    val query = MutableStateFlow("")

    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String): Boolean {
            query.value = newText
            return true
        }
    })

    return query
}
