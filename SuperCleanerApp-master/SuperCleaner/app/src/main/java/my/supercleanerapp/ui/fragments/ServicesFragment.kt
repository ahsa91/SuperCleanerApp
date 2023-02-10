package my.supercleanerapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import my.supercleanerapp.R
import my.supercleanerapp.databinding.FragmentServicesBinding
import my.supercleanerapp.firestore.FirestoreClass
import my.supercleanerapp.models.Service
import my.supercleanerapp.ui.activites.AddServiceActivity
import my.supercleanerapp.ui.adapters.MyServicesListAdapter


@Suppress("DEPRECATION")

class ServicesFragment : BaseFragment() {

    private var _fragBinding:FragmentServicesBinding?=null
    private val fragBinding get() = _fragBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)*/
        _fragBinding = FragmentServicesBinding.inflate(inflater, container, false)
        val root = fragBinding.root
//        val root = inflater.inflate(R.layout.fragment_services, container, false)

        /*homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/

        return root
    }

    //verride the onCreateOptionsMenu function and inflate the Add Service menu.
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_service_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_add_service) {
            // Launch the add service activity.

            startActivity(Intent(activity, AddServiceActivity::class.java))

            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * A function to get the successful service list from cloud firestore.
     *
     * @param servicesList Will receive the service list from cloud firestore.
     */
    fun successServicesListFromFireStore(servicesList: ArrayList<Service>) {

        // Hide Progress dialog.
        hideProgressDialog()

        for(i in servicesList){
            Log.i("service name",i.title)
        }


        if (servicesList.size > 0) {
            fragBinding.rvMyServiceItems.visibility = View.VISIBLE
            fragBinding.tvNoServicesFound.visibility = View.GONE

            fragBinding.rvMyServiceItems.layoutManager = LinearLayoutManager(activity)
            fragBinding.rvMyServiceItems.setHasFixedSize(true)


            val adapterServices =
                MyServicesListAdapter(requireActivity(), servicesList, this@ServicesFragment)

            fragBinding.rvMyServiceItems.adapter = adapterServices
        } else {
            fragBinding.rvMyServiceItems.visibility = View.GONE
            fragBinding.tvNoServicesFound.visibility = View.VISIBLE
        }
    }

    private fun getServiceListFromFireStore() {
        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of Firestore class.
        FirestoreClass().getServicesList(this@ServicesFragment)
    }

    override fun onResume() {
        super.onResume()

        getServiceListFromFireStore()
    }

    /**
     * A function that will call the delete function of FirestoreClass that will delete the service added by the user.
     *
     * @param serviceID To specify which service need to be deleted.
     */
    fun deleteService(serviceID: String) {

        // Here we will call the delete function of the FirestoreClass. But, for now lets display the Toast message and call this function from adapter class.

        Toast.makeText(
            requireActivity(),
            "You can now delete the service. $serviceID",
            Toast.LENGTH_SHORT
        ).show()
    }

}