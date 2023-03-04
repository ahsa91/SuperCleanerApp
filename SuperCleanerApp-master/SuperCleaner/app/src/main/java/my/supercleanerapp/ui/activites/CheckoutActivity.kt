package my.supercleanerapp.ui.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import my.supercleanerapp.R
import my.supercleanerapp.databinding.ActivityCheckoutBinding
import my.supercleanerapp.models.Address
import my.supercleanerapp.utils.Constants


@Suppress("DEPRECATION")

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding:ActivityCheckoutBinding
    private var mAddressDetails: Address? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupActionBar()

        if (intent.hasExtra(Constants.EXTRA_SELECTED_ADDRESS)) {
            mAddressDetails =
                intent.getParcelableExtra<Address>(Constants.EXTRA_SELECTED_ADDRESS)!!
        }

        if (mAddressDetails != null) {
            binding.tvCheckoutAddressType.text = mAddressDetails?.type
            binding.tvCheckoutFullName.text = mAddressDetails?.name
            binding.tvCheckoutAddress.text = "${mAddressDetails!!.address}, ${mAddressDetails!!.postCode}"
            binding.tvCheckoutAdditionalNote.text = mAddressDetails?.additionalNote

            if (mAddressDetails?.otherDetails!!.isNotEmpty()) {
                binding.tvCheckoutOtherDetails.text = mAddressDetails?.otherDetails
            }
            binding.tvMobileNumber.text = mAddressDetails?.mobileNumber
        }
    }

    /**
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarCheckoutActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        binding.toolbarCheckoutActivity.setNavigationOnClickListener { onBackPressed() }
    }
}