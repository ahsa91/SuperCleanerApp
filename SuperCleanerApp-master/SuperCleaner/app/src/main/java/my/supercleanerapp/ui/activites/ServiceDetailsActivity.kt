package my.supercleanerapp.ui.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import my.supercleanerapp.R
import my.supercleanerapp.databinding.ActivityServiceDetailBinding
import my.supercleanerapp.databinding.ActivitySettingsBinding
import my.supercleanerapp.firestore.FirestoreClass
import my.supercleanerapp.models.Service
import my.supercleanerapp.utils.Constants
import my.supercleanerapp.utils.GlideLoader

class ServiceDetailsActivity : BaseActivity() {


    private var mServiceId: String = ""
    private lateinit var binding: ActivityServiceDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityServiceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_SERVICE_ID)) {
            mServiceId =
                intent.getStringExtra(Constants.EXTRA_SERVICE_ID)!!
        }

        var serviceOwnerId: String = ""

        if (intent.hasExtra(Constants.EXTRA_SERVICE_OWNER_ID)) {
            serviceOwnerId =
                intent.getStringExtra(Constants.EXTRA_SERVICE_OWNER_ID)!!
        }

        //implement actionbar
        setupActionBar()
        //Now we have the service owner id so if the product which is added by owner himself should not see the button Add To Cart.
        if (FirestoreClass().getCurrentUserID() == serviceOwnerId) {
            binding.btnAddToCart.visibility = View.GONE
        } else {
            binding.btnAddToCart.visibility = View.VISIBLE
        }
        //function to get the service details when the activity is launched.
        getServiceDetails()
    }

    /**
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarServiceDetailsActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        binding.toolbarServiceDetailsActivity.setNavigationOnClickListener { onBackPressed() }
    }

    /**
     * A function to notify the success result of the service details based on the service id.
     *
     * @param service A model class with product details.
     */
    fun serviceDetailsSuccess(service: Service) {

        // Hide Progress dialog.
        hideProgressDialog()

        // Populate the product details in the UI.
        GlideLoader(this@ServiceDetailsActivity).loadServicePicture(
            service.image,
            binding.ivServiceDetailImage
        )

        binding.tvServiceDetailsTitle.text = service.title
        binding.tvServiceDetailsPrice.text = "€${service.price}"
        binding.tvServiceDetailsDescription.text = service.description
    }

    /**
     * A function to call the firestore class function that will get the product details from cloud firestore based on the product id.
     */
    private fun getServiceDetails() {

        // Show the product dialog
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of FirestoreClass to get the product details.
        FirestoreClass().getServiceDetails(this@ServiceDetailsActivity, mServiceId)
    }
}