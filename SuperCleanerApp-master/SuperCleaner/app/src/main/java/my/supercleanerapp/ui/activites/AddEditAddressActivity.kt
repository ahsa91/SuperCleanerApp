package my.supercleanerapp.ui.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import my.supercleanerapp.R
import my.supercleanerapp.databinding.ActivityAddEditAddressBinding
import my.supercleanerapp.firestore.FirestoreClass
import my.supercleanerapp.models.Address
import my.supercleanerapp.utils.Constants

class AddEditAddressActivity : BaseActivity() {

    private lateinit var binding:ActivityAddEditAddressBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddEditAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupActionBar()

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
        }
    }
// END
}
