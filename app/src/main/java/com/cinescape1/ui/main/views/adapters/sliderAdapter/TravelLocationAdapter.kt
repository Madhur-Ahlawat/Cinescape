package com.cinescape1.ui.main.views.adapters.sliderAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.cinescape1.R
import com.cinescape1.data.models.ModelSliderHome
import com.makeramen.roundedimageview.RoundedImageView
import kotlinx.android.synthetic.main.slider_item_container.view.*



class TravelLocationAdapter (context: Context, private var comingSoonList: List<ModelSliderHome>, viewPager2: ViewPager2) :
    RecyclerView.Adapter<TravelLocationAdapter.MyViewHolderTravelLocation>() {
    private var mcontext: Context = context
    private var viewPager = viewPager2

    private lateinit var runnable : Runnable


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderTravelLocation {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.slider_item_container, parent, false)
        return MyViewHolderTravelLocation(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderTravelLocation, position: Int) {

        if (comingSoonList.isEmpty()) {
//            holder.title.setText(R.string.empty_photo)
        } else {
            val comingSoonItem = comingSoonList[position]
            holder.imageSlide.setImageResource(comingSoonItem.imgUrl)

            if (position == comingSoonList.size - 2){
                viewPager.post(runnable)
            }


        }
    }

    override fun getItemCount(): Int {
        return if (comingSoonList.isNotEmpty()) comingSoonList.size else 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadNewData(newPhotoslist: List<ModelSliderHome>) {
          runnable = Runnable {
             comingSoonList = newPhotoslist
        }
        notifyDataSetChanged()
    }

    class MyViewHolderTravelLocation(view: View) : RecyclerView.ViewHolder(view) {
        //    var kenImage: KenBurnsView = view.findViewById(R.id.ken_image)
        var imageSlide: RoundedImageView = view.imageSlide

    }


}