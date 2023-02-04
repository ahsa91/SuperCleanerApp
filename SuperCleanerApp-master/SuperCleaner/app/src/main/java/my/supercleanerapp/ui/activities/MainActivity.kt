package my.supercleanerapp.ui.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import my.supercleanerapp.R
import my.supercleanerapp.databinding.ActivityMainBinding
import my.supercleanerapp.utils.Constants

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences =
            getSharedPreferences(Constants.MYSUPERCLEANERAPP_PREFERENCES, Context.MODE_PRIVATE)

        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, "")!!

        binding.tvMain.text= "The logged in user is $username."
    }
}