package com.cinescape1.ui.main.views.home.fragments.home.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_IDLE
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.HomeDataResponse
import com.cinescape1.ui.main.views.adapters.*
import com.cinescape1.ui.main.views.adapters.sliderAdapter.HomeFrontSliderAdapter
import com.cinescape1.ui.main.views.home.fragments.home.seeAll.SeeAllActivity
import com.cinescape1.ui.main.views.home.fragments.movie.MoviesFragment
import com.cinescape1.utils.*
import com.cinescape1.utils.Constant.Companion.SEE_ALL_TYPE
import com.github.islamkhsh.viewpager2.ViewPager2
import kotlinx.android.synthetic.main.home_parrent_list.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

class HomeParentAdapter(
    private var mContext: Activity,
    private var homeDataList: ArrayList<HomeDataResponse.HomeOne>,
    var listener: RecycleViewItemClickListener,
    var listenerTypeface: TypeFaceInter
) : RecyclerView.Adapter<HomeParentAdapter.MyViewHolder>(), HomeChildAdapter.ImageChangeIcon {
    var adapter: HomeChildAdapter? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_parrent_list, parent, false)
        return MyViewHolder(view)
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val obj = homeDataList[position]
        holder.homeTitle.text = obj.name
        holder.txtSeeAll.text = mContext.getText(R.string.view_all)
        val movieDataList: ArrayList<HomeDataResponse.MovieData> = obj.movieData
        listenerTypeface.typeFace(holder.homeTitle, holder.txtSeeAll)

        when (obj.key) {
            "slider" -> {
                holder.homeTitle.hide()
                holder.txtSeeAll.hide()
                holder.homeList.hide()
                holder.viewpagerBack.show()
                holder.viewpager.show()
                holder.consAdvance.hide()

                if (Constant.LANGUAGE == "ar") {
                    LocaleHelper.setLocale(mContext, "ar")
                } else {
                    LocaleHelper.setLocale(mContext, "en")
                }

                val pagerAdapter = HomeFrontSliderAdapter(mContext, obj.movieData, holder.viewpager)
                holder.viewpager.adapter = pagerAdapter
//                holder.viewpager.currentItem=1
//                onInfinitePageChangeCallback(obj.movieData.size + 2, holder, obj.movieData)
                if(obj.movieData.size>0){
                    holder.viewpager.setCurrentItem(1,false)
                    HelperUtils.setGradient(holder.viewpager,obj.movieData[1].sliderimgurl)
                }

                holder.viewpager.registerOnPageChangeCallback(object:
                    androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
                    var mPosition:Int=-1
                    override fun onPageSelected(position: Int) {
                        mPosition=position
                        super.onPageSelected(position)
                    }

                    override fun onPageScrollStateChanged(state: Int) {
                        if(state==SCROLL_STATE_IDLE){
                            HelperUtils.setGradient(holder.viewpager,obj.movieData[mPosition].sliderimgurl)
                            if(obj.movieData[mPosition].isFakeFirstElement){
                                holder.viewpager.setCurrentItem(1,false)
                            }
                            if(obj.movieData[mPosition].isFakeLastElement){
                                holder.viewpager.setCurrentItem(obj.movieData.size-2,false)
                            }
                        }
                        super.onPageScrollStateChanged(state)
                    }
                })
                holder.viewpager.offscreenPageLimit = 3
                holder.viewpager.clipChildren = false
                holder.viewpager.clipToPadding = false
                holder.viewpager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
                val transfer = CompositePageTransformer()
                val nextItemVisiblePx =
                    mContext.resources.getDimension(R.dimen.viewpager_next_item_visible)
                val currentItemHorizontalMarginPx =
                    mContext.resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)

                val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
                transfer.addTransformer(object : ViewPager2.PageTransformer,
                    androidx.viewpager2.widget.ViewPager2.PageTransformer {
                    override fun transformPage(page: View, position: Float) {
                        val r = 1 - abs(position)
                        page.translationX = -pageTranslationX * position
                        page.scaleY = 1 - (0.35f * abs(position))

                        println("FrontSliderPositionY---------->${page.scaleY}-----positionX--->${page.translationX}")
                    }
                })
                holder.viewpager.setPageTransformer(transfer)

            }
            "advance" -> {
                holder.homeTitle.show()
                holder.txtSeeAll.hide()
                holder.consAdvance.show()
                holder.sliderAdvance.show()
                holder.homeList.hide()
                holder.viewpager.hide()
                holder.viewpagerBack.hide()
                val advanceSliderAdapter = AdvanceSliderAdapter(mContext, obj.movieData)
                holder.sliderAdvance.adapter = advanceSliderAdapter
                holder.myTabLayout.setupWithViewPager(holder.sliderAdvance, true)
            }
            "homeOnes" -> {
                holder.viewpager.hide()
                holder.viewpagerBack.hide()
                holder.homeList.isLayoutFrozen = false
                val gridLayout = GridLayoutManager(mContext, 1, GridLayoutManager.HORIZONTAL, false)
                holder.homeList.layoutManager = LinearLayoutManager(mContext)
                adapter = HomeChildAdapter(mContext, movieDataList, 1, true, this)
                holder.homeList.layoutManager = gridLayout
                holder.homeList.adapter = adapter
            }
            "offers" -> {
                try {
                    holder.txtSeeAll.show()
                    holder.itemView.show()
                    holder.homeTitle.show()
                    holder.homeList.show()
                    holder.viewpager.hide()
                    holder.viewpager.hide()
                    holder.viewpagerBack.hide()
                    holder.homeList.isLayoutFrozen = false
                    val gridLayout = GridLayoutManager(mContext, 1, GridLayoutManager.HORIZONTAL, false)
                    holder.homeList.layoutManager = LinearLayoutManager(mContext)
                    val adapter = OfferAdapter(mContext, obj.offers)
                    holder.homeList.layoutManager = gridLayout
                    holder.homeList.adapter = adapter

                    holder.txtSeeAll.setOnClickListener {
                        val intent = Intent(mContext, SeeAllActivity::class.java)
                        intent.putExtra("type", "offer")
                        intent.putExtra("arrayList", obj.movieData)
                        mContext.startActivity(intent)
                    }

                } catch (e: Exception) {
                    println("errorOffer--->${e.message}")
                }

            }
            "cinemas" -> {
                holder.itemView.show()
                holder.homeTitle.show()
                holder.homeList.show()
                holder.viewpager.hide()
                holder.homeList.isLayoutFrozen = false
                holder.itemView.show()
                holder.txtSeeAll.hide()

                holder.viewpager.hide()
                holder.viewpagerBack.hide()
                val gridLayout = GridLayoutManager(mContext, 1, GridLayoutManager.HORIZONTAL, false)
                holder.homeList.layoutManager = LinearLayoutManager(mContext)
                val adapter = HomeMovieAdapter(mContext, obj.cinemas)
                holder.homeList.layoutManager = gridLayout
                holder.homeList.adapter = adapter
            }
            "comingSoon" -> {
                if (obj.movieData.isNotEmpty()) {
                    holder.itemView.show()
                    holder.homeTitle.show()
                    holder.homeList.show()

                    holder.viewpager.hide()
                    holder.viewpagerBack.hide()
                    holder.homeList.isLayoutFrozen = false
                    holder.itemView.show()
                    holder.txtSeeAll.show()

                    val gridLayout = GridLayoutManager(mContext, 1, GridLayoutManager.HORIZONTAL, false)
                    holder.homeList.layoutManager = LinearLayoutManager(mContext)
                    adapter = HomeChildAdapter(mContext, obj.movieData, 1, true, this)
                    holder.homeList.layoutManager = gridLayout
                    holder.homeList.adapter = adapter
                    holder.txtSeeAll.setOnClickListener {
                        if (obj.name == mContext.getString(R.string.commingSoon)) {
                            Constant.SEE_ALL_TYPES = 1
                            listener.onSeeAllClick(1)
                            MoviesFragment.positionState = 1
                            SEE_ALL_TYPE = 1
                        } else {
                            listener.onSeeAllClick(0)
                            MoviesFragment.positionState = 0
                            SEE_ALL_TYPE = 0
                        }
                    }
                } else {
                    holder.itemView.hide()
                }
            }
            else -> {
                if (obj.movieData.isNotEmpty()) {
                    holder.itemView.show()
                    holder.homeTitle.show()
                    holder.homeList.show()

                    holder.viewpager.hide()
                    holder.viewpagerBack.hide()
                    holder.homeList.isLayoutFrozen = false
                    holder.itemView.show()
                    holder.txtSeeAll.hide()

                    val gridLayout = GridLayoutManager(mContext, 1, GridLayoutManager.HORIZONTAL, false)
                    holder.homeList.layoutManager = LinearLayoutManager(mContext)
                    adapter = HomeChildAdapter(mContext, obj.movieData, 1, false, this)
                    holder.homeList.layoutManager = gridLayout
                    holder.homeList.adapter = adapter
                    holder.txtSeeAll.setOnClickListener {
                        if (obj.name == mContext.getString(R.string.commingSoon)) {
                            SEE_ALL_TYPE = 1
                            listener.onSeeAllClick(1)
                            MoviesFragment.positionState = 1
                        } else {
                            listener.onSeeAllClick(0)
                            MoviesFragment.positionState = 0
                            SEE_ALL_TYPE = 0
                        }
                    }
                } else {
                    holder.itemView.hide()
                }
            }
        }
    }

    var index = 0
    private fun onInfinitePageChangeCallback(
        listSize: Int,
        holder: MyViewHolder,
        movieData: ArrayList<HomeDataResponse.MovieData>) {
        holder.viewpager.registerOnPageChangeCallback(object : androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback(){
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)

                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    println("FrontSliderPageSelected213---------->${listSize}----->${holder.viewpager.currentItem}")

//                    if (index == 0){
//                        holder.viewpager.setCurrentItem(listSize - 2, false)
//                    }else if (index == listSize - 1){
//                        holder.viewpager.setCurrentItem(1, false)
//                    }

                    when (holder.viewpager.currentItem) {
                        listSize - 1 -> {
                            holder.viewpager.setCurrentItem(1, false)
                            println("FrontSliderPageSelected21---------->${listSize}----->${holder.viewpager.currentItem}")
                        }

                        0 ->{
                            holder.viewpager.setCurrentItem(listSize - 2, false)
                            println("FrontSliderPageSelected212---------->${listSize}----->${holder.viewpager.currentItem}")
                        }

                    }


                }

            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                        index=position
                        Constant.select_pos = position
                        val colorCode=movieData[position].sliderimgurl
                println("FrontSliderPageSelected---------->${index}-----onPageSelected--->${colorCode}")

                        try {
                            val startColor = Color.parseColor(colorCode)
                            val endColor = Color.parseColor("#000000")
                            val mDrawable = GradientDrawable(
                                GradientDrawable.Orientation.TOP_BOTTOM,
                                intArrayOf(startColor, endColor)
                            )
                            holder.viewpagerBack.setBackgroundDrawable(mDrawable)

                        }catch (e:Exception){
                            e.printStackTrace()
                        }

//                if (position != 0 && position != listSize - 1) {
//                    // pageIndicatorView.setSelected(position-1)
//                }
            }
        })

    }


    override fun getItemCount(): Int {
        return homeDataList.size
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var homeTitle = itemView.home_title!!
        var txtSeeAll = itemView.textView4!!
        var homeList = itemView.homeList!!
        var viewpager = itemView.viewpager!!
        var viewpagerBack = itemView.viewpagerBack!!
        var consAdvance = itemView.consAdvance!!
        var sliderAdvance = itemView.sliderAdvance!!
        var myTabLayout = itemView.my_tablayout!!
    }

    interface RecycleViewItemClickListener {
        fun onSeeAllClick(type: Int)
    }

    interface TypeFaceInter {
        fun typeFace(homeTitle: TextView, txtSeeAll: TextView)
    }

    override fun arabicClick(imgArabic: ImageView) {

    }

}