package my.supercleanerapp.ui.activites

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import my.supercleanerapp.R
import my.supercleanerapp.databinding.ActivityCheckoutBinding
import my.supercleanerapp.firestore.FirestoreClass
import my.supercleanerapp.models.Address
import my.supercleanerapp.models.Cart
import my.supercleanerapp.models.Reservation
import my.supercleanerapp.models.Service
import my.supercleanerapp.ui.adapters.CartItemsListAdapter
import my.supercleanerapp.utils.Constants
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList
//import my.supercleanerapp.models.PaymentMethod



@Suppress("DEPRECATION")

class CheckoutActivity : BaseActivity() {

    private lateinit var binding: ActivityCheckoutBinding
    private var mAddressDetails: Address? = null
    private lateinit var mServicesList: ArrayList<Service>
    private lateinit var mCartItemsList: ArrayList<Cart>
    private var mSubTotal: Double = 0.0
    private var mTotalAmount: Double = 0.0
    private var mPaymentMethod: String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
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

        binding.btnPlaceReservation.setOnClickListener {
            placeAreservation()
        }

        // Set click listeners for "Pick Date" and "Pick Time" buttons
        binding.btnPickDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                null,
                year,
                month,
                dayOfMonth
            )

            datePickerDialog.datePicker.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = "${monthOfYear + 1}/$dayOfMonth/$year"
                val isSunday = Calendar.getInstance().apply {
                    set(year, monthOfYear, dayOfMonth)
                }.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY

                if (isSunday) {
                    datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).isEnabled = false
                } else {
                    datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).isEnabled = true
                }
            }

            datePickerDialog.setButton(
                DialogInterface.BUTTON_POSITIVE,
                "OK"
            ) { _, _ ->
                val day = datePickerDialog.datePicker.dayOfMonth
                val month = datePickerDialog.datePicker.month
                val year = datePickerDialog.datePicker.year
                val selectedDate = "${month + 1}/$day/$year"
                binding.tvSelectedDateTime.text = selectedDate
            }

            datePickerDialog.show()
            datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).isEnabled = false
        }

        binding.btnPickTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            val timePickerDialog = object : TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                    val currentText = binding.tvSelectedDateTime.text
                    binding.tvSelectedDateTime.text = "$currentText $selectedTime"
                },
                hour,
                minute,
                true // Use 24-hour format
            ) {
                override fun onTimeChanged(view: TimePicker, hourOfDay: Int, minute: Int) {
                    if (hourOfDay < 8 || hourOfDay >= 17) {
                        getButton(TimePickerDialog.BUTTON_POSITIVE).isEnabled = false
                    } else {
                        getButton(TimePickerDialog.BUTTON_POSITIVE).isEnabled = true
                    }
                }
            }

            timePickerDialog.show()
            timePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE).isEnabled = false
        }
        binding.paymentMethodGroup.setOnCheckedChangeListener { group, checkedId ->

            when (checkedId) {
                R.id.cash_radio_button -> {
                    mPaymentMethod = "Cash"
                }
                R.id.card_radio_button -> {
                    mPaymentMethod = "Card"
                }
            }
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


        for (item in mCartItemsList) {


            var price = item.price.toDouble()
            val quantity = item.cart_quantity.toInt()

            mSubTotal += price

        }

        binding.tvCheckoutSubTotal.text = "€$mSubTotal"
        binding.tvVat.text = "€13.5%"

        if (mSubTotal > 0) {
            binding.llCheckoutPlaceOrder.visibility = View.VISIBLE

            mTotalAmount = mSubTotal *1.135
            val decimalFormat = DecimalFormat("#.##") // specify the format pattern
            decimalFormat.roundingMode = RoundingMode.CEILING
            Log.d("CheckoutActivity", "mTotalAmount before formatting: $mTotalAmount")
            var formattedTotal = String.format("%.3f", mTotalAmount) // round to 3 digits after decimal
            Log.d("CheckoutActivity", "mTotalAmount after formatting: $formattedTotal")
            binding.tvCheckoutTotalAmount.text = "€$formattedTotal"
        } else {
            binding.llCheckoutPlaceOrder.visibility = View.GONE
        }


    }

    private fun placeAreservation() {
        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        val selectedDate = binding.tvSelectedDateTime.text.split(" ")[0]
        val selectedTime = binding.tvSelectedDateTime.text.split(" ")[1]


        val reservation = Reservation(
            FirestoreClass().getCurrentUserID(),
            mCartItemsList,
            mAddressDetails!!,
            "My reservation ${System.currentTimeMillis()}",
            mCartItemsList[0].image,
            mSubTotal.toString(),
            "13.5%",
            mTotalAmount.toString(),
            selectedDate, // Pass the selected date
            selectedTime, // Pass the selected time
            reservation_paymentMethod = mPaymentMethod // Set the payment method value
        )

        FirestoreClass().placeReservation(this@CheckoutActivity, reservation)
    }




    /**
     * A function to notify the success result of the order placed.
     */
    fun reservationPlacedSuccess() {

        FirestoreClass().updateAllDetails(this@CheckoutActivity, mCartItemsList)
    }

    /**
     * A function to notify the success result after updating all the required details.
     */
    fun allDetailsUpdatedSuccessfully() {


        // Hide the progress dialog.
        hideProgressDialog()

        Toast.makeText(this@CheckoutActivity, "Your reservation is placed successfully.", Toast.LENGTH_SHORT)
            .show()

        val intent = Intent(this@CheckoutActivity, DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()

    }
}