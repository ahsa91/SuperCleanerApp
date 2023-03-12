package my.supercleanerapp.ui.activites

import android.content.Intent
import android.os.Bundle
import android.view.Menu
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
///////////////put nav bar declarations here
class DashboardActivity : BaseActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDashboardBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportActionBar!!.setBackgroundDrawable(
            ContextCompat.getDrawable(
                this@DashboardActivity,
                R.drawable.app_gradient_color_background
            )
        )
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navBottomView : BottomNavigationView = findViewById(R.id.bottom_navigation_view)
        val navController = findNavController(R.id.nav_host_fragment_content_dashboard)




        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_services, R.id.navigation_dashboard, R.id.navigation_reservations
            ), drawerLayout
        )



        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navBottomView.setupWithNavController(navController)
        //collapse nav menu after selecting fragment
        navView.setNavigationItemSelectedListener { menuItem ->
            // Handle menu item clicks here
            when (menuItem.itemId) {
                R.id.navigation_services -> {
                    navController.navigate(R.id.navigation_services)
                    true
                }
                R.id.navigation_dashboard -> {
                    navController.navigate(R.id.navigation_dashboard)
                    true
                }
                R.id.navigation_reservations -> {
                    navController.navigate(R.id.navigation_reservations)
                    true
                }
                R.id.item_settings ->{
                    val intent = Intent(this@DashboardActivity, SettingsActivity::class.java)
                    startActivity(intent)
                }
                R.id.item_cart ->{
                    val intent = Intent(this@DashboardActivity, CartListActivity::class.java)
                    startActivity(intent)
                }
                R.id.item_logout ->{
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

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_dashboard)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }



}