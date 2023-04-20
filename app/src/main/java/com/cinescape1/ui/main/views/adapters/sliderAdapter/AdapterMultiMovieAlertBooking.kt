package com.cinescape1.ui.main.views.adapters.sliderAdapter

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Color
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.NextBookingResponse
import com.cinescape1.databinding.ItemBookingBinding
import com.cinescape1.utils.Constant
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.account_booking_history_item.*
import kotlinx.android.synthetic.main.alert_booking.view.*
import java.security.MessageDigest

class AdapterMultiMovieAlertBooking(
    private var context: Activity,
    private var sliderMultiMovieItemList: ArrayList<NextBookingResponse.Current>,
    var listener: RecycleViewItemClickListener
) :
    RecyclerView.Adapter<AdapterMultiMovieAlertBooking.MyViewHolderMultiMovieAlertBooking>() {
    companion object {
        var height = 0
        var width = 0
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolderMultiMovieAlertBooking {
        val binding: ItemBookingBinding = ItemBookingBinding.inflate(context.layoutInflater,parent,false)
//            LayoutInflater.from(parent.context).inflate(R.layout.item_booking, parent, false)
        return MyViewHolderMultiMovieAlertBooking(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolderMultiMovieAlertBooking, position: Int) {
        val showtimeListItem = sliderMultiMovieItemList[position]

        println("imageUrl---->${showtimeListItem.posterhori}")

        try {
            Glide.with(context)
                .load(showtimeListItem.posterhori)
                .transform(CutOffLogo(position))
                .placeholder(R.drawable.placeholder_movie_alert_poster)
                .into(holder.binding.imageBookingAlert)

        }catch (e:java.lang.Exception){
            if(position!=0){
                Picasso.get().load(showtimeListItem.posterhori).into(holder.imageBookingAlert);
            }
            e.printStackTrace()
        }

        when (showtimeListItem.experience) {
            "4DX" -> {
                Glide
                    .with(context)
                    .load(R.drawable.four_dx)
                    .into(holder.binding.experienceName)

            }
            "Standard" -> {
                Glide
                    .with(context)
                    .load(R.drawable.standard)
                    .into(holder.binding.experienceName)
            }
            "VIP" -> {
                Glide
                    .with(context)
                    .load(R.drawable.vip)
                    .into(holder.binding.experienceName)
            }
            "IMAX" -> {
                Glide
                    .with(context)
                    .load(R.drawable.imax)
                    .into(holder.binding.experienceName)
            }
            "3D" -> {
                Glide
                    .with(context)
                    .load(R.drawable.threed_black)
                    .into(holder.binding.experienceName)
            }
            "DOLBY" -> {
                Glide
                    .with(context)
                    .load(R.drawable.dolby_black)
                    .into(holder.binding.experienceName)
            }
            "ELEVEN" -> {
                Glide
                    .with(context)
                    .load(R.drawable.eleven_black)
                    .into(holder.binding.experienceName)
            }
            "SCREENX" -> {
                Glide
                    .with(context)
                    .load(R.drawable.screenx_black)
                    .into(holder.binding.experienceName)
            }
            "PREMIUM" -> {
                Glide
                    .with(context)
                    .load(R.drawable.premium_black)
                    .into(holder.binding.experienceName)
            }
        }
        holder?.apply {
            binding?.apply {
                textBombshell.isSelected = true
                textBombshell.text = showtimeListItem.moviename
                textLocationName.text = showtimeListItem.cinemaname
                textScreenNumber.text = showtimeListItem.screenId.toString()
                textDateVisible.text = showtimeListItem.showDate
                textTimeVisible.text = showtimeListItem.showTime
                rating.text = showtimeListItem.mcensor
            }
        }

        val ratingColor = showtimeListItem.ratingColor

        try {
            holder.rating.setBackgroundColor(Color.parseColor(ratingColor))
        } catch (e: Exception) {
            e.printStackTrace()
        }




        holder.goToBookingBtn1.text = context.getString(R.string.go_to_bookings)
        holder.goToBookingBtn1.setOnClickListener {
            Constant.IntentKey.BACKFinlTicket += 1
            listener.onDateClick(showtimeListItem)
        }

        holder.imageBookingAlert.setOnClickListener {
            listener.onItemClick(showtimeListItem)

        }
    }

    override fun getItemCount(): Int {
        return if (sliderMultiMovieItemList.isNotEmpty()) sliderMultiMovieItemList.size else 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun renewItems(sliderItems: ArrayList<NextBookingResponse.Current>) {
        this.sliderMultiMovieItemList = sliderItems
        notifyDataSetChanged()
    }

    class MyViewHolderMultiMovieAlertBooking(binding: ItemBookingBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding=binding
        var rating: TextView = binding.text13
        var imageBookingAlert = binding.imageBookingAlert
        var ratingUi: CardView = binding.ratingUi
        var goToBookingBtn1 = binding.goToBookingBtn1
    }

    interface RecycleViewItemClickListener {
        fun onDateClick(showtimeListItem: NextBookingResponse.Current)
        fun onItemClick(showtimeListItem: NextBookingResponse.Current)
    }
    class CutOffLogo(val position: Int) : BitmapTransformation() {
        override fun updateDiskCacheKey(messageDigest: MessageDigest) {

        }

        override fun transform(
            pool: BitmapPool, toTransform: Bitmap,
            outWidth: Int, outHeight: Int
        ): Bitmap {
//            (amount / 100.0f) * 10;
            if (position==0){
                height = (toTransform.height * 70) / 100
                width = toTransform.width
            }
            println("toTransform.height--->${toTransform.width}----$width")

            return Bitmap.createBitmap(
                toTransform,
                0,
                0,
                width,
                height
            )
        }
    }
}

