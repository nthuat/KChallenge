package com.ntt.kchallenge.ui.users

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.ntt.kchallenge.R
import com.ntt.kchallenge.api.UserResponse
import kotlinx.android.synthetic.main.fragment_user_detail.view.*
import kotlinx.android.synthetic.main.user_detail.view.*

/**
 * A fragment representing a single User detail screen.
 * This fragment is either contained in a [UserListActivity]
 * in two-pane mode (on tablets) or a [UserDetailActivity]
 * on handsets.
 */
class UserDetailFragment : Fragment() {

    private var userResponse: UserResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_USER)) {
                userResponse = it.getParcelable(ARG_USER)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_user_detail, container, false)
        (activity as AppCompatActivity).setSupportActionBar(rootView.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        userResponse?.let {
            rootView.toolbar.title = it.name
            rootView.tv_name.text = it.name
            rootView.tv_username.text = it.username
            rootView.tv_email.text = it.email
            rootView.tv_website.text = it.website
            rootView.tv_address.text = it.address.street
            rootView.tv_company.text = it.company.name
        }
        return rootView
    }

    companion object {
        const val ARG_USER = "user"
    }
}
