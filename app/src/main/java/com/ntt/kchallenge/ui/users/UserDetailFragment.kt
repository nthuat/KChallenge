package com.ntt.kchallenge.ui.users

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ntt.kchallenge.R
import com.ntt.kchallenge.data.model.UserResponse
import kotlinx.android.synthetic.main.fragment_user_detail.view.*
import kotlinx.android.synthetic.main.user_detail.view.*

/**
 * A fragment representing a single User detail screen.
 * This fragment is either contained in a [UserListActivity]
 * in two-pane mode (on tablets) or a [UserDetailActivity]
 * on handsets.
 */
class UserDetailFragment : Fragment(), OnMapReadyCallback {

    private var googleMap: GoogleMap? = null
    private lateinit var mapView: MapView
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
        // Setup SupportActionBar
        (activity as AppCompatActivity).setSupportActionBar(rootView.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set up Map View
        mapView = rootView.findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        // Bind data
        userResponse?.let {
            rootView.collapsing_toolbar.title = it.name
            rootView.tv_name.text = it.name
            rootView.tv_username.text = it.username
            rootView.tv_phone.text = it.phone
            rootView.tv_email.text = it.email
            rootView.tv_website.text = it.website
            rootView.tv_address.text = it.address.fullAddress()
            rootView.tv_company.text = it.company.name
            rootView.tv_catchphrase.text = it.company.catchPhrase
            rootView.tv_bs.text = it.company.bs
        }
        return rootView
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onMapReady(map: GoogleMap?) {
        var userLatLng = LatLng(userResponse!!.address.geo.lat, userResponse!!.address.geo.lng)

        googleMap = map
        googleMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN
        googleMap?.uiSettings?.isZoomControlsEnabled = false
        googleMap?.addMarker(MarkerOptions().position(userLatLng))
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 2f))
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    companion object {
        const val ARG_USER = "user"
    }
}
