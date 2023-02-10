package my.supercleanerapp.ui.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import my.supercleanerapp.R
import my.supercleanerapp.databinding.ActivityServiceDetailBinding
import my.supercleanerapp.databinding.ActivitySettingsBinding
import my.supercleanerapp.utils.Constants

class ServiceDetailsActivity : AppCompatActivity() {


    private var mServiceId: String = ""
    private lateinit var binding: ActivityServiceDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityServiceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_SERVICE_ID)) {
            mServiceId =
                intent.getStringExtra(Constants.EXTRA_SERVICE_ID)!!
            Log.i("Service Id", mServiceId)
        }

        //implement actionbar
        setupActionBar()
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
}