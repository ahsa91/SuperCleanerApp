package my.supercleanerapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import my.supercleanerapp.R
import my.supercleanerapp.databinding.FragmentReservationsBinding
import my.supercleanerapp.databinding.FragmentReservedServiceBinding
import my.supercleanerapp.firestore.FirestoreClass
import my.supercleanerapp.models.ReservedService
import my.supercleanerapp.ui.adapters.ReservedServicesListAdapter


/**
 * A simple [Fragment] subclass.
 * Use the [ReservedServiceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReservedServiceFragment : BaseFragment() {

    private var _fragBinding: FragmentReservedServiceBinding?=null
    private val fragBinding get() = _fragBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = FragmentReservedServiceBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        return root

    }

    override fun onResume() {
        super.onResume()

        getReservedServicesList()
    }

    private fun getReservedServicesList() {
        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of Firestore class.
        FirestoreClass().getReservedServicesList(this@ReservedServiceFragment)
    }

    /**
     * A function to get the list of reserved services
     */
    fun successReservedServicesList(reservedServicesList: ArrayList<ReservedService>) {
        // Hide Progress dialog.
        hideProgressDialog()

        _fragBinding?.let { fragBinding ->
            if (reservedServicesList.size > 0) {
                fragBinding.rvReservedServiceItems.visibility = View.VISIBLE
                fragBinding.tvNoReservedServiceFound.visibility = View.GONE

                fragBinding.rvReservedServiceItems.layoutManager = LinearLayoutManager(activity)
                fragBinding.rvReservedServiceItems.setHasFixedSize(true)

                val soldProductsListAdapter =
                    ReservedServicesListAdapter(requireActivity(), reservedServicesList)
                fragBinding.rvReservedServiceItems.adapter = soldProductsListAdapter
            } else {
                fragBinding.rvReservedServiceItems.visibility = View.GONE
                fragBinding.tvNoReservedServiceFound.visibility = View.VISIBLE
            }
        }
    }


}