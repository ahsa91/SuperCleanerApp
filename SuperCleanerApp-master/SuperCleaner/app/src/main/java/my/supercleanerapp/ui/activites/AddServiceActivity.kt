package my.supercleanerapp.ui.activites

import android.Manifest
import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import my.supercleanerapp.R
import my.supercleanerapp.databinding.ActivityAddServiceBinding
import my.supercleanerapp.firestore.FirestoreClass
import my.supercleanerapp.utils.Constants
import my.supercleanerapp.utils.GlideLoader
import java.io.IOException

@Suppress("DEPRECATION")

class AddServiceActivity :  BaseActivity(), View.OnClickListener {

    // A global variable for URI of a selected image from phone storage.
    private var mSelectedImageFileUri: Uri? = null

    // A global variable for uploaded service image URL.
    private var mServiceImageURL: String = ""


    private lateinit var binding:ActivityAddServiceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Assign the click event to iv_add_update_service image.
        binding.ivAddUpdateService.setOnClickListener(this)

        // Assign the click event to submit button.
        binding.btnSubmit.setOnClickListener(this)
    }



    /**
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarAddServiceActivity)

        val actionBar= supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        binding.toolbarAddServiceActivity.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(v: View?) {

        if (v != null) {
            when (v.id) {

                // The permission code is similar to the user profile image selection.
                R.id.iv_add_update_service -> {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        Constants.showImageChooser(this@AddServiceActivity)
                    } else {
                        /*Requests permissions to be granted to this application. These permissions
                         must be requested in your manifest, they should not be granted to your app,
                         and they should have protection level*/
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }

                R.id.btn_submit -> {
                    if (validateServiceDetails()) {

                        uploadServiceImage()
                    }
                }
            }
        }
    }

    /**
     * This function will identify the result of runtime permission after the user allows or deny permission based on the unique code.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this@AddServiceActivity)
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(
                    this,
                    resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK
            && requestCode == Constants.PICK_IMAGE_REQUEST_CODE
            && data!!.data != null
        ) {

            // Replace the add icon with edit icon once the image is selected.
            binding.ivAddUpdateService.setImageDrawable(
                ContextCompat.getDrawable(
                    this@AddServiceActivity,
                    R.drawable.ic_vector_edit
                )
            )

            // The uri of selection image from phone storage.
            mSelectedImageFileUri = data.data!!

            try {
                // Load the service image in the ImageView.
                GlideLoader(this@AddServiceActivity).loadServicePicture(
                    mSelectedImageFileUri!!,
                    binding.ivServiceImage
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * A function to validate the product details.
     */
    private fun validateServiceDetails(): Boolean {
        return when {

            mSelectedImageFileUri == null -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_select_service_image), true)
                false
            }

            TextUtils.isEmpty(binding.etServiceTitle.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_service_title), true)
                false
            }

            TextUtils.isEmpty(binding.etServicePrice.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_price), true)
                false
            }

            TextUtils.isEmpty(binding.etServiceDescription.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_product_description),
                    true
                )
                false
            }

            else -> {
                true
            }
        }
    }

    /**
     * A function to upload the selected service image to firebase cloud storage.
     */
    private fun uploadServiceImage() {

        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().uploadImageToCloudStorage(
            this@AddServiceActivity,
            mSelectedImageFileUri,
            Constants.SERVICE_IMAGE
        )
    }

    /**
     * A function to get the successful result of product image upload.
     */
    fun imageUploadSuccess(imageURL: String) {

        // Initialize the global image url variable.
        mServiceImageURL = imageURL

        uploadServiceDetails()
    }

    private fun uploadServiceDetails() {

        // Get the logged in username from the SharedPreferences that we have stored at a time of login.
        val username =
            this.getSharedPreferences(Constants.MYSUPERCLEANERAPP_PREFERENCES, Context.MODE_PRIVATE)
                .getString(Constants.LOGGED_IN_USERNAME, "")!!

        // Here we get the text from editText and trim the space
        val service = my.supercleanerapp.models.Service(
            FirestoreClass().getCurrentUserID(),
            username,
            binding.etServiceTitle.text.toString().trim { it <= ' ' },
            binding.etServicePrice.text.toString().trim { it <= ' ' },
            binding.etServiceDescription.text.toString().trim { it <= ' ' },
            mServiceImageURL
        )

        FirestoreClass().uploadProductDetails(this@AddServiceActivity, service)
    }


    /**
     * A function to return the successful result of service upload.
     */
    fun serviceUploadSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(
            this@AddServiceActivity,
            resources.getString(R.string.service_uploaded_success_message),
            Toast.LENGTH_SHORT
        ).show()

        finish()
    }






}