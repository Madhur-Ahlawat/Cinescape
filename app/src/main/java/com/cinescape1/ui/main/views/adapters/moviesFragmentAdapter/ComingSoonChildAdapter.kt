package com.cinescape1.ui.main.views.adapters.moviesFragmentAdapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.MoviesResponse
import com.cinescape1.ui.main.views.details.commingSoon.ComingSoonActivity
import com.cinescape1.utils.Constant

class ComingSoonChildAdapter(val comingSoon: ArrayList<MoviesResponse.ComingSoons>, val mContext: Activity) :
    RecyclerView.Adapter<ComingSoonChildAdapter.MyViewHolderNowShowing>() {
    private val displayMetrics = DisplayMetrics()
    private var screenWidth = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.com_soon_item, parent, false)
        return MyViewHolderNowShowing(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val comingSoonItem = comingSoon[position]

//        mContext.windowManager.defaultDisplay.getMetrics(displayMetrics)
//        screenWidth = displayMetrics.widthPixels
//        holder.thumbnail.layoutParams.width = ((screenWidth)/3.69f).toInt()
//        println("NowShowingScreenWidth21-------->${screenWidth}")

        Glide.with(mContext)
            .load(comingSoonItem.mobimgsmall)
            .error(R.drawable.pos_not_avilbale)
            .into(holder.thumbnail)

        holder.thumbnail.setOnClickListener {
            val intent = Intent(holder.thumbnail.context, ComingSoonActivity::class.java)
            intent.putExtra(Constant.IntentKey.MOVIE_ID, comingSoonItem.id)
            intent.putExtra("type", "comingSoon")
            println("ComingSoonActivity2121--------->${"yes"}")
            holder.thumbnail.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return if (comingSoon.isNotEmpty()) comingSoon.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var thumbnail: ImageView = view.findViewById(R.id.imageView62)
    }

    @SuppressLint("NotifyDataSetChanged")
     fun updateList(updatedList: ArrayList<MoviesResponse.Comingsoon>){
//        nowShowingList = updatedList
        notifyDataSetChanged()
    }

}