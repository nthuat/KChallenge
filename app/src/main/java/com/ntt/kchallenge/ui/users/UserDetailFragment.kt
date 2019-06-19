package com.ntt.kchallenge.ui.users

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ntt.kchallenge.R

/**
 * A fragment representing a single User detail screen.
 * This fragment is either contained in a [UserListActivity]
 * in two-pane mode (on tablets) or a [UserDetailActivity]
 * on handsets.
 */
class UserDetailFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.user_detail, container, false)



        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
        const val ARG_USER = "user"
    }
}
