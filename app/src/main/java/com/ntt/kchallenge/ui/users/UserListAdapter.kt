package com.ntt.kchallenge.ui.users

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ntt.kchallenge.R
import com.ntt.kchallenge.data.model.UserResponse
import com.ntt.kchallenge.utils.LoadDataState
import kotlinx.android.synthetic.main.item_loading.view.*
import kotlinx.android.synthetic.main.user_list_content.view.*
import kotlin.random.Random

class UserListAdapter : PagedListAdapter<UserResponse, RecyclerView.ViewHolder>(diffCallback) {

    var onClickListener: View.OnClickListener? = null
    var onLoadFailedListener: OnLoadFailedListener? = null

    private var loadDataState: LoadDataState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_ITEM) {
            val view = inflater.inflate(R.layout.user_list_content, parent, false)
            UserViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_loading, parent, false)
            LoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is UserViewHolder) {
            val user = getItem(position)
            holder.bindView(user)
        } else if (holder is LoadingViewHolder) {
            holder.bindView(loadDataState)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoading() && position == itemCount - 1) TYPE_PROGRESS
        else TYPE_ITEM
    }

    private fun isLoading(): Boolean {
        return loadDataState != null && loadDataState == LoadDataState.LOADING
    }

    fun setLoadDataState(loadDataState: LoadDataState?) {
        if (loadDataState?.status == LoadDataState.Status.FAILED && itemCount == 0) {
            onLoadFailedListener?.onError(loadDataState.msg)
        } else {
            val preLoadingStatus = isLoading()
            this.loadDataState = loadDataState
            if (preLoadingStatus != isLoading()) {
                if (preLoadingStatus) {
                    notifyItemRemoved(itemCount)
                } else {
                    notifyItemInserted(itemCount)
                }
            }
        }
    }

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val txtIcon: TextView = view.tv_icon
        private val txtName: TextView = view.tv_name
        private val txtPhone: TextView = view.tv_phone

        fun bindView(user: UserResponse?) {
            user?.let {
                txtName.text = it.name
                txtPhone.text = it.phone
                txtIcon.text = it.name[0].toString().toUpperCase()
                val color = Color.argb(255, Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
                (txtIcon.background as GradientDrawable).setColor(color)
                with(itemView) {
                    tag = user
                    setOnClickListener(onClickListener)
                }
            }
        }

    }

    inner class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val progressBar: ProgressBar = view.progress_bar

        fun bindView(loadDataState: LoadDataState?) {
            if (loadDataState?.status == LoadDataState.Status.LOADING) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
            if (loadDataState?.status == LoadDataState.Status.FAILED) {
                onLoadFailedListener?.onError(loadDataState.msg)
            }
        }

    }

    companion object {

        private const val TYPE_ITEM = 0
        private const val TYPE_PROGRESS = 1

        val diffCallback = object : DiffUtil.ItemCallback<UserResponse>() {
            override fun areItemsTheSame(oldItem: UserResponse, newItem: UserResponse): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: UserResponse, newItem: UserResponse): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnLoadFailedListener {
        fun onError(message: String)
    }
}