package com.app.kotlinapp.ui.main.adapter

import android.content.*
import android.util.*
import android.view.*
import android.widget.*
import androidx.appcompat.widget.*
import androidx.recyclerview.widget.*
import com.app.kotlinapp.R
import com.app.kotlinapp.data.model.*
import com.app.kotlinapp.ui.main.*
import com.bumptech.glide.*
import com.bumptech.glide.request.*

/**
 * Created by santosh on 28/10/20.
 */
class SearchMoveAdapter(private val context: Context,
    private var movieList: MutableList<SearchItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var VIEW_TYPE_LOADING = 0
    private var VIEW_TYPE_NORMAL = 1
    private var isLoaderVisible = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_LOADING) {
            ViewHolderLoading(LayoutInflater.from(parent.context)
                .inflate(R.layout.progress_bar_layout, parent, false))
        } else {
            ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.search_movie_item_layout, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.tvTittle.text = movieList[position].title
            Glide.with(context).load(movieList[position].poster)
                .apply(RequestOptions().error(R.drawable.ic_no_image_available))
                .into(holder.ivMoviePic)
            holder.itemView.setOnClickListener {
                context.startActivity(Intent(context,
                    MovieDetailsActivity::class.java).putExtra("movieId",
                    movieList[position].imdbID))
            }
        } else if (holder is ViewHolderLoading) {

        }
    }

    override fun getItemViewType(position: Int): Int {
        if (isLoaderVisible) {
            return if (position == (movieList.size - 1)) VIEW_TYPE_LOADING else VIEW_TYPE_NORMAL
        }
        return VIEW_TYPE_NORMAL;
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    fun setMovieData(data: MutableList<SearchItem>?, loadMoreDataEnable: Boolean) {
        movieList.apply {
            if (!loadMoreDataEnable) {
                clear()
            }
            data?.let { movieList.addAll(it) }
            notifyDataSetChanged()
        }
    }

    fun addLoading() {
        isLoaderVisible = true
        movieList.add(SearchItem())
        notifyItemInserted(movieList.size - 1)
    }

    fun removeLoading() {
        isLoaderVisible = false
        val position: Int = movieList.size - 1
        movieList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun clear() {
        movieList.clear()
        notifyDataSetChanged()
    }

    internal inner class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var tvTittle: AppCompatTextView = convertView.findViewById(R.id.tvTittle)
        var ivMoviePic: AppCompatImageView = convertView.findViewById(R.id.ivMoviePic)
    }

    internal inner class ViewHolderLoading(convertView: View) :
        RecyclerView.ViewHolder(convertView) {
        var pbLoading: ProgressBar = convertView.findViewById(R.id.pbLoading)
    }
}
