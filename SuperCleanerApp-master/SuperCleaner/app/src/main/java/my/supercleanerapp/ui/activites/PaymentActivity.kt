package my.supercleanerapp.ui.activites

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.stripe.android.*
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.PaymentMethod
import com.stripe.android.view.BillingAddressFields
import my.supercleanerapp.R
import my.supercleanerapp.firestore.FirebaseEphemeralKeyProvider
import my.supercleanerapp.databinding.ActivityPaymentBinding
import my.supercleanerapp.ui.fragments.DashboardFragment

@Suppress("DEPRECATION")
class PaymentActivity : AppCompatActivity() {

    private var currentUser: FirebaseUser? = null
    private lateinit var paymentSession: PaymentSession
    private lateinit var selectedPaymentMethod: PaymentMethod
    private val stripe: Stripe by lazy { Stripe(applicationContext, PaymentConfiguration.getInstance(applicationContext).publishableKey) }
    var RC_SIGN_IN = 1
    private var mTotalAmount: Double = 0.0

    private lateinit var binding: ActivityPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize PaymentConfiguration
        PaymentConfiguration.init(applicationContext, "pk_test_51L8PnGAbMWXxjwlWmnXIS3B0ZF8tAVFh0NEBF8HBT3JiqL7AKFFSdJzuWRvMMrv7Uq9ZF9E4d9OuNMROlaY4Wmnf00duWPWcQH")

            binding.loginButton.setOnClickListener {
            // login to firebase
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build())

            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(),
                RC_SIGN_IN
            )
        }

        binding.payButton.setOnClickListener {
            confirmPayment(selectedPaymentMethod.id!!)
        }

        binding.paymentmethod.setOnClickListener {
            // Create the customer session and kick start the payment flow
            paymentSession.presentPaymentMethodSelection()
        }
        mTotalAmount = intent.getDoubleExtra("TOTAL_AMOUNT", 50.0)


        showUI()
    }


    private fun confirmPayment(paymentMethodId: String) {
        binding.payButton.isEnabled = false

        val paymentCollection = Firebase.firestore
            .collection("stripe_customers").document(currentUser?.uid?:"")
            .collection("payments")

        // Add a new document with a generated ID
        paymentCollection.add(hashMapOf(
            "amount" to (mTotalAmount * 100).toInt(),
            "currency" to "eur"
        ))
            .addOnSuccessListener { documentReference ->
                Log.d("payment", "DocumentSnapshot added with ID: ${documentReference.id}")
                documentReference.addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.w("payment", "Listen failed.", e)
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Log.d("payment", "Current data: ${snapshot.data}")
                        val clientSecret = snapshot.data?.get("client_secret")
                        Log.d("payment", "Create paymentIntent returns $clientSecret")
                        clientSecret?.let {
                            stripe.confirmPayment(this, ConfirmPaymentIntentParams.createWithPaymentMethodId(
                                paymentMethodId,
                                (it as String)
                            ))

                            binding.checkoutSummary.text = "Thank you for your payment"
                            Toast.makeText(applicationContext, "Payment Done!!", Toast.LENGTH_LONG).show()
                            val intent = Intent(this@PaymentActivity, DashboardFragment::class.java)
                            startActivity(intent)
                        }
                    } else {
                        Log.e("payment", "Current payment intent : null")
                        binding.payButton.isEnabled = true
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.w("payment", "Error adding document", e)
                binding.payButton.isEnabled = true
            }
    }

    private fun showUI() {
        currentUser?.let {
            binding.loginButton.visibility = View.INVISIBLE

            binding.greeting.visibility = View.VISIBLE
            binding.checkoutSummary.visibility = View.VISIBLE
            binding.payButton.visibility = View.VISIBLE
            binding.paymentmethod.visibility = View.VISIBLE

            binding.greeting.text = "Hello ${it.displayName}"

            setupPaymentSession()
        }?: run {
            // User does not login
            binding.loginButton.visibility = View.VISIBLE

            binding.greeting.visibility = View.INVISIBLE
            binding.checkoutSummary.visibility = View.INVISIBLE
            binding.paymentmethod.visibility = View.INVISIBLE
            binding.payButton.visibility = View.INVISIBLE
            binding.payButton.isEnabled = false

        }
    }

    private fun setupPaymentSession () {
        // Setup Customer Session
        CustomerSession.initCustomerSession(this, FirebaseEphemeralKeyProvider())
        // Setup a payment session
        paymentSession = PaymentSession(this, PaymentSessionConfig.Builder()
            .setShippingInfoRequired(false)
            .setShippingMethodsRequired(false)
            .setBillingAddressFields(BillingAddressFields.None)
            .setShouldShowGooglePay(true)
            .build())

        paymentSession.init(
            object: PaymentSession.PaymentSessionListener {
                override fun onPaymentSessionDataChanged(data: PaymentSessionData) {
                    Log.d("PaymentSession", "PaymentSession has changed: $data")
                    Log.d("PaymentSession", "${data.isPaymentReadyToCharge} <> ${data.paymentMethod}")

                    if (data.isPaymentReadyToCharge) {
                        Log.d("PaymentSession", "Ready to charge");
                        binding.payButton.isEnabled = true

                        data.paymentMethod?.let {
                            Log.d("PaymentSession", "PaymentMethod $it selected")
                            binding.paymentmethod.text = "${it.card?.brand} card ends with ${it.card?.last4}"
                            selectedPaymentMethod = it
                        }
                    }
                }

                override fun onCommunicatingStateChanged(isCommunicating: Boolean) {
                    Log.d("PaymentSession",  "isCommunicating $isCommunicating")
                }

                override fun onError(errorCode: Int, errorMessage: String) {
                    Log.e("PaymentSession",  "onError: $errorCode, $errorMessage")
                }
            }
        )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                currentUser = FirebaseAuth.getInstance().currentUser

                Log.d("Login", "User ${currentUser?.displayName} has signed in.")
                Toast.makeText(applicationContext, "Welcome ${currentUser?.displayName}", Toast.LENGTH_SHORT).show()
                showUI()
            } else {
                Log.d("Login", "Signing in failed!")
                Toast.makeText(applicationContext, response?.error?.message?:"Sign in failed", Toast.LENGTH_LONG).show()
            }
        } else {
            if (::paymentSession.isInitialized) {
                paymentSession.handlePaymentData(requestCode, resultCode, data ?: Intent())
            }
        }
    }

}