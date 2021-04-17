package com.ahmedrafat.ordersapp.ui.reviewsfragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ahmedrafat.ordersapp.databinding.ReviewItemBinding
import com.ahmedrafat.ordersapp.model.apimodels.ReviewModel

class ReviewsAdapter(private val reviews: ReviewModel) :
    RecyclerView.Adapter<ReviewsAdapter.ViewHolder>() {

    lateinit var context: Context

    class ViewHolder(binding: ReviewItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val reviewsItemsBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(ReviewItemBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reviewModel = reviews[position]

        holder.reviewsItemsBinding.tvReview.text = if(!reviewModel.message.isNullOrEmpty()) reviewModel.message else "No Review !"

        when {
            reviewModel.rateValue != null -> {
                if (reviewModel.rateValue is Int)
                    holder.reviewsItemsBinding.rbRate.rating = reviewModel.rateValue.toFloat()
                else if(reviewModel.rateValue is String)
                    holder.reviewsItemsBinding.rbRate.rating = reviewModel.rateValue.toFloat()
            }
            reviewModel.rating != null -> {
                if (reviewModel.rating is Int)
                    holder.reviewsItemsBinding.rbRate.rating = reviewModel.rating.toFloat()
                else if(reviewModel.rating is String)
                    holder.reviewsItemsBinding.rbRate.rating = reviewModel.rating.toFloat()
            }
            reviewModel.review != null -> {
                if (reviewModel.review is Int)
                    holder.reviewsItemsBinding.rbRate.rating = reviewModel.review.toFloat()
                else if(reviewModel.review is String)
                    holder.reviewsItemsBinding.rbRate.rating = reviewModel.review.toFloat()
            }
        }
    }

    override fun getItemCount(): Int {
        return reviews.size
    }
}