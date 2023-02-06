package my.supercleanerapp.ui.activites

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import my.supercleanerapp.R
import my.supercleanerapp.databinding.ActivityAddressListBinding
import my.supercleanerapp.firestore.FirestoreClass
import my.supercleanerapp.models.Address
import my.supercleanerapp.ui.adapters.AddressListAdapter
import my.supercleanerapp.utils.SwipeToEditCallback

@Suppress("DEPRECATION")
class AddressListActivity : BaseActivity() {



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
        //get the address list.
        getAddressList()


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

    /**
     * A function to get the success result of address list from cloud firestore.
     *
     * @param addressList
     */
    fun successAddressListFromFirestore(addressList: ArrayList<Address>) {

        // Hide the progress dialog
        hideProgressDialog()


        // Print all the list of addresses in the log with name.
        for (i in addressList) {

            Log.i("Name and Address", "${i.name} ::  ${i.address}")
        }
        if (addressList.size > 0) {

            binding.rvAddressList.visibility = View.VISIBLE
            binding.tvNoAddressFound.visibility = View.GONE

            binding.rvAddressList.layoutManager = LinearLayoutManager(this@AddressListActivity)
            binding.rvAddressList.setHasFixedSize(true)

            val addressAdapter = AddressListAdapter(this@AddressListActivity, addressList)
            binding.rvAddressList.adapter = addressAdapter

            val editSwipeHandler = object : SwipeToEditCallback(this) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                // Call the notifyEditItem function of the adapter class.

                    val adapter = binding.rvAddressList.adapter as AddressListAdapter
                    adapter.notifyEditItem(
                        this@AddressListActivity,
                        viewHolder.adapterPosition
                    )

                }
            }
            val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
            editItemTouchHelper.attachToRecyclerView(binding.rvAddressList)
            // END
        } else {
            binding.rvAddressList.visibility = View.GONE
            binding.tvNoAddressFound.visibility = View.VISIBLE
        }
    }

    /**
     * A function to get the list of address from cloud firestore.
     */
    private fun getAddressList() {

        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getAddressesList(this@AddressListActivity)
    }



}
