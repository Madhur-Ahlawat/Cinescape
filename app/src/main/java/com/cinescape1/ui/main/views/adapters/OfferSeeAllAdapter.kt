package com.cinescape1.ui.main.views.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.MoviesResponse
import com.cinescape1.data.models.responseModel.OfferResponse
import com.cinescape1.ui.main.views.OfferDetailsActivity
import com.cinescape1.utils.Constant

class OfferSeeAllAdapter(private val context: Context, private val items: ArrayList<OfferResponse.Output>) :
    RecyclerView.Adapter<OfferSeeAllAdapter.TodoViewHolder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.offer_see_all_item, null)
        return TodoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val obj = items[position]
        Glide.with(context)
            .load(obj.appImageUrl)
            .placeholder(R.drawable.bombshell)
            .into(holder.todoImage)

        holder.todoImage.setOnClickListener {
            val intent = Intent(context, OfferDetailsActivity::class.java)
            intent.putExtra(Constant.IntentKey.OFFER_ID,obj.id.toString())
            print("offerData--->${obj.id}")
            context.startActivity(intent)
        }
    }

    inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var todoImage: ImageView = view.findViewById(R.id.imageView16) as ImageView
    }

    interface RecycleViewItemClickListener {
        fun onItemClick(view: MoviesResponse.Output, title: String)
    }
}