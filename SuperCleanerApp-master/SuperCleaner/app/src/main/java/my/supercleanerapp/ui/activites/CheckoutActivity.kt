package my.supercleanerapp.ui.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import my.supercleanerapp.R
import my.supercleanerapp.databinding.ActivityCheckoutBinding
import my.supercleanerapp.firestore.FirestoreClass
import my.supercleanerapp.models.Address
import my.supercleanerapp.models.Cart
import my.supercleanerapp.models.Service
import my.supercleanerapp.ui.adapters.CartItemsListAdapter
import my.supercleanerapp.utils.Constants


@Suppress("DEPRECATION")

class CheckoutActivity : BaseActivity() {

    private lateinit var binding:ActivityCheckoutBinding
    private var mAddressDetails: Address? = null
    private lateinit var mServicesList: ArrayList<Service>
    private lateinit var mCartItemsList: ArrayList<Cart>

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

        getServiceList()
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

    private fun getServiceList() {

        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getAllServicesList(this@CheckoutActivity)
    }

    /**
     * A function to get the success result of service list.
     *
     * @param servicesList
     */
    fun successServicesListFromFireStore(servicesList: ArrayList<Service>) {


        mServicesList = servicesList

        getCartItemsList()

    }

    /**
     * A function to get the list of cart items in the activity.
     */
    private fun getCartItemsList() {

        FirestoreClass().getCartList(this@CheckoutActivity)
    }

    /**
     * A function to notify the success result of the cart items list from cloud firestore.
     *
     * @param cartList
     */
    fun successCartItemsList(cartList: ArrayList<Cart>) {

        // Hide progress dialog.
        hideProgressDialog()

        mCartItemsList = cartList
        // START
        binding.rvCartListItems.layoutManager = LinearLayoutManager(this@CheckoutActivity)
        binding.rvCartListItems.setHasFixedSize(true)

        val cartListAdapter = CartItemsListAdapter(this@CheckoutActivity, mCartItemsList, false)
        binding.rvCartListItems.adapter = cartListAdapter

        var subTotal: Double = 0.0

        for (item in mCartItemsList) {


            val price = item.price.toDouble()
            val quantity = item.cart_quantity.toInt()

            subTotal += (price * quantity)

        }

        binding.tvCheckoutSubTotal.text = "€$subTotal"
        binding.tvVat.text = "€10.0"

        if (subTotal > 0) {
            binding.llCheckoutPlaceOrder.visibility = View.VISIBLE

            val total = subTotal *1.135
            val formattedTotal = String.format("%.3f", total) // round to 3 digits after decimal
            binding.tvCheckoutTotalAmount.text = "€$formattedTotal"
        } else {
            binding.llCheckoutPlaceOrder.visibility = View.GONE
        }
        // END
    }
}