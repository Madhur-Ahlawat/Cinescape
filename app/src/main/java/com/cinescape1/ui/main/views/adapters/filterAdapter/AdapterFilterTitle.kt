package com.cinescape1.ui.main.views.adapters.filterAdapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.FilterModel
import com.cinescape1.data.models.responseModel.FilterTypeModel
import com.cinescape1.ui.main.views.adapters.FilterChildAdapter
import com.cinescape1.ui.main.views.adapters.FilterChildCinemaAdapter
import com.cinescape1.ui.main.views.adapters.FilterChildMovieAdapter
import com.cinescape1.ui.main.views.home.fragments.movie.adapter.FilterExperiencesAdapter
import com.cinescape1.utils.hide
import com.cinescape1.utils.show
import com.google.android.flexbox.*

class AdapterFilterTitle(
    context: Activity,
    private var filterTitleList: ArrayList<FilterModel>,
    private val dataList: ArrayList<FilterTypeModel>
) :
    RecyclerView.Adapter<AdapterFilterTitle.MyViewHolderFilterTitle>() {
    private var up = true
    private val mContext: Activity = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderFilterTitle {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.filter_alert_title_item, parent, false)
        return MyViewHolderFilterTitle(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(
        holder: MyViewHolderFilterTitle,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val filterExpItem = filterTitleList[position]
        holder.textTitleFilter.text = filterExpItem.title

        holder.itemView.setOnClickListener {
            try {
                when (filterExpItem.type) {
                    1 -> {
                        println("ExperienceClickAdapter1---------->Yes")
                        if (holder.filterExpand.visibility == View.GONE) {
                            holder.viewSpace.show()
                            holder.filterExpand.show()
                            holder.filterExpand.visibility = View.VISIBLE
                            holder.filterExpand.removeAllViews()
                            if (getList(filterExpItem).size > 0) {
                                holder.selectFilter.show()
                            } else {
                                holder.selectFilter.hide()
                            }
                            holder.imageArrowDrop.setImageResource(R.drawable.arrow_up)
                            val adapter = FilterExperiencesAdapter(mContext, filterExpItem.dataList, getList(filterExpItem))
                            holder.filterExpand.adapter = adapter

                            val layoutManager = FlexboxLayoutManager(mContext)
                            layoutManager.flexDirection = FlexDirection.ROW
                            layoutManager.flexWrap = FlexWrap.WRAP
                            layoutManager.alignItems = AlignItems.CENTER
                            layoutManager.justifyContent = JustifyContent.SPACE_EVENLY
                            holder.filterExpand.layoutManager = layoutManager

                        } else {
                            holder.imageArrowDrop.setImageResource(R.drawable.arrow_down)
                            holder.filterExpand.hide()
                            holder.viewSpace.hide()
                            holder.filterExpand.hide()
                            holder.selectFilter.hide()
                        }

                    }
                    2 -> {
                        if (holder.filterExpand.visibility == View.GONE) {
                            holder.filterExpand.visibility = View.VISIBLE
                            holder.filterExpand.removeAllViews()
                            if (getList(filterExpItem).size > 0) {
                                holder.selectFilter.show()
                            } else {
                                holder.selectFilter.hide()
                            }
                            val adapter = FilterChildCinemaAdapter(
                                mContext,
                                filterExpItem.cinemaList!!, getList(filterExpItem)
                            )
                            holder.imageArrowDrop.setImageResource(R.drawable.arrow_up)

                            holder.filterExpand.adapter = adapter
                            val layoutManager = FlexboxLayoutManager(mContext)
                            layoutManager.flexDirection = FlexDirection.ROW
                            layoutManager.flexWrap = FlexWrap.WRAP
                            layoutManager.alignItems = AlignItems.FLEX_START
                            layoutManager.justifyContent = JustifyContent.FLEX_START
                            holder.filterExpand.layoutManager = layoutManager

                            println("ExperienceClickAdapter2---------->Yes")

                        } else {
                            holder.imageArrowDrop.setImageResource(R.drawable.arrow_down)
                            holder.filterExpand.hide()
                            holder.viewSpace.hide()
                            holder.filterExpand.hide()
                            holder.selectFilter.hide()

                        }
                    }
                    3 -> {
                        println("ExperienceClickAdapter3---------->Yes")
                        if (
                            holder.filterExpand.visibility == View.GONE){
                            up = false
                            holder.viewSpace.show()
                            holder.filterExpand.show()
                            holder.imageArrowDrop.setImageResource(R.drawable.arrow_up)
                            holder.filterExpand.visibility = View.VISIBLE
                            holder.filterExpand.removeAllViews()
                            if (getList(filterExpItem).size > 0) {
                                holder.selectFilter.show()
                            } else {
                                holder.selectFilter.hide()
                            }
                            val adapter = FilterChildMovieAdapter(
                                mContext,
                                filterExpItem.movieTimings!!, getList(filterExpItem)
                            )
                            holder.filterExpand.adapter = adapter
                            val layoutManager = FlexboxLayoutManager(mContext)
                            layoutManager.flexDirection = FlexDirection.ROW
                            layoutManager.flexWrap = FlexWrap.WRAP
                            layoutManager.alignItems = AlignItems.FLEX_START
                            layoutManager.justifyContent = JustifyContent.FLEX_START
                            holder.filterExpand.layoutManager = layoutManager


                        }else{
                            holder.imageArrowDrop.setImageResource(R.drawable.arrow_down)
                            holder.viewSpace.hide()
                            holder.filterExpand.hide()
                            holder.selectFilter.hide()

                        }
                    }
                    4 -> {
                        if (holder.filterExpand.visibility == View.GONE) {
                            holder.imageArrowDrop.setImageResource(R.drawable.arrow_up)
                            holder.viewSpace.show()
                            holder.filterExpand.show()
                            holder.filterExpand.visibility = View.VISIBLE
                            holder.filterExpand.removeAllViews()
                            println("checkList--->${getList(filterExpItem)}")
                            if (getList(filterExpItem).size > 0) {
                                holder.selectFilter.show()
                            } else {
                                holder.selectFilter.hide()
                            }
                            val adapter = FilterChildAdapter(
                                mContext,
                                filterExpItem.dataList, getList(filterExpItem)
                            )
                            holder.filterExpand.adapter = adapter
                            val layoutManager = FlexboxLayoutManager(mContext)
                            layoutManager.flexDirection = FlexDirection.ROW
                            layoutManager.flexWrap = FlexWrap.WRAP
                            layoutManager.alignItems = AlignItems.FLEX_START
                            layoutManager.justifyContent = JustifyContent.FLEX_START
                            holder.filterExpand.layoutManager = layoutManager
                        } else {
                            up = false
                            holder.imageArrowDrop.setImageResource(R.drawable.arrow_down)
                            holder.viewSpace.hide()
                            holder.filterExpand.hide()
                            holder.selectFilter.hide()
                        }
                    }
                    5 -> {
                        if (holder.filterExpand.visibility == View.GONE) {
                            holder.imageArrowDrop.setImageResource(R.drawable.arrow_up)
                            holder.viewSpace.show()
                            holder.filterExpand.show()
                            holder.filterExpand.visibility = View.VISIBLE
                            holder.filterExpand.removeAllViews()
                            println("checkList--->${getList(filterExpItem)}")
                            if (getList(filterExpItem).size > 0) {
                                holder.selectFilter.show()
                            } else {
                                holder.selectFilter.hide()
                            }

                            val adapter = FilterChildAdapter(
                                mContext,
                                filterExpItem.dataList, getList(filterExpItem)
                            )
                            holder.filterExpand.adapter = adapter
                            val layoutManager = FlexboxLayoutManager(mContext)
                            layoutManager.flexDirection = FlexDirection.ROW
                            layoutManager.flexWrap = FlexWrap.WRAP
                            layoutManager.alignItems = AlignItems.FLEX_START
                            layoutManager.justifyContent = JustifyContent.FLEX_START
                            holder.filterExpand.layoutManager = layoutManager

                        } else {
                            up = false
                            holder.imageArrowDrop.setImageResource(R.drawable.arrow_down)
                            holder.viewSpace.hide()
                            holder.filterExpand.hide()
                            holder.selectFilter.hide()
                        }
                    }
                    6 -> {
                        if (holder.filterExpand.visibility == View.GONE) {
                            holder.imageArrowDrop.setImageResource(R.drawable.arrow_up)
                            holder.viewSpace.show()
                            holder.filterExpand.show()
                            holder.filterExpand.visibility = View.VISIBLE
                            holder.filterExpand.removeAllViews()
                            println("checkList--->${getList(filterExpItem)}")
                            if (getList(filterExpItem).size > 0) {
                                holder.selectFilter.show()
                            } else {
                                holder.selectFilter.hide()
                            }
                            val adapter = FilterChildAdapter(mContext, filterExpItem.dataList, getList(filterExpItem))
                            holder.filterExpand.adapter = adapter
                            val layoutManager = FlexboxLayoutManager(mContext)
                            layoutManager.flexDirection = FlexDirection.ROW
                            layoutManager.flexWrap = FlexWrap.WRAP
                            layoutManager.alignItems = AlignItems.FLEX_START
                            layoutManager.justifyContent = JustifyContent.FLEX_START
                            holder.filterExpand.layoutManager = layoutManager

                        } else {
                            up = false
                            holder.imageArrowDrop.setImageResource(R.drawable.arrow_down)
                            holder.viewSpace.hide()
                            holder.filterExpand.hide()
                            holder.selectFilter.hide()
                        }
                    }
                }
            } catch (e: Exception) {
                println("Exception------>${e.message}")
            }
        }

        if (getList(filterExpItem).size > 0) {
            holder.selectFilter.show()
        } else {
            holder.selectFilter.hide()
        }


    }

    private fun getList(i: FilterModel): ArrayList<String> {
        for (data in dataList) {
            if (data.type == i.type) {
                i.selectedList = data.selectedList
                break
            }
        }
        return i.selectedList
    }

    override fun getItemCount(): Int {
        return if (filterTitleList.isNotEmpty()) filterTitleList.size else 0
    }

    class MyViewHolderFilterTitle(view: View) : RecyclerView.ViewHolder(view) {
        var textTitleFilter: TextView = view.findViewById(R.id.text_title_filter)
        var imageArrowDrop: ImageView = view.findViewById(R.id.image_arrow_up)
        var selectFilter: ImageView = view.findViewById(R.id.imageView50)
        var filterExpand: RecyclerView = view.findViewById(R.id.filter_exapands)
        var viewSpace: View = view.findViewById(R.id.viewSpace)
    }
}