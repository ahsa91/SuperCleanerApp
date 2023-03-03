package my.supercleanerapp.ui.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import my.supercleanerapp.R
import my.supercleanerapp.databinding.ActivityCartListBinding
import my.supercleanerapp.firestore.FirestoreClass
import my.supercleanerapp.models.Cart

class CartListActivity : BaseActivity() {
    /**
     * This function is auto created by Android when the Activity Class is created.
     */

    private lateinit var binding:ActivityCartListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        binding=ActivityCartListBinding.inflate(layoutInflater)
        // This is used to align the xml view to this class
        setContentView(binding.root)

        setupActionBar()

    }


    override fun onResume() {
        super.onResume()

        getCartItemsList()
    }



    /**
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarCartListActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        binding.toolbarCartListActivity.setNavigationOnClickListener { onBackPressed() }
    }


    private fun getCartItemsList() {

        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getCartList(this@CartListActivity)
    }

    /**
     * A function to notify the success result of the cart items list from cloud firestore.
     */
    fun successCartItemsList(cartList: ArrayList<Cart>) {

        // Hide progress dialog.
        hideProgressDialog()

        for (i in cartList) {

            Log.i("Cart Item Title", i.title)

        }
    }

}