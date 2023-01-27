package com.cinescape1.ui.main.views.adapters.sliderAdapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.HomeDataResponse
import com.cinescape1.ui.main.views.details.nowShowing.ShowTimesActivity
import com.cinescape1.utils.Constant
import com.cinescape1.utils.Constant.Companion.select_pos
import com.github.islamkhsh.CardSliderAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.home_slider_item.view.*


 class HomeFrontSliderAdapter(
    private val mContext: Activity,
    private val movies: ArrayList<HomeDataResponse.MovieData>,
    val viewpager: androidx.viewpager2.widget.ViewPager2
) : CardSliderAdapter<HomeFrontSliderAdapter.SliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_slider_item, parent, false)
        return SliderViewHolder(view)
    }

    override fun bindVH(holder: SliderViewHolder, position: Int) {
        val obj = movies[position]
        holder.image.adjustViewBounds = true
//        if (select_pos!=viewpager.currentItem){
//            holder.itemView.scaleY = 0.5f
//        }else{
//            holder.itemView.scaleY = 1f
//        }

        if (obj.mobimgsmall.isEmpty()) {
            holder.image.setImageResource(R.drawable.pos_not_avilbale)
        } else {
            Picasso.get().load(obj.mobimgsmall).placeholder(R.drawable.pos_not_avilbale).into(holder.image)
        }

        holder.image.setOnClickListener {
            val intent = Intent(mContext, ShowTimesActivity::class.java)
            intent.putExtra(Constant.IntentKey.MOVIE_ID,obj.id)
            mContext.startActivity(intent)
        }

    }

     class SliderViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var image = itemView.image

    }

    override fun getItemCount(): Int {
        return movies.size
    }
}