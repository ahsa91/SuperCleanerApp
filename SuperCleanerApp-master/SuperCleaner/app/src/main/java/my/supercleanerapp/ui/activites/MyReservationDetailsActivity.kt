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
import my.supercleanerapp.ui.adapters.CartItemsListAdapter
import my.supercleanerapp.utils.Constants
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@Suppress("DEPRECATION")
class MyReservationDetailsActivity : AppCompatActivity() {


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


        // Date Format in which the date will be displayed in the UI.
        val dateFormat = "dd MMM yyyy HH:mm"
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = reservationDetails.reservation_datetime

        val reservationDateTime = formatter.format(calendar.time)
        binding.tvReservationDetailsDate.text = reservationDateTime
        // END

        //change this to payment later
        // Get the difference between the order date time and current date time in hours.
        // If the difference in hours is 1 or less then the order status will be PENDING.
        // If the difference in hours is 2 or greater then 1 then the order status will be PROCESSING.
        // And, if the difference in hours is 3 or greater then the order status will be DELIVERED.

        val diffInMilliSeconds: Long = System.currentTimeMillis() - reservationDetails.reservation_datetime
        val diffInHours: Long = TimeUnit.MILLISECONDS.toHours(diffInMilliSeconds)
        Log.e("Difference in Hours", "$diffInHours")

        when {
            diffInHours < 1 -> {
                binding.tvReservationStatus.text = resources.getString(R.string.reservation_status_pending)
                binding.tvReservationStatus.setTextColor(
                    ContextCompat.getColor(
                        this@MyReservationDetailsActivity,
                        R.color.colorAccent
                    )
                )
            }
            diffInHours < 2 -> {
                binding.tvReservationStatus.text = resources.getString(R.string.reservation_status_in_process)
                binding.tvReservationStatus.setTextColor(
                    ContextCompat.getColor(
                        this@MyReservationDetailsActivity,
                        R.color.colorOrderStatusInProcess
                    )
                )
            }
            else -> {
                binding.tvReservationStatus.text = resources.getString(R.string.reservation_status_complete)
                binding.tvReservationStatus.setTextColor(
                    ContextCompat.getColor(
                        this@MyReservationDetailsActivity,
                        R.color.colorOrderStatusDelivered
                    )
                )
            }
        }
        // END

        binding.rvMyReservationItemsList.layoutManager = LinearLayoutManager(this@MyReservationDetailsActivity)
        binding.rvMyReservationItemsList.setHasFixedSize(true)

        val cartListAdapter =
            CartItemsListAdapter(this@MyReservationDetailsActivity, reservationDetails.items, false)
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