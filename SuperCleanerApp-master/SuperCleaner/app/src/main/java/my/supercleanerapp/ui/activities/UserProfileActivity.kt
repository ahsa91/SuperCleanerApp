package my.supercleanerapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import my.supercleanerapp.R
import my.supercleanerapp.databinding.ActivityUserProfileBinding
import my.supercleanerapp.models.User
import my.supercleanerapp.utils.Constants

@Suppress("DEPRECATION")


class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding:ActivityUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Create a instance of the User model class.
        var userDetails: User = User()
        if(intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            // Get the user details from intent as a ParcelableExtra.
            userDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        // Here, the some of the edittext components are disabled because it is added at a time of Registration.
        binding.etFirstName.isEnabled = false
        binding.etFirstName.setText(userDetails.firstName)

        binding.etLastName.isEnabled = false
        binding.etLastName.setText(userDetails.lastName)

        binding.etEmail.isEnabled = false
        binding.etEmail.setText(userDetails.email)

    }
}