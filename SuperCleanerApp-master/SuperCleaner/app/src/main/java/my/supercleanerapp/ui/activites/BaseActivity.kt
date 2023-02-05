package my.supercleanerapp.ui.activites

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import my.supercleanerapp.R

/**
 * A base activity class is used to define the functions and members which we will use in all the ui.
 * It inherits the AppCompatActivity class so in other activity class we will replace the AppCompatActivity with BaseActivity.
 */
@Suppress("DEPRECATION")

open class BaseActivity : AppCompatActivity() {
   // A global variable for double back press feature.
    private var doubleBackToExitPressedOnce = false
    /**
     * This is a progress dialog instance which we will initialize later on.
     */
    private lateinit var mProgressDialog: Dialog


    /**
     * A function to show the success and error messages in snack bar component.
     */
    fun showErrorSnackBar(message: String, errorMessage: Boolean) {
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if (errorMessage) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.colorSnackBarError
                )
            )
        }else{
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.colorSnackBarSuccess
                )
            )
        }
        snackBar.show()
    }

    /**
     * This function is used to show the progress dialog with the title and message to user.
     */
    fun showProgressDialog(text: String) {
        mProgressDialog = Dialog(this)


        /*Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen.*/
        mProgressDialog.setContentView(R.layout.dialog_progress)
//        mProgressDialog.findViewById is a method that returns the view with the specified ID from the layout inflated in the Dialog.

//        The method is called with the argument R.id.tv_progress_text, which is the ID of the TextView we want to access.
//
//        The view returned by findViewById is cast to a TextView using the <TextView> syntax.
//
//        Finally, the text property of the TextView is set to the argument "text" passed to the showProgressDialog function.
//
//        So this line of code sets the text of the TextView inside the Dialog to the value of the "text" argument.
        mProgressDialog.findViewById<TextView>(R.id.tv_progress_text).text = text

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        //Start the dialog and display it on screen.
        mProgressDialog.show()
    }

    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }

    /**
     * A function to implement the double back press feature to exit the app.
     */
    fun doubleBackToExit() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true

        Toast.makeText(
            this,
            resources.getString(R.string.please_click_back_again_to_exit),
            Toast.LENGTH_SHORT
        ).show()


        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }




}
