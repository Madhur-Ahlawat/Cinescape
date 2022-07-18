package com.cinescape1.ui.main.views.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.HomeDataResponse
import com.cinescape1.data.models.responseModel.MoviesResponse
import com.cinescape1.ui.main.views.ShowTimesActivity
import com.cinescape1.utils.Constant

class HomeMovieAdapter(private  val mContext: Activity, private  val cinemas: ArrayList<HomeDataResponse.Cinema>) :
    RecyclerView.Adapter<HomeMovieAdapter.TodoViewHolder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cinema_layout, null)
        return TodoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cinemas.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val obj = cinemas[position]
        print("offerData123--->${cinemas}")
        holder.title.text=obj.name
        Glide.with(mContext)
            .load(obj.icon)
            .error(R.drawable.cinema)
            .into(holder.todoImage)

        holder.todoImage.setOnClickListener {
            val intent = Intent(mContext, ShowTimesActivity::class.java)
            intent.putExtra(Constant.IntentKey.MOVIE_ID, obj.id)
            intent.putExtra("type","movie")
            mContext.startActivity(intent)
        }
    }

    inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var todoImage: ImageView = view.findViewById(R.id.image_recommended) as ImageView
        var title: TextView = view.findViewById(R.id.textView58) as TextView
    }

    interface RecycleViewItemClickListener {
        fun onItemClick(view: MoviesResponse.Output, title: String)
    }
}