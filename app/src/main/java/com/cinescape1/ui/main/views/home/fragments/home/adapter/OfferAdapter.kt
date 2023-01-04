package com.cinescape1.ui.main.views.home.fragments.home.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.HomeDataResponse
import com.cinescape1.data.models.responseModel.MoviesResponse
import com.cinescape1.ui.main.views.home.fragments.home.offerDetails.OfferDetailsActivity
import com.cinescape1.utils.Constant

class OfferAdapter(val mContext: Activity, val movieDataList: ArrayList<HomeDataResponse.Offer>) :
    RecyclerView.Adapter<OfferAdapter.TodoViewHolder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.offer_item, null)
        return TodoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movieDataList.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val obj = movieDataList[position]
        Glide.with(mContext)
            .load(obj.appThumbImageUrl)
            .error(R.drawable.placeholder_home_small_poster)
            .into(holder.todoImage)

        holder.todoImage.setOnClickListener {
            val intent = Intent(mContext, OfferDetailsActivity::class.java)
            intent.putExtra(Constant.IntentKey.OFFER_ID,obj.id.toString())
            mContext.startActivity(intent)
        }

    }

    inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var todoImage: ImageView = view.findViewById(R.id.imageView16) as ImageView
    }

}