package my.supercleanerapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import my.supercleanerapp.R
import my.supercleanerapp.databinding.FragmentReservationsBinding
import my.supercleanerapp.firestore.FirestoreClass
import my.supercleanerapp.models.Reservation
import my.supercleanerapp.ui.adapters.MyReservationsListAdapter

@Suppress("DEPRECATION")
class ReservationsFragment : BaseFragment() {

    private var _fragBinding: FragmentReservationsBinding?=null
    private val fragBinding get() = _fragBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // If we want to use the option menu in fragment we need to add it.
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = FragmentReservationsBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        return root
    }


    override fun onResume() {
        super.onResume()

        getMyReservationsList()
    }

    /**
    * A function to get the list of my orders.
    */
    private fun getMyReservationsList() {
        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getMyReservationsList(this@ReservationsFragment)
    }

    /**
     * A function to get the success result of the my order list from cloud firestore.
     *
     * @param reservationsList List of my orders.
     */
    fun populateReservationsListInUI(reservationsList: ArrayList<Reservation>) {

        // Hide the progress dialog.
        hideProgressDialog()


        if (reservationsList.size > 0) {

            fragBinding.rvMyReservationItems.visibility = View.VISIBLE
            fragBinding.tvNoReservationsFound.visibility = View.GONE

            fragBinding.rvMyReservationItems.layoutManager = LinearLayoutManager(activity)
            fragBinding.rvMyReservationItems.setHasFixedSize(true)

            val myReservationsAdapter = MyReservationsListAdapter(requireActivity(), reservationsList)
            fragBinding.rvMyReservationItems.adapter = myReservationsAdapter
        } else {
            fragBinding.rvMyReservationItems.visibility = View.GONE
            fragBinding.tvNoReservationsFound.visibility = View.VISIBLE
        }

    }


}