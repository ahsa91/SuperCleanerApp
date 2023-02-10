package my.supercleanerapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import my.supercleanerapp.R
import my.supercleanerapp.firestore.FirestoreClass
import my.supercleanerapp.models.Service
import my.supercleanerapp.ui.activites.AddServiceActivity


@Suppress("DEPRECATION")

class ServicesFragment : BaseFragment() {


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

        val root = inflater.inflate(R.layout.fragment_services, container, false)

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
            Log.i("Product name",i.title)
        }

//        if (servicesList.size > 0) {
//            rv_my_product_items.visibility = View.VISIBLE
//            tv_no_products_found.visibility = View.GONE
//
//            rv_my_product_items.layoutManager = LinearLayoutManager(activity)
//            rv_my_product_items.setHasFixedSize(true)
//
//            // TODO Step 7: Pass the third parameter value.
//            // START
//            val adapterProducts =
//                MyProductsListAdapter(requireActivity(), servicesList, this@ServicesFragment)
//            // END
//            rv_my_product_items.adapter = adapterProducts
//        } else {
//            rv_my_product_items.visibility = View.GONE
//            tv_no_products_found.visibility = View.VISIBLE
//        }
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

}