package my.supercleanerapp.ui.activites

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import my.supercleanerapp.R
import my.supercleanerapp.databinding.ActivityDashboardBinding
import my.supercleanerapp.firestore.FirestoreClass
import my.supercleanerapp.models.User
import my.supercleanerapp.utils.GlideLoader


class DashboardActivity : BaseActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var mUserDetails: User
    private var userAppAdmin: Boolean = false // default value is false

    private fun getUserDetails() {
        // Show the progress dialog
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of Firestore class to get the user details from firestore which is already created.
        FirestoreClass().getUserDetails(this@DashboardActivity)
        hideProgressDialog()

    }

    fun userDetailsSuccess(user: User) {
        mUserDetails = user
        userAppAdmin = mUserDetails.userAppAdmin
        println("userAppAdmin is $userAppAdmin")

        // Setup navigation with the correct value of userAppAdmin
        setupNavigation()

        // Hide the progress dialog
        hideProgressDialog()
    }

    private fun setupNavigation() {
        supportActionBar!!.setBackgroundDrawable(
            ContextCompat.getDrawable(
                this@DashboardActivity,
                R.drawable.app_gradient_color_background
            )
        )
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val menu: Menu = navView.menu
        val navigationServicesItem: MenuItem = menu.findItem(R.id.navigation_services)
        val navigationReservedServicesItem: MenuItem = menu.findItem(R.id.navigation_reserved_service)
        val navigationReservationServiceItem: MenuItem = menu.findItem(R.id.navigation_reservations)
        if (userAppAdmin) {
            navigationServicesItem.isVisible = true
            navigationReservedServicesItem.isVisible = true
            navigationReservationServiceItem.isVisible = false

        } else {
            navigationServicesItem.isVisible = false
            navigationReservedServicesItem.isVisible = false
        }

        // Update the bottom navigation view based on userAppAdmin value
        val navBottomView: BottomNavigationView = findViewById(R.id.bottom_navigation_view)
        val navBottomMenu: Menu = navBottomView.menu
        val navBottomServicesItem: MenuItem = navBottomMenu.findItem(R.id.navigation_services)
        val navBottomReservedServicesItem: MenuItem = navBottomMenu.findItem(R.id.navigation_reserved_service)
        val navBottomReservationServicesItem: MenuItem = navBottomMenu.findItem(R.id.navigation_reservations)
        if (userAppAdmin) {
            navBottomServicesItem.isVisible = true
            navBottomReservedServicesItem.isVisible = true
            navBottomReservationServicesItem.isVisible = false

        } else {
            navBottomServicesItem.isVisible = false
            navBottomReservedServicesItem.isVisible = false
        }

        val navController = findNavController(R.id.nav_host_fragment_content_dashboard)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_services, R.id.navigation_dashboard, R.id.navigation_reservations,R.id.navigation_reserved_service
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navBottomView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_services -> {
                    navController.navigate(R.id.navigation_services)
                    true
                }
                R.id.navigation_dashboard -> {
                    navController.navigate(R.id.navigation_dashboard)
                    true
                }
                R.id.navigation_reserved_service -> {
                    navController.navigate(R.id.navigation_reserved_service)
                    true
                }
                R.id.navigation_reservations -> {
                    navController.navigate(R.id.navigation_reservations)
                    true
                }
                R.id.item_settings -> {
                    val intent = Intent(this@DashboardActivity, SettingsActivity::class.java)
                    startActivity(intent)
                }
                R.id.item_cart -> {
                    val intent = Intent(this@DashboardActivity, CartListActivity::class.java)
                    startActivity(intent)
                }
                R.id.item_logout -> {
                    FirebaseAuth.getInstance().signOut()

                    val intent = Intent(this@DashboardActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                else -> false
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getUserDetails()
    }

    override fun onResume() {
        super.onResume()

        getUserDetails()
    }




    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_dashboard)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
