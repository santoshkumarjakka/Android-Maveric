package com.app.kotlinapp.utils

import androidx.recyclerview.widget.*

/**
 * Created by santosh on 29/10/20.
 */
abstract class PaginationListener(var layoutManager: GridLayoutManager, var PAGE_SIZE: Int) :
    RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = layoutManager.getChildCount();
        val totalItemCount = layoutManager.getItemCount();
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >=
                totalItemCount && firstVisibleItemPosition >= 0) {
                loadMoreItems();
            }
        }
    }

    protected abstract fun loadMoreItems();
    protected abstract fun isLastPage():Boolean
    protected abstract fun isLoading():Boolean
}