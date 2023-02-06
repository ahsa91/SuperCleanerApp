package my.supercleanerapp.ui.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import my.supercleanerapp.R
import my.supercleanerapp.databinding.ActivityAddressListBinding

@Suppress("DEPRECATION")
class AddressListActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAddressListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddressListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        //click event for the Add Address and launch the AddEditAddressActivity.
        binding.tvAddAddress.setOnClickListener {
            val intent = Intent(this@AddressListActivity, AddEditAddressActivity::class.java)
            startActivity(intent)
        }

    }


    /**
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarAddressListActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        binding.toolbarAddressListActivity.setNavigationOnClickListener { onBackPressed() }
    }



}
