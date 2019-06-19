package com.ntt.kchallenge.ui.users

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ntt.kchallenge.R
import com.ntt.kchallenge.api.UserResponse
import kotlinx.android.synthetic.main.user_list_content.view.*

class UserListAdapter(
    private val parentActivity: UserListActivity,
    private val userList: List<UserResponse>,
    private val twoPane: Boolean
) : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {
    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            val user = v.tag as UserResponse
            if (twoPane) {
                val fragment = UserDetailFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(UserDetailFragment.ARG_USER, user)
                    }
                }
                parentActivity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.user_detail_container, fragment)
                    .commit()
            } else {
                val intent = Intent(v.context, UserDetailActivity::class.java).apply {
                    putExtra(UserDetailFragment.ARG_USER, user)
                }
                v.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_list_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
        holder.txtName.text = user.name
        holder.txtEmail.text = user.email

        with(holder.itemView) {
            tag = user
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount() = userList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtName: TextView = view.txtName
        val txtEmail: TextView = view.txtEmail
    }
}