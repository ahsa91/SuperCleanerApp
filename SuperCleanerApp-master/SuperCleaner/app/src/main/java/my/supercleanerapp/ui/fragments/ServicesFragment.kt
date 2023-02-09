package my.supercleanerapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import my.supercleanerapp.R
import my.supercleanerapp.ui.activites.AddServiceActivity


@Suppress("DEPRECATION")

class ServicesFragment : Fragment() {

    /*private lateinit var homeViewModel: HomeViewModel*/

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
        val textView: TextView = root.findViewById(R.id.text_service)
        textView.text = "This is service Fragment"

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

}