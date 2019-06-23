package com.ntt.kchallenge.ui.users

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.ntt.kchallenge.R
import com.ntt.kchallenge.data.model.UserResponse
import com.ntt.kchallenge.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_user_list.*
import kotlinx.android.synthetic.main.user_list.*

/**
 * An activity representing a list of Users. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [UserDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class UserListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false
    private lateinit var userViewModel: UserViewModel
    private lateinit var adapter: UserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        if (user_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        swipe_layout.isRefreshing = true
        swipe_layout.setOnRefreshListener {
            userViewModel.refresh()
        }

        adapter = UserListAdapter()
        adapter.onClickListener = View.OnClickListener { v ->
            handleClickItemAction(v.tag as UserResponse)
        }
        adapter.onLoadFailedListener = object : UserListAdapter.OnLoadFailedListener {
            override fun onError(message: String) {
                handleError(message)
            }
        }
        setupRecyclerView()

        userViewModel = UserViewModel()
        userViewModel.userLiveData?.observe(this, Observer { pagedList ->
            adapter.submitList(pagedList)
            swipe_layout.isRefreshing = false
        })

        userViewModel.loadDataState?.observe(this, Observer { networkState ->
            adapter.setLoadDataState(networkState)
        })
    }

    private fun setupRecyclerView() {
        user_list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        user_list.adapter = adapter
    }

    private fun handleClickItemAction(user: UserResponse) {
        if (twoPane) {
            val fragment = UserDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(UserDetailFragment.ARG_USER, user)
                }
            }
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.user_detail_container, fragment)
                .commit()
        } else {
            val intent = Intent(this, UserDetailActivity::class.java).apply {
                putExtra(UserDetailFragment.ARG_USER, user)
            }
            startActivity(intent)
        }
    }

    private fun handleError(message: String) {
        Snackbar.make(user_list, message, Snackbar.LENGTH_LONG).show()
    }
}
