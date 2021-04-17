package com.ahmedrafat.ordersapp.ui.reviewsfragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmedrafat.ordersapp.R
import com.ahmedrafat.ordersapp.databinding.ReviewsFragmentBinding
import com.ahmedrafat.ordersapp.ui.reviewsfragment.adapter.ReviewsAdapter
import com.ahmedrafat.ordersapp.utils.dismissLoading
import com.ahmedrafat.ordersapp.utils.showLoading
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewsFragment : Fragment() {

    private val viewModel:ReviewsViewModel by viewModels()
    private lateinit var reviewsFragmentBinding:ReviewsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        reviewsFragmentBinding = ReviewsFragmentBinding.inflate(LayoutInflater.from(requireContext()))
        return reviewsFragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.getReviews()
        viewModel.reviewsLiveData.observe(requireActivity(),{
            Log.d("Reviews",it.toString())
            val reviewsAdapter = ReviewsAdapter(it)
            reviewsFragmentBinding.rvOrders.adapter = reviewsAdapter
            reviewsFragmentBinding.rvOrders.layoutManager = LinearLayoutManager(requireContext())
            reviewsAdapter.notifyDataSetChanged()
        })

        viewModel.loading.observe(requireActivity(), {
            if(it) showLoading(requireContext()) else dismissLoading()
        })

        viewModel.errorMutableLiveData.observe(requireActivity(),{
            Log.d("Error",it)
        })
    }

}