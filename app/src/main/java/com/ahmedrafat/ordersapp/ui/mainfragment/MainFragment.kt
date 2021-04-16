package com.ahmedrafat.ordersapp.ui.mainfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ahmedrafat.ordersapp.R
import com.ahmedrafat.ordersapp.databinding.MainFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment) {

    //Inject viewmodel
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var mainFragmentBindings: MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainFragmentBindings = MainFragmentBinding.inflate(inflater, container, false)
        return mainFragmentBindings.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainFragmentBindings.btNewOrder.setOnClickListener {
            mainFragmentBindings.fabAddOrder.performClick()
        }
        mainFragmentBindings.fabAddOrder.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_addOrderFragment)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

}