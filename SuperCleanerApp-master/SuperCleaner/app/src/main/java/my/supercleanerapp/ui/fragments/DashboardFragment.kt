package my.supercleanerapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import my.supercleanerapp.R
import my.supercleanerapp.databinding.FragmentDashboardBinding
import my.supercleanerapp.firestore.FirestoreClass
import my.supercleanerapp.models.Service
import my.supercleanerapp.ui.activites.CartListActivity
import my.supercleanerapp.ui.activites.SettingsActivity
import my.supercleanerapp.ui.adapters.MyDashboardListAdapter

@Suppress("DEPRECATION")
class DashboardFragment : BaseFragment() {

    private var _fragBinding:FragmentDashboardBinding?=null
    private val fragBinding get() = _fragBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // If we want to use the option menu in fragment we need to add it.
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        return root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.action_settings -> {


                startActivity(Intent(activity, SettingsActivity::class.java))
                return true
            }
            R.id.action_cart -> {
                startActivity(Intent(activity, CartListActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * A function to get the successful service list from cloud firestore.
     *
     * @param dashboardList Will receive the service list from cloud firestore.
     */
    fun successDashboardListFromFireStore(dashboardList: ArrayList<Service>) {

        // Hide Progress dialog.
        hideProgressDialog()

        if (dashboardList.size > 0) {
            fragBinding.rvMyServiceItems.visibility = View.VISIBLE
            fragBinding.tvNoServicesFound.visibility = View.GONE

            fragBinding.rvMyServiceItems.layoutManager = LinearLayoutManager(activity)
            fragBinding.rvMyServiceItems.setHasFixedSize(true)


            val adapterServices =
                MyDashboardListAdapter(requireActivity(), dashboardList, this@DashboardFragment)

            fragBinding.rvMyServiceItems.adapter = adapterServices
        } else {
            fragBinding.rvMyServiceItems.visibility = View.GONE
            fragBinding.tvNoServicesFound.visibility = View.VISIBLE
        }
    }

    private fun getDashboardListFromFireStore() {
        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of Firestore class.
        FirestoreClass().getDashboardList(this@DashboardFragment)
    }

    override fun onResume() {
        super.onResume()

        getDashboardListFromFireStore()
    }


}