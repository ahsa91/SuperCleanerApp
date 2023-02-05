package my.supercleanerapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import my.supercleanerapp.R

class ProductsFragment : Fragment() {

    /*private lateinit var homeViewModel: HomeViewModel*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)*/

        val root = inflater.inflate(R.layout.fragment_products, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        textView.text = "This is home Fragment"

        /*homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/

        return root
    }

}