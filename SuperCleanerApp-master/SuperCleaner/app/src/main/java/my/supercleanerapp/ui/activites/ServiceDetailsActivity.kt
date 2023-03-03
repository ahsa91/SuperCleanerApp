package my.supercleanerapp.ui.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import my.supercleanerapp.R
import my.supercleanerapp.databinding.ActivityServiceDetailsBinding
import my.supercleanerapp.databinding.ActivitySettingsBinding
import my.supercleanerapp.firestore.FirestoreClass
import my.supercleanerapp.models.Cart
import my.supercleanerapp.models.Service
import my.supercleanerapp.utils.Constants
import my.supercleanerapp.utils.GlideLoader

class ServiceDetailsActivity : BaseActivity(), View.OnClickListener {


    private var mServiceId: String = ""
    private lateinit var binding: ActivityServiceDetailsBinding
    private lateinit var mServiceDetails: Service

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityServiceDetailsBinding.inflate(layoutInflater)
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
            binding.btnGoToCart.visibility=View.GONE
        } else {
            binding.btnAddToCart.visibility = View.VISIBLE
        }

        binding.btnAddToCart.setOnClickListener(this)
        binding.btnAddToCart.setOnClickListener(this)
        //function to get the service details when the activity is launched.
        getServiceDetails()
    }

    override fun onClick(v: View?) {

        if (v != null) {
            when (v.id) {

                R.id.btn_add_to_cart -> {
                    addToCart()
                }
                R.id.btn_go_to_cart->{
                    startActivity(Intent(this@ServiceDetailsActivity, CartListActivity::class.java))
                }
            }
        }

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

        mServiceDetails= service
        GlideLoader(this@ServiceDetailsActivity).loadServicePicture(
            service.image,
            binding.ivServiceDetailImage
        )

        binding.tvServiceDetailsTitle.text = service.title
        binding.tvServiceDetailsPrice.text = "€${service.price}"
        binding.tvServiceDetailsDescription.text = service.description

        // There is no need to check the cart list if the product owner himself is seeing the product details.
        if (FirestoreClass().getCurrentUserID() == service.user_id) {
            // Hide Progress dialog.
            hideProgressDialog()
        } else {
            FirestoreClass().checkIfItemExistInCart(this@ServiceDetailsActivity, mServiceId)
        }
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

    /**
     * A function to prepare the cart item to add it to the cart.
     */
    private fun addToCart() {

        val addToCart = Cart(
            FirestoreClass().getCurrentUserID(),
            mServiceId,
            mServiceDetails.title,
            mServiceDetails.price,
            mServiceDetails.image,
            Constants.DEFAULT_CART_QUANTITY
        )
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().addCartItems(this@ServiceDetailsActivity, addToCart)
    }
    fun addToCartSuccess() {
        // Hide the progress dialog.
        hideProgressDialog()

        Toast.makeText(
            this@ServiceDetailsActivity,
            resources.getString(R.string.success_message_item_added_to_cart),
            Toast.LENGTH_SHORT
        ).show()
    }
    /**
     * A function to notify the success result of item exists in the cart.
     */
    fun serviceExistsInCart() {

        // Hide the progress dialog.
        hideProgressDialog()

        // Hide the AddToCart button if the item is already in the cart.
        binding.btnAddToCart.visibility = View.GONE
        // Show the GoToCart button if the item is already in the cart. User can update the quantity from the cart list screen if he wants.
        binding.btnGoToCart.visibility = View.VISIBLE
    }

}