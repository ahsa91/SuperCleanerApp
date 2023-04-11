package my.supercleanerapp.ui.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityReservedServicesDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var reservedServiceDetails: ReservedService= ReservedService()

        if (intent.hasExtra(Constants.EXTRA_RESERVED_SERVICE_DETAILS)) {
            reservedServiceDetails =
                intent.getParcelableExtra<ReservedService>(Constants.EXTRA_RESERVED_SERVICE_DETAILS)!!
        }

        setupActionBar()
        setupUI(reservedServiceDetails)
    }

    /**
    * A function for actionBar Setup.
    */
    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarReservedServiceDetailsActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        binding.toolbarReservedServiceDetailsActivity.setNavigationOnClickListener { onBackPressed() }
    }

    /**
     * A function to setup UI.
     *
     * @param serviceDetails Order details received through intent.
     */
    private fun setupUI(serviceDetails: ReservedService) {

        binding.tvServiceDetailsId.text = serviceDetails.id

        // Display the selected date
        binding.tvServiceDetailsDate.text = serviceDetails.reservation_date


        // Display the selected time
        binding.tvServiceDetailsTime.text = serviceDetails.reservation_time

        GlideLoader(this@ReservedServicesDetailsActivity).loadServicePicture(
            serviceDetails.image,
            binding.ivServiceItemImage
        )
        binding.tvServiceItemName.text = serviceDetails.title
        binding.tvServiceItemPrice.text ="$${serviceDetails.price}"

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

}