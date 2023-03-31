package my.supercleanerapp.ui.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import my.supercleanerapp.R
import my.supercleanerapp.databinding.ActivityMyReservationDetailsBinding
import my.supercleanerapp.models.Reservation
import my.supercleanerapp.models.User
import my.supercleanerapp.ui.adapters.CartItemsListAdapter
import my.supercleanerapp.utils.Constants
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.log

@Suppress("DEPRECATION")
class MyReservationDetailsActivity : AppCompatActivity() {
    private lateinit var mUserDetails: User
    private var userAppAdmin: Boolean = false // default value is false
    private var reservation_status: Boolean = false// default value is false

    private lateinit var binding: ActivityMyReservationDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMyReservationDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        var myReservationDetails: Reservation= Reservation()

        if (intent.hasExtra(Constants.EXTRA_MY_RESERVATIONS_DETAILS)) {
            myReservationDetails =
                intent.getParcelableExtra<Reservation>(Constants.EXTRA_MY_RESERVATIONS_DETAILS)!!
            Log.d("myReservationDetails", "reservationDetails: $myReservationDetails")
        }


        setupUI(myReservationDetails)
    }


    /**
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarMyReservationDetailsActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        binding.toolbarMyReservationDetailsActivity.setNavigationOnClickListener { onBackPressed() }
    }


    /**
     * A function to setup UI.
     *
     * @param reservationDetails Reservation details received through intent.
     */
    private fun setupUI(reservationDetails: Reservation) {

        binding.tvReservationDetailsId.text = reservationDetails.title

        // Display the selected date
        binding.tvReservationDetailsDate.text = reservationDetails.reservation_date


        // Display the selected time
        binding.tvReservationDetailsTime.text = reservationDetails.reservation_time





        binding.rvMyReservationItemsList.layoutManager = LinearLayoutManager(this@MyReservationDetailsActivity)
        binding.rvMyReservationItemsList.setHasFixedSize(true)

        val cartListAdapter = CartItemsListAdapter(this@MyReservationDetailsActivity, reservationDetails.items, false)
        binding.rvMyReservationItemsList.adapter = cartListAdapter

        binding.tvMyReservationDetailsAddressType.text = reservationDetails.address.type
        binding.tvMyReservationDetailsFullName.text = reservationDetails.address.name
        binding.tvMyReservationDetailsAddress.text =
            "${reservationDetails.address.address}, ${reservationDetails.address.postCode}"
        binding.tvMyReservationDetailsAdditionalNote.text = reservationDetails.address.additionalNote

        if (reservationDetails.address.otherDetails.isNotEmpty()) {
            binding.tvMyReservationDetailsOtherDetails.visibility = View.VISIBLE
            binding.tvMyReservationDetailsOtherDetails.text = reservationDetails.address.otherDetails

        } else {
            binding.tvMyReservationDetailsOtherDetails.visibility = View.GONE
        }
        binding.tvMyReservationDetailsMobileNumber.text = reservationDetails.address.mobileNumber

        binding.tvReservationDetailsSubTotal.text = reservationDetails.sub_total_amount
        binding.tvReservationVatCharge.text = reservationDetails.vat_charge
        binding.tvReservationDetailsTotalAmount.text = reservationDetails.total_amount

    }
}