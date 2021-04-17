package com.ahmedrafat.ordersapp.ui.mainfragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmedrafat.ordersapp.R
import com.ahmedrafat.ordersapp.databinding.MainFragmentBinding
import com.ahmedrafat.ordersapp.ui.mainfragment.adapter.OrdersAdapter
import com.ahmedrafat.ordersapp.utils.dismissLoading
import com.ahmedrafat.ordersapp.utils.showLoading
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
            findNavController().navigate(R.id.action_mainFragment_to_addOrderFragment,)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mainViewModel.getMainOrders()
        mainViewModel.ordersLiveData.observe(requireActivity(), {
            if(it.isNotEmpty()) {
                mainFragmentBindings.tvNoOrders.visibility = View.GONE
                mainFragmentBindings.btNewOrder.visibility = View.GONE
                val ordersAdapter = OrdersAdapter(it)
                mainFragmentBindings.rvOrders.layoutManager = LinearLayoutManager(requireContext())
                mainFragmentBindings.rvOrders.adapter = ordersAdapter
                ordersAdapter.notifyDataSetChanged()
            }else{
                mainFragmentBindings.tvNoOrders.visibility = View.VISIBLE
                mainFragmentBindings.btNewOrder.visibility = View.VISIBLE
            }
        })

        mainViewModel.loading.observe(requireActivity(), {
            if(it) showLoading(requireContext()) else dismissLoading()
        })
    }

}