package my.supercleanerapp.ui.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import my.supercleanerapp.R
import my.supercleanerapp.databinding.ActivityReservedServicesDetailsBinding
import my.supercleanerapp.models.ReservedService
import my.supercleanerapp.utils.Constants
import my.supercleanerapp.utils.GlideLoader
import java.text.SimpleDateFormat
import java.util.*


@Suppress("DEPRECATION")
class ReservedServicesDetailsActivity : BaseActivity() {
    private lateinit var binding: ActivityReservedServicesDetailsBinding
    private lateinit var reservedServiceDetails: ReservedService
    private var reserved_service_status: Boolean = false// default value is false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservedServicesDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_RESERVED_SERVICE_DETAILS)) {
            reservedServiceDetails =
                intent.getParcelableExtra<ReservedService>(Constants.EXTRA_RESERVED_SERVICE_DETAILS)!!
        }

        setupActionBar()
        setupUI(reservedServiceDetails)

        binding.tvReservationStatusToggle.setOnClickListener {
            updateReservationStatus()
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarReservedServiceDetailsActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        binding.toolbarReservedServiceDetailsActivity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setupUI(serviceDetails: ReservedService) {
        reserved_service_status = serviceDetails.reserved_service_status

        binding.tvServiceDetailsId.text = serviceDetails.id
        binding.tvServiceDetailsDate.text = serviceDetails.reservation_date
        binding.tvServiceDetailsTime.text = serviceDetails.reservation_time

        GlideLoader(this@ReservedServicesDetailsActivity).loadServicePicture(
            serviceDetails.image,
            binding.ivServiceItemImage
        )
        binding.tvServiceItemName.text = serviceDetails.title
        binding.tvServiceItemPrice.text = "$${serviceDetails.price}"

        binding.tvReservedDetailsAddressType.text = serviceDetails.address.type
        binding.tvReservedDetailsFullName.text = serviceDetails.address.name
        binding.tvReservedDetailsAddress.text =
            "${serviceDetails.address.address}, ${serviceDetails.address.postCode}"
        binding.tvReservedDetailsAdditionalNote.text = serviceDetails.address.additionalNote

        if (serviceDetails.address.otherDetails.isNotEmpty()) {
            binding.tvReservedDetailsOtherDetails.visibility = View.VISIBLE
            binding.tvReservedDetailsOtherDetails.text = serviceDetails.address.otherDetails
        } else {
            binding.tvReservedDetailsOtherDetails.visibility = View.GONE
        }
        binding.tvReservedDetailsMobileNumber.text = serviceDetails.address.mobileNumber

        binding.tvReservedServiceSubTotal.text = serviceDetails.sub_total_amount
        binding.tvReservedServiceTotalAmount.text = serviceDetails.total_amount
        binding.tvVat.text = "13.5%"
    }

    private fun updateReservationStatus() {
        reserved_service_status = !reserved_service_status // Toggle the status

        val firestore = FirebaseFirestore.getInstance()
        val documentReference = firestore.collection(Constants.RESERVED_SERVICES)
            .document(reservedServiceDetails.id)

        documentReference.update("reserved_service_status", reserved_service_status)
            .addOnSuccessListener {
                // Update UI or show a success message
                Toast.makeText(
                    this@ReservedServicesDetailsActivity,
                    "Reservation status updated successfully.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                // Show an error message or handle the failure
                Log.e("UpdateStatusError", "Error updating reservation status", e)
            }
    }
}