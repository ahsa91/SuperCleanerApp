package com.supercleanerapp.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.supercleanerapp.R
import com.supercleanerapp.databinding.ActivityLoginBinding

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    private lateinit var binding:ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // This is used to hide the status bar and make the login screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding.tvRegister.setOnClickListener {

            // Launch the register screen when the user clicks on the text.
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

    }
}