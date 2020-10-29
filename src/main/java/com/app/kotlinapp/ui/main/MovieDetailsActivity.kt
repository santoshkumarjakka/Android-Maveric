package com.app.kotlinapp.ui.main

import android.os.*
import android.text.*
import android.view.*
import androidx.appcompat.app.*
import androidx.lifecycle.*
import com.app.kotlinapp.R
import com.app.kotlinapp.data.api.*
import com.app.kotlinapp.data.model.*
import com.app.kotlinapp.databinding.*
import com.app.kotlinapp.ui.base.*
import com.app.kotlinapp.ui.main.viewmodel.*
import com.app.kotlinapp.utils.*
import com.bumptech.glide.*
import com.bumptech.glide.request.*
import kotlinx.coroutines.*
import kotlin.coroutines.*

class MovieDetailsActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var binder: ActivityMovieDetailsBinding
    private lateinit var mainModel: MainViewModel
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private lateinit var job: Job
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binder.root)
        setSupportActionBar(binder.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        job = Job()
        setUpViewModel()
        val moveId = intent.getStringExtra("movieId")
        setupObserver(moveId)
        binder.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupObserver(moveId: String?) {
        moveId?.let { it ->
            mainModel.fetchMovieLDetails(it).observe(this, Observer {
                when (it.status) {
                    Status.ERROR -> {
                        binder.pbLoading.visibility = View.GONE
                    }
                    Status.SUCCESS -> {
                        binder.pbLoading.visibility = View.GONE
                        setMovieDetailsView(it.data)
                        binder.tvTittle.text = it.data?.title
                    }
                    Status.LOADING -> {
                        binder.pbLoading.visibility = View.VISIBLE
                    }
                }
            })
        }
    }

    private fun setMovieDetailsView(data: MovieDetailsModel?) {
        binder.tvTittle.text = data?.title
        binder.tvActor.text = Html.fromHtml("<b> Actors :</b> ".plus(data?.actors))
        binder.tvDirector.text = Html.fromHtml("<b> Director :</b> ".plus(data?.director))
        binder.tvWriter.text = Html.fromHtml("<b> Writer : </b>".plus(data?.writer))
        binder.tvCategory.text = data?.genre
        binder.tvDuration.text = data?.runtime
        binder.tvRate.text = Html.fromHtml("&#9733; ".plus(data?.imdbRating))
        binder.tvDec.text = data?.plot
        binder.tvYear.text = data?.year
        Glide.with(this).load(data?.poster)
            .apply(RequestOptions().fitCenter().error(R.drawable.ic_no_image_available))
            .into(binder.ivMoviePoster)
        binder.tvScore.text = data?.metascore?.let { setSpannableText("Score\n", it) }
        binder.tvReviews.text = data?.imdbRating?.let { setSpannableText("Reviews\n", it) }
        binder.tvVotes.text = data?.imdbVotes?.let { setSpannableText("Popularity\n", it) }
    }

    private fun setSpannableText(firstString: String, secondString: String): SpannableString {
        val spanableString = SpannableString(firstString.plus(secondString))

        spanableString.setSpan(Layout.Alignment.ALIGN_CENTER,
            0,
            firstString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanableString.setSpan(Layout.Alignment.ALIGN_CENTER,
            secondString.length,
            (firstString + secondString).length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spanableString
    }

    private fun setUpViewModel() {
        mainModel = ViewModelProvider(this@MovieDetailsActivity,
            ViewModelFactory(ApiHelper((ApiServiceImpl.provideApiService())))).get(MainViewModel::class.java)
    }
}