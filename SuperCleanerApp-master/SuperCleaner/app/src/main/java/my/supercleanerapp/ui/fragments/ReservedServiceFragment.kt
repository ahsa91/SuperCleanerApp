package my.supercleanerapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import my.supercleanerapp.R
import my.supercleanerapp.databinding.FragmentReservedServiceBinding


/**
 * A simple [Fragment] subclass.
 * Use the [ReservedServiceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReservedServiceFragment : BaseFragment() {

    private var _fragBinding: FragmentReservedServiceBinding?=null
    private val fragBinding get() = _fragBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reserved_service, container, false)
    }

}