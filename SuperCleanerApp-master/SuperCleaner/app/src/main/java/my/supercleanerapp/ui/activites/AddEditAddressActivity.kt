package my.supercleanerapp.ui.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import my.supercleanerapp.R
import my.supercleanerapp.databinding.ActivityAddEditAddressBinding
import my.supercleanerapp.firestore.FirestoreClass
import my.supercleanerapp.models.Address
import my.supercleanerapp.utils.Constants

@Suppress("DEPRECATION")

class AddEditAddressActivity : BaseActivity() {

    private var mAddressDetails: Address? = null

    private lateinit var binding:ActivityAddEditAddressBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddEditAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_ADDRESS_DETAILS)) {
            mAddressDetails =
                intent.getParcelableExtra(Constants.EXTRA_ADDRESS_DETAILS)!!
        }

        setupActionBar()

        //checked change listener on click of radio buttons for the address type.
        binding.rgType.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rb_other) {
                binding.tilOtherDetails.visibility = View.VISIBLE
            } else {
                binding.tilOtherDetails.visibility = View.GONE
            }
        }

        //click event of submit button and save the address.
        binding.btnSubmitAddress.setOnClickListener {
            saveAddressToFirestore()
        }

    }


    /**
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarAddEditAddressActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        binding.toolbarAddEditAddressActivity.setNavigationOnClickListener { onBackPressed() }
    }

    //validate the address input entries
    private fun validateData(): Boolean {
        return when {

            TextUtils.isEmpty(binding.etFullName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_please_enter_full_name),
                    true
                )
                false
            }

            TextUtils.isEmpty(binding.etPhoneNumber.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_please_enter_phone_number),
                    true
                )
                false
            }

            TextUtils.isEmpty(binding.etAddress.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_please_enter_address), true)
                false
            }

            TextUtils.isEmpty(binding.etPostCode.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_please_enter_post_code), true)
                false
            }

            binding.rbOther.isChecked && TextUtils.isEmpty(
                binding.etPostCode.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_please_enter_post_code), true)
                false
            }
            else -> {
                true
            }
        }
    }
    // END


    // save the address to the cloud firestore.

    /**
     * A function to save the address to the cloud firestore.
     */
    private fun saveAddressToFirestore() {

        // Here we get the text from editText and trim the space
        val fullName: String = binding.etFullName.text.toString().trim { it <= ' ' }
        val phoneNumber: String = binding.etPhoneNumber.text.toString().trim { it <= ' ' }
        val address: String = binding.etAddress.text.toString().trim { it <= ' ' }
        val postCode: String =binding.etPostCode.text.toString().trim { it <= ' ' }
        val additionalNote: String = binding.etAdditionalNote.text.toString().trim { it <= ' ' }
        val otherDetails: String = binding.etOtherDetails.text.toString().trim { it <= ' ' }

        if (validateData()) {

            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))

            val addressType: String = when {
                binding.rbHome.isChecked -> {
                    Constants.HOME
                }
                binding.rbOffice.isChecked -> {
                    Constants.OFFICE
                }
                else -> {
                    Constants.OTHER
                }
            }

            val addressModel = Address(
                FirestoreClass().getCurrentUserID(),
                fullName,
                phoneNumber,
                address,
                postCode,
                additionalNote,
                addressType,
                otherDetails
            )

            //Call the function to save the address.

            FirestoreClass().addAddress(this@AddEditAddressActivity, addressModel)
        }

    }

    /**
     * A function to notify the success result of address saved.
     */
    fun addUpdateAddressSuccess() {

        // Hide progress dialog
        hideProgressDialog()

        Toast.makeText(
            this@AddEditAddressActivity,
            resources.getString(R.string.err_your_address_added_successfully),
            Toast.LENGTH_SHORT
        ).show()

        finish()
    }
}
