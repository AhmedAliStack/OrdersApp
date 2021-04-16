package com.ahmedrafat.ordersapp.ui.addorderfragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ahmedrafat.ordersapp.R
import com.ahmedrafat.ordersapp.model.apimodels.ShopperModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddOrderFragment : Fragment() {

    private val viewModel: AddOrderViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_order_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.getShoppers()

        observeResponse()
    }

    private fun observeResponse() {
        viewModel.shoppersModelMutableLiveData.observe(requireActivity(), {
            val arrayAdapter:ArrayAdapter<ShopperModel> = ArrayAdapter(requireContext(),android.R.layout.simple_expandable_list_item_1,it)

        })
    }

}