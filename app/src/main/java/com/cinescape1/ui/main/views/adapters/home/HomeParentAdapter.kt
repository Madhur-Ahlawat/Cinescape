package com.cinescape1.ui.main.views.adapters.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.HomeDataResponse
import com.cinescape1.ui.main.views.home.fragments.home.seeAll.SeeAllActivity
import com.cinescape1.ui.main.views.adapters.*
import com.cinescape1.ui.main.views.adapters.sliderAdapter.HomeFrontSliderAdapter
import com.cinescape1.ui.main.views.home.fragments.home.adapter.OfferAdapter
import com.cinescape1.utils.Constant
import com.cinescape1.utils.Constant.Companion.SEE_ALL_TYPE
import com.cinescape1.utils.hide
import com.cinescape1.utils.show
import com.github.islamkhsh.viewpager2.ViewPager2
import kotlinx.android.synthetic.main.home_parrent_list.view.*
import java.util.*
import kotlin.math.abs


class HomeParentAdapter(
    var mContext: Activity,
    var homeDataList: ArrayList<HomeDataResponse.HomeOne>,var listener:RecycleViewItemClickListener
) :
    RecyclerView.Adapter<HomeParentAdapter.MyViewHolder>() {
    var adapter: HomeChildAdapter? = null
    var currentPage = 0
    var timer: Timer? = null
    val DELAY_MS: Long = 5000 //delay in milliseconds before task is to be executed
    val PERIOD_MS: Long = 5000 // time in milliseconds between successive task executions.

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.home_parrent_list, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val obj = homeDataList[position]
        holder.home_title.text = obj.name
        holder.txtSeeAll.text = mContext.getText(R.string.view_all)
        holder.search?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                params.addRule(RelativeLayout.ALIGN_PARENT_END)
                holder.search?.layoutParams = params
            } else {
                val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                params.addRule(RelativeLayout.ALIGN_PARENT_END)
                holder.search?.layoutParams = params
            }
        }
        var movieDataList = ArrayList<HomeDataResponse.MovieData>()
        movieDataList = obj.movieData

        when (obj.key) {
            "slider" -> {
                print("TypeKey--->${obj.key}")
                holder.home_title.hide()
                holder.txtSeeAll.hide()
                holder.homeList.show()
                holder.viewpagerBack.show()
                holder.viewpager.show()
                holder.search.show()
                holder.viewpagerBack.layoutDirection = View.LAYOUT_DIRECTION_RTL
                val sliderbackAdapter = SliderBackAdapter(mContext, obj.movieData)
                holder.viewpagerBack.adapter = sliderbackAdapter
                holder.viewpagerBack.setPagingEnabled(false)

                holder.viewpager.adapter = HomeFrontSliderAdapter(mContext, obj.movieData,holder.viewpager)
                holder.viewpager.offscreenPageLimit = 3
                holder.viewpager.clipChildren = false
                holder.viewpager.clipToPadding = false
                holder.viewpager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
                val transfer = CompositePageTransformer()
                transfer.addTransformer(MarginPageTransformer(70))


                val nextItemVisiblePx = mContext.resources.getDimension(R.dimen.viewpager_next_item_visible)
                val currentItemHorizontalMarginPx = mContext.resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
                val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx

                transfer.addTransformer(object : ViewPager2.PageTransformer,
                    androidx.viewpager2.widget.ViewPager2.PageTransformer {
                    override fun transformPage(page: View, position: Float) {
                        val r = 1- abs(position)
                        page.translationX = -pageTranslationX * position
                        page.scaleY = 1 - (0.35f * abs(position))
//                        if ((0.85f+ r*0.14f)>90.0) {
//                            page.scaleY = (0.85f + r * 0.14f)
//                        }else{
//                            page.scaleY=   (0.59f)
//                        }
                        println("otherPage--->${(0.85f+ r*0.14f)}--->position${position}--->R${r}")
                    }

                })
                holder.viewpager.setPageTransformer(transfer)
              val  NUM_PAGES=obj.movieData.size
                val handler = Handler()
                val Update = Runnable {
                    if (currentPage == NUM_PAGES) {
                        currentPage = 0
                    }
                    holder.viewpager.setCurrentItem(currentPage++, true)
                }

                holder.viewpager.registerOnPageChangeCallback(object : androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {

                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        Log.e("Selected_Page", position.toString())
                        Constant.select_pos = position;
                        holder.viewpagerBack.currentItem = position

                    }

                    override fun onPageScrollStateChanged(state: Int) {
                        super.onPageScrollStateChanged(state)
                    }

                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {
                        super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                        println("positionOffset3--->${holder.viewpager?.getChildAt(position)?.scrollX}---->$position---->${positionOffsetPixels}")


                    }

                })


            }
            "advance" -> {
                holder.home_title.show()
                holder.txtSeeAll.hide()
                holder.consAdvance.show()
                holder.sliderAdvance.show()
                holder.viewpagerBack.hide()
                holder.homeList.hide()
                holder.viewpager.hide()
                val advanceSliderAdapter = AdvanceSliderAdapter(mContext, obj.movieData)
                holder.sliderAdvance.adapter = advanceSliderAdapter
                holder.mytablayout.setupWithViewPager(holder.sliderAdvance, true)
                // The_slide_timer
                /*After setting the adapter use the timer */
                val pagerLength = obj.movieData.size
                val handler = Handler()
                val Update = Runnable {
                    if (currentPage == pagerLength) {
                        currentPage = 0
                    }
                    holder.sliderAdvance.setCurrentItem(currentPage++, true)
                }
                timer = Timer()
                timer!!.schedule(object : TimerTask() {
                    override fun run() {
                        handler.post(Update)
                    }
                }, DELAY_MS, PERIOD_MS)
            }
            "homeOnes" -> {
                val gridLayout = GridLayoutManager(mContext, 1, GridLayoutManager.HORIZONTAL, false)
                holder.homeList?.layoutManager = LinearLayoutManager(mContext)
                adapter = HomeChildAdapter(mContext, movieDataList, 1, true)
                holder.homeList?.layoutManager = gridLayout
                holder.homeList?.adapter = adapter
            }
            "offers" -> {
                holder.itemView.show()
                holder.home_title.show()
                holder.homeList.show()
                holder.viewpager.hide()
                holder.viewpagerBack.hide()
                holder.itemView.show()
                val gridLayout = GridLayoutManager(mContext, 1, GridLayoutManager.HORIZONTAL, false)
                holder.homeList?.layoutManager = LinearLayoutManager(mContext)
                val adapter = OfferAdapter(mContext, obj.offers)
                holder.homeList?.layoutManager = gridLayout
                holder.homeList?.adapter = adapter
                holder.txtSeeAll.setOnClickListener {
                    val intent = Intent(mContext, SeeAllActivity::class.java)
                    intent.putExtra("type", "offer")
                    intent.putExtra("arrayList", obj.movieData)
                    mContext.startActivity(intent)
                }
            }
            "cinemas" -> {
                holder.itemView.show()
                holder.home_title.show()
                holder.homeList.show()
                holder.viewpager.hide()
                holder.viewpagerBack.hide()
                holder.itemView.show()
                holder.txtSeeAll.hide()

                val gridLayout = GridLayoutManager(mContext, 1, GridLayoutManager.HORIZONTAL, false)
                holder.homeList?.layoutManager = LinearLayoutManager(mContext)
                val adapter = HomeMovieAdapter(mContext, obj.cinemas)
                holder.homeList?.layoutManager = gridLayout
                holder.homeList?.adapter = adapter
            }
            "comingSoon" -> {
                if (!obj.movieData.isNullOrEmpty()) {
                    holder.itemView.show()
                    holder.home_title.show()
                    holder.homeList.show()
                    holder.viewpager.hide()
                    holder.viewpagerBack.hide()
                    holder.itemView.show()
                    holder.txtSeeAll.show()

                    val gridLayout =
                        GridLayoutManager(mContext, 1, GridLayoutManager.HORIZONTAL, false)
                    holder.homeList?.layoutManager = LinearLayoutManager(mContext)
                    adapter = HomeChildAdapter(mContext, obj.movieData, 1,true)
                    holder.homeList?.layoutManager = gridLayout
                    holder.homeList?.adapter = adapter
                    holder.txtSeeAll.setOnClickListener {
                        if (obj.name == mContext.getString(R.string.commingSoon)) {
                            SEE_ALL_TYPE = 1
                            listener.onSeeAllClick(1)
                        }else{
                            listener.onSeeAllClick(0)
                            SEE_ALL_TYPE = 0
                        }
                    }
                } else {
                    holder.itemView.hide()
                }
            }
            else -> {
                if (!obj.movieData.isNullOrEmpty()) {
                    holder.itemView.show()
                    holder.home_title.show()
                    holder.homeList.show()
                    holder.viewpager.hide()
                    holder.viewpagerBack.hide()
                    holder.itemView.show()
                    holder.txtSeeAll.show()

                    val gridLayout =
                        GridLayoutManager(mContext, 1, GridLayoutManager.HORIZONTAL, false)
                    holder.homeList?.layoutManager = LinearLayoutManager(mContext)
                    adapter = HomeChildAdapter(mContext, obj.movieData, 1, false)
                    holder.homeList?.layoutManager = gridLayout
                    holder.homeList?.adapter = adapter
                    holder.txtSeeAll.setOnClickListener {
                        if (obj.name == mContext.getString(R.string.commingSoon)) {
                            SEE_ALL_TYPE = 1
                            listener.onSeeAllClick(1)
                        }else{
                            listener.onSeeAllClick(0)
                            SEE_ALL_TYPE = 0
                        }
                    }
                } else {
                    holder.itemView.hide()
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return homeDataList.size
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var home_title = itemView.home_title
        var txtSeeAll = itemView.textView4
        var homeList = itemView.homeList
        var viewpager = itemView.viewpager
        var search = itemView.search
        var viewpagerBack = itemView.viewpagerBack
        var consAdvance = itemView.consAdvance
        var sliderAdvance = itemView.sliderAdvance
        var mytablayout = itemView.my_tablayout
    }

    interface RecycleViewItemClickListener {
        fun onSeeAllClick(type:Int)
    }
}