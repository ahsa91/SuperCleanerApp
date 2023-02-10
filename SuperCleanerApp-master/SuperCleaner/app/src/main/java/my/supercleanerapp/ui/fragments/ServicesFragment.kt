package my.supercleanerapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

        showAlertDialogToDeleteProduct(serviceID)
    }

    /**
     * A function to notify the success result of servuce deleted from cloud firestore.
     */
    fun serviceDeleteSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(
            requireActivity(),
            resources.getString(R.string.err_your_address_deleted_successfully),
            Toast.LENGTH_SHORT
        ).show()

        // Get the latest service list from cloud firestore.
        getServiceListFromFireStore()
    }

    /**
     * A function to show the alert dialog for the confirmation of delete service from cloud firestore.
     */
    private fun showAlertDialogToDeleteProduct(serviceID: String) {

        val builder = AlertDialog.Builder(requireActivity())
        //set title for alert dialog
        builder.setTitle(resources.getString(R.string.delete_dialog_title))
        //set message for alert dialog
        builder.setMessage(resources.getString(R.string.delete_dialog_message))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, _ ->

            //  Call the function to delete the service from cloud firestore.
            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))

            // Call the function of Firestore class.
            FirestoreClass().deleteService(this@ServicesFragment, serviceID)
            // END

            dialogInterface.dismiss()
        }

        //performing negative action
        builder.setNegativeButton(resources.getString(R.string.no)) { dialogInterface, _ ->

            dialogInterface.dismiss()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

}