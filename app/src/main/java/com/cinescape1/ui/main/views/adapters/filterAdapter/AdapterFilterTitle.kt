package com.cinescape1.ui.main.views.adapters.filterAdapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.FilterModel
import com.cinescape1.data.models.responseModel.FilterTypeModel
import com.cinescape1.ui.main.views.adapters.FilterChildAdapter
import com.cinescape1.ui.main.views.adapters.FilterChildCinemaAdapter
import com.cinescape1.ui.main.views.adapters.FilterChildMovieAdapter
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
    private var rowIndex = -1

    private val mContext: Context = context
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
        try {
            when (filterExpItem.type) {
                1 -> {
                    up = false
                    holder.filterExpand.visibility = View.VISIBLE
                    holder.filterExpand.removeAllViews()
                    if (getList(filterExpItem).size>0){
                        holder.selectFilter.show()
                    }else{
                        holder.selectFilter.hide()
                    }
                    val adapter = FilterChildAdapter(
                        mContext,
                        filterExpItem.dataList, getList(filterExpItem)
                    )
                    println("checkList--->${getList(filterExpItem)}")
                    holder.filterExpand.adapter = adapter
                    val layoutManager = FlexboxLayoutManager(mContext)
                    layoutManager.flexDirection = FlexDirection.ROW
                    layoutManager.flexWrap = FlexWrap.WRAP
                    layoutManager.alignItems = AlignItems.FLEX_START
                    layoutManager.justifyContent = JustifyContent.FLEX_START
                    holder.filterExpand.layoutManager = layoutManager
                }
                4 -> {
                    up = false
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
                }
                5 -> {
                    up = false
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
                }
                6 -> {
                    up = false
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
                }
                2 -> {
                    up = false
                    holder.filterExpand.visibility = View.VISIBLE
                    holder.filterExpand.removeAllViews()
                    println("checkList--->${getList(filterExpItem)}")
                    if (getList(filterExpItem).size > 0) {
                        holder.selectFilter.show()
                    } else {
                        holder.selectFilter.hide()
                    }
                    val adapter = FilterChildCinemaAdapter(
                        mContext,
                        filterExpItem.cinemaList!!, getList(filterExpItem)
                    )
                    holder.filterExpand.adapter = adapter
                    val layoutManager = FlexboxLayoutManager(mContext)
                    layoutManager.flexDirection = FlexDirection.ROW
                    layoutManager.flexWrap = FlexWrap.WRAP
                    layoutManager.alignItems = AlignItems.FLEX_START
                    layoutManager.justifyContent = JustifyContent.FLEX_START
                    holder.filterExpand.layoutManager = layoutManager
                }
                3 -> {
                    up = false
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
                }
            }
        } catch (e: Exception) {
            println("Exception------>${e.message}")
        }

        holder.itemView.setOnClickListener {
            rowIndex = position
            notifyDataSetChanged()
        }

        if (rowIndex == position) {
            holder.imageArrowDrop.setImageResource(R.drawable.arrow_down)
            holder.viewSpace.show()
            holder.filterExpand.show()
        } else {
            holder.imageArrowDrop.setImageResource(R.drawable.arrow_up)
            holder.viewSpace.hide()
            holder.filterExpand.hide()
        }
    }

    private fun getList(i: FilterModel): ArrayList<String> {
        for (data in dataList) {
            if (data.type == i.type) {
                i.selectedList = data.selectedList

                println("data.selectedList12345---" + i.selectedList + "----")
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