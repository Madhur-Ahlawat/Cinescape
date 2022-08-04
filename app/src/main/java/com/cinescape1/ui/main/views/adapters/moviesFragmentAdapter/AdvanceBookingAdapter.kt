package com.cinescape1.ui.main.views.adapters.moviesFragmentAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.MoviesResponse
import com.cinescape1.ui.main.views.ShowTimesActivity
import com.cinescape1.utils.Constant
import com.cinescape1.utils.hide

class AdvanceBookingAdapter(private var nowShowingList: List<MoviesResponse.AdvanceBooking>, context:Context) :
    RecyclerView.Adapter<AdvanceBookingAdapter.MyViewHolderNowShowing>() {
    private var mContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_now_showing_item, parent, false)
        return MyViewHolderNowShowing(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val comingSoonItem = nowShowingList[position]

        Glide.with(mContext)
            .load(comingSoonItem.mobimgsmall)
            .error(R.drawable.pos_not_avilbale)
            .into(holder.thumbnail)

        holder.movieTitle.text = comingSoonItem.title
        holder.type.text=comingSoonItem.rating
        when (comingSoonItem.rating) {
            "PG" -> {
                holder.cardView.setCardBackgroundColor(mContext.resources.getColor(R.color.grey))

            }
            "G" -> {
                holder.cardView.setCardBackgroundColor(mContext.resources.getColor(R.color.green))

            }
            "18+" -> {
                holder.cardView.setCardBackgroundColor(mContext.resources.getColor(R.color.red))

            }
            "13+" -> {
                holder.cardView.setCardBackgroundColor(mContext.resources.getColor(R.color.yellow))

            }
            "E" -> {
                holder.cardView.setCardBackgroundColor(mContext.resources.getColor(R.color.wowOrange))

            }
            "T" -> {
                holder.cardView.setCardBackgroundColor(mContext.resources.getColor(R.color.tabIndicater))

            }
            else -> {
                holder.cardView.setCardBackgroundColor(mContext.resources.getColor(R.color.blue))
            }
        }

        holder.movieCategory.text = comingSoonItem.language+" | "+comingSoonItem.genre

        holder.thumbnail.setOnClickListener {
            val intent = Intent(holder.thumbnail.context, ShowTimesActivity::class.java)
            intent.putExtra(Constant.IntentKey.MOVIE_ID, comingSoonItem.id)
            holder.thumbnail.context.startActivity(intent)
        }

        holder.thumbnail.setOnClickListener {
            val intent = Intent(holder.thumbnail.context, ShowTimesActivity::class.java)
            intent.putExtra(Constant.IntentKey.MOVIE_ID, comingSoonItem.id)
            intent.putExtra("type", "comingSoon")
            holder.thumbnail.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var thumbnail: ImageView = view.findViewById(R.id.image_now_showing)
        var movieTitle: TextView = view.findViewById(R.id.text_movie_title)
        var movieCategory: TextView = view.findViewById(R.id.text_movie_category)
        var type: TextView = view.findViewById(R.id.movieRating)
        var cardView: CardView = view.findViewById(R.id.rating_ui)
    }

    @SuppressLint("NotifyDataSetChanged")
    public fun updateList(updatedList: ArrayList<MoviesResponse.AdvanceBooking>){
        nowShowingList = updatedList
        notifyDataSetChanged()
    }

}