package com.cinescape1.ui.main.views.adapters.moviesFragmentAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.MoviesResponse
import com.cinescape1.ui.main.views.details.ShowTimesActivity
import com.cinescape1.utils.Constant
import com.cinescape1.utils.hide

class ComingSoonChildAdapter(val comingSoon: ArrayList<MoviesResponse.ComingSoons>, val mContext: Context) :
    RecyclerView.Adapter<ComingSoonChildAdapter.MyViewHolderNowShowing>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.comming_soon_list, parent, false)
        return MyViewHolderNowShowing(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val comingSoonItem = comingSoon[position]

        Glide.with(mContext)
            .load(comingSoonItem.mobimgsmall)
            .error(R.drawable.pos_not_avilbale)
            .into(holder.thumbnail)

        holder.movieCategory.hide()
        holder.type.hide()
        holder.movieTitle.hide()
        holder.movieTitle.text = comingSoonItem.title
        holder.movieCategory.text = comingSoonItem.distributorName

        holder.thumbnail.setOnClickListener {
            val intent = Intent(holder.thumbnail.context, ShowTimesActivity::class.java)
            intent.putExtra(Constant.IntentKey.MOVIE_ID, comingSoonItem.id)
            intent.putExtra("type", "comingSoon")
            holder.thumbnail.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return if (comingSoon.isNotEmpty()) comingSoon.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var thumbnail: ImageView = view.findViewById(R.id.image_now_showing)
        var movieTitle: TextView = view.findViewById(R.id.text_movie_title)
        var movieCategory: TextView = view.findViewById(R.id.text_movie_category)
        var type: TextView = view.findViewById(R.id.text_movie_type)
    }

    @SuppressLint("NotifyDataSetChanged")
     fun updateList(updatedList: ArrayList<MoviesResponse.Comingsoon>){
//        nowShowingList = updatedList
        notifyDataSetChanged()
    }

}