package com.cinescape1.ui.main.views.home.fragments.home.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
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
import com.cinescape1.utils.Constant
import com.cinescape1.utils.Constant.Companion.SEE_ALL_TYPE
import com.cinescape1.utils.hide
import com.cinescape1.utils.show
import com.github.islamkhsh.viewpager2.ViewPager2
import kotlinx.android.synthetic.main.home_parrent_list.view.*
import java.util.*
import kotlin.math.abs

class HomeParentAdapter(
    private var mContext: Activity,
    private var homeDataList: ArrayList<HomeDataResponse.HomeOne>,
    var listener: RecycleViewItemClickListener, var listenerTypeface : TypeFaceInter) :
    RecyclerView.Adapter<HomeParentAdapter.MyViewHolder>(),HomeChildAdapter.ImageChangeIcon {
    var adapter: HomeChildAdapter? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.home_parrent_list, parent, false)
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
                print("TypeKey--->${obj.key}")
                holder.homeTitle.hide()
                holder.txtSeeAll.hide()
                holder.homeList.show()
                holder.viewpagerBack.show()
                holder.viewpager.show()
                holder.search.show()
                holder.viewpagerBack.layoutDirection = View.LAYOUT_DIRECTION_RTL
                val sliderBackAdapter = SliderBackAdapter(mContext, obj.movieData)
                holder.viewpagerBack.adapter = sliderBackAdapter
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

                holder.viewpager.registerOnPageChangeCallback(object : androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {

                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        Log.e("Selected_Page", position.toString())
                        Constant.select_pos = position
                        holder.viewpagerBack.currentItem = position
                    }
                })

            }
            "advance" -> {
                holder.homeTitle.show()
                holder.txtSeeAll.hide()
                holder.consAdvance.show()
                holder.sliderAdvance.show()
                holder.viewpagerBack.hide()
                holder.homeList.hide()
                holder.viewpager.hide()
                val advanceSliderAdapter = AdvanceSliderAdapter(mContext, obj.movieData)
                holder.sliderAdvance.adapter = advanceSliderAdapter
                holder.myTabLayout.setupWithViewPager(holder.sliderAdvance, true)
            }
            "homeOnes" -> {
                val gridLayout = GridLayoutManager(mContext, 1, GridLayoutManager.HORIZONTAL, false)
                holder.homeList.layoutManager = LinearLayoutManager(mContext)
                adapter = HomeChildAdapter(mContext, movieDataList, 1, true, this)
                holder.homeList.layoutManager = gridLayout
                holder.homeList.adapter = adapter
            }
            "offers" -> {
                try {
                    holder.txtSeeAll.hide()
                    holder.itemView.show()
                    holder.homeTitle.show()
                    holder.homeList.show()
                    holder.viewpager.hide()
                    holder.viewpagerBack.hide()

                    val gridLayout = GridLayoutManager(mContext, 1, GridLayoutManager.HORIZONTAL, false)
                    holder.homeList.layoutManager = LinearLayoutManager(mContext)
                   val  adapter = OfferAdapter(mContext, obj.offers)
                    holder.homeList.adapter = adapter
                    println("errorOffer---123>${obj.offers.size}")
                    holder.homeList.layoutManager = gridLayout

//                    val gridLayout = GridLayoutManager(mContext, 1, GridLayoutManager.HORIZONTAL, false)
//                    holder.homeList.layoutManager = LinearLayoutManager(mContext)
//                    val adapter = OfferAdapter(mContext, obj.offers)
//                    holder.homeList.layoutManager = gridLayout
//                    holder.homeList.adapter = adapter
//                    holder.txtSeeAll.setOnClickListener {
//                        val intent = Intent(mContext, SeeAllActivity::class.java)
//                        intent.putExtra("type", "offer")
//                        intent.putExtra("arrayList", obj.movieData)
//                        mContext.startActivity(intent)
//                    }

                } catch (e:Exception){
                    println("errorOffer--->${e.message}")
                }

            }
            "cinemas" -> {
                holder.itemView.show()
                holder.homeTitle.show()
                holder.homeList.show()
                holder.viewpager.hide()
                holder.viewpagerBack.hide()
                holder.itemView.show()
                holder.txtSeeAll.hide()

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
                    holder.itemView.show()
                    holder.txtSeeAll.show()

                    val gridLayout =
                        GridLayoutManager(mContext, 1, GridLayoutManager.HORIZONTAL, false)
                    holder.homeList.layoutManager = LinearLayoutManager(mContext)
                    adapter = HomeChildAdapter(mContext, obj.movieData, 1,true, this)
                    holder.homeList.layoutManager = gridLayout
                    holder.homeList.adapter = adapter
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
                if (obj.movieData.isNotEmpty()) {
                    holder.itemView.show()
                    holder.homeTitle.show()
                    holder.homeList.show()
                    holder.viewpager.hide()
                    holder.viewpagerBack.hide()
                    holder.itemView.show()
                    holder.txtSeeAll.hide()

                    val gridLayout =
                        GridLayoutManager(mContext, 1, GridLayoutManager.HORIZONTAL, false)
                    holder.homeList.layoutManager = LinearLayoutManager(mContext)
                    adapter = HomeChildAdapter(mContext, obj.movieData, 1, false, this)
                    holder.homeList.layoutManager = gridLayout
                    holder.homeList.adapter = adapter
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
        var homeTitle = itemView.home_title!!
        var txtSeeAll = itemView.textView4!!
        var homeList = itemView.homeList!!
        var viewpager = itemView.viewpager!!
        var search = itemView.search!!
        var viewpagerBack = itemView.viewpagerBack!!
        var consAdvance = itemView.consAdvance!!
        var sliderAdvance = itemView.sliderAdvance!!
        var myTabLayout = itemView.my_tablayout!!

    }

    interface RecycleViewItemClickListener {
        fun onSeeAllClick(type:Int)
    }

    interface TypeFaceInter{
       fun typeFace(homeTitle : TextView, txtSeeAll : TextView)
    }

    override fun arabicClick(imgArabic: ImageView) {

    }
}