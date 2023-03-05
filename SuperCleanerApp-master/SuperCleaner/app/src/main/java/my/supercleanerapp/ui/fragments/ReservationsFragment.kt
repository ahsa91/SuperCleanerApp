package my.supercleanerapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import my.supercleanerapp.R

class ReservationsFragment : Fragment() {

    /*private lateinit var notificationsViewModel: NotificationsViewModel*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*notificationsViewModel =
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)*/

        val root = inflater.inflate(R.layout.fragment_reservations, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        textView.text = "This is notifications Fragment"

        /*notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/
        return root
    }
}