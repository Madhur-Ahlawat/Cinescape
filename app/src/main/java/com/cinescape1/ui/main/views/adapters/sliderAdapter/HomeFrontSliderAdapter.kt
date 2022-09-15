package com.cinescape1.ui.main.views.adapters.sliderAdapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.HomeDataResponse
import com.cinescape1.ui.main.views.details.ShowTimesActivity
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

//    override fun getItemCount() = movies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_slider_item, parent, false)
        return SliderViewHolder(view)
    }

    override fun bindVH(holder: SliderViewHolder, position: Int) {
        //TODO bind item object with item layout view
        val obj = movies[position]
//        holder.image.layoutParams = ViewGroup.MarginLayoutParams(
//            ViewGroup.LayoutParams.WRAP_CONTENT,
//            ViewGroup.LayoutParams.MATCH_PARENT
//        )
        holder.image.adjustViewBounds = true
        println("viewpager.currentItem---"+viewpager.currentItem+"----"+select_pos)
        if (select_pos!=viewpager.currentItem){
            holder.itemView.scaleY = 0.5f
        }else{
            holder.itemView.scaleY = 1f
        }
        if (obj.mobimgsmall.isEmpty()) {
            holder.image.setImageResource(R.drawable.pos_not_avilbale)
        } else {
//            holder.image.setImageResource(R.drawable.test1)

            Picasso.get().load(obj.mobimgsmall).placeholder(R.drawable.pos_not_avilbale).into(holder.image)

//            Glide.with(mContext)
//                .load(obj.mobimgsmall)
//                .placeholder(R.drawable.pos_not_avilbale).override(Constant().convertDpToPixel(257f,mContext),Constant().convertDpToPixel(380f,mContext))
//                .into(holder.image)
            println("FrontData--->${obj.mobimgsmall}")
        }

        holder.image.setOnClickListener {
            val intent = Intent(mContext, ShowTimesActivity::class.java)
            intent.putExtra(Constant.IntentKey.MOVIE_ID,obj.id)
//            intent.putExtra("type","movie")
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