package com.cinescape1.ui.main.views.adapters.foodAdapters

import android.annotation.SuppressLint
import android.app.Activity
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.responseModel.GetFoodResponse
import com.cinescape1.utils.hide
import com.cinescape1.utils.invisible
import com.cinescape1.utils.show
import com.google.android.flexbox.*

class AdapterFoodAddComboTitle( context: Activity, private var foodAddComboTitleList: List<GetFoodResponse.ComboItem>,
    private var alternateItems: List<GetFoodResponse.AlternateItem>, private val listener:RecycleViewItemClickListener,val foodItem:GetFoodResponse.ConcessionItem,val foodPos:Int):
    RecyclerView.Adapter<AdapterFoodAddComboTitle.MyViewHolderFoodAddComboTitle>() {

    private var mContext = context
    private var rowIndex = -1
    private lateinit var v: View
    var displayMetrics = DisplayMetrics()
    private var screenWidth = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderFoodAddComboTitle {
        mContext.windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels
        val view = LayoutInflater.from(parent.context).inflate(R.layout.food_selected_add_combo_title_item, parent, false)
        return MyViewHolderFoodAddComboTitle(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyViewHolderFoodAddComboTitle, @SuppressLint("RecyclerView") position: Int) {
        println("foodAddComboTitleList---$foodAddComboTitleList")
        if (foodAddComboTitleList.isNullOrEmpty()) {

//            val itemPadding = 1
//            //here you may change the divide amount from 2.5 to whatever you need
//            val itemWidth = (screenWidth - itemPadding).div(0.5)
//            val layoutParams = holder.itemView.layoutParams
//            layoutParams.height = layoutParams.height
//            layoutParams.width = itemWidth.toInt()
//            holder.itemView.layoutParams = layoutParams

            screenWidth = displayMetrics.widthPixels
            holder.popcornUi.layoutParams.width = ((screenWidth)/3.4f).toInt()
            println("ScreenWidth22222-------->${((screenWidth)/4f).toInt()}")

            val foodSelctedItem: GetFoodResponse.AlternateItem = alternateItems[position]
            holder.textTitleCombo.text = foodSelctedItem.description
            holder.foodComboSubtitleList.hide()
            holder.imageView.show()
            holder.popcornUi.show()
            holder.comboUi.hide()


            Glide.with(mContext).asBitmap()
                .placeholder(R.drawable.ic_back_button)
                .load(foodSelctedItem.itemImageUrl)
                .into(holder.imageView)

            if (rowIndex==position){
                holder.textView18.show()
            }else{
                holder.textView18.invisible()
            }

            holder.itemView.setOnClickListener {
                print("hello---->${foodSelctedItem.itemImageUrl}")
                rowIndex = position
                notifyDataSetChanged()
                for (data in alternateItems){
                    data.checkFlag = false
                }
                listener.onAlternateClick(foodSelctedItem,foodItem,foodPos)
            }

        } else {

//            val itemPadding = 2
//            //here you may change the divide amount from 2.5 to whatever you need
//            val itemWidth = (screenWidth - itemPadding).div(0.5)
//            val layoutParams = holder.itemView.layoutParams
//            layoutParams.height = layoutParams.height
//            layoutParams.width = itemWidth.toInt()
//            holder.itemView.layoutParams = layoutParams

            val foodSelectedItem = foodAddComboTitleList[position]
            holder.combo_title.text = foodSelectedItem.description
            holder.foodComboSubtitleList.show()
            holder.popcornUi.hide()
            holder.comboUi.show()


//            val marginLayoutParams = MarginLayoutParams(
//                MarginLayoutParams.MATCH_PARENT,
//                MarginLayoutParams.WRAP_CONTENT
//            )
//            marginLayoutParams.setMargins(0, 60, 0, 0)
//            holder.combo_title.layoutParams = marginLayoutParams

            val viewList:ArrayList<View> = ArrayList()
            holder.foodComboSubtitleList.removeAllViews()
            for (item in foodSelectedItem.alternateItems) {

//              v = LayoutInflater.from(mContext).inflate(R.layout.food_selected_add_combo_subtitle_item, holder.foodComboSubtitleList, false)
                val  v : View = mContext.layoutInflater.inflate(R.layout.food_selected_add_combo_subtitle_item, null)
                mContext.windowManager.defaultDisplay.getMetrics(displayMetrics)
                screenWidth = displayMetrics.widthPixels
                val imgSubItem: ImageView = v.findViewById(R.id.img_sub_item)
                val textSubAddFoodName: TextView = v.findViewById(R.id.text_sub_add_food_name)
                val view22: TextView = v.findViewById(R.id.view22)

                viewList.add(v)
                imgSubItem.show()

                screenWidth = displayMetrics.widthPixels
                imgSubItem.layoutParams.width = ((screenWidth)/3.6f).toInt()

                println("ScreenWidth111111-------->${((screenWidth)/3.6f).toInt()}")

                Glide.with(mContext).asBitmap()
                    .load(item.itemImageUrl)
                    .placeholder(R.drawable.ic_back_button)
                    .into(imgSubItem)

                v.tag = item


                textSubAddFoodName.text = item.description

                holder.foodComboSubtitleList.addView(v)
                v.setOnClickListener {
                    val data = it.tag as GetFoodResponse.Item
                    var viewLine: TextView?

                    for(item in foodSelectedItem.alternateItems){
                        item.checkFlag = data.id==item.id
                    }

                    listener.onComboClick(foodSelectedItem,position,foodItem,foodPos)
                    for (view in viewList){
                        viewLine = view.findViewById(R.id.view22)
                        viewLine.invisible()
                    }

                    view22.show()
                  //  data.checkFlag = true

                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (foodAddComboTitleList.isNotEmpty()){
            foodAddComboTitleList.size
        }else{
            alternateItems.size
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadNewData( foodSelectors: List<GetFoodResponse.ComboItem>,alternateItems1: List<GetFoodResponse.AlternateItem>) {
        if (foodSelectors.isNullOrEmpty()){
            alternateItems = alternateItems1
        }else {
            foodAddComboTitleList = foodSelectors
        }
        notifyDataSetChanged()
    }

    interface RecycleViewItemClickListener {
        fun onAlternateClick(
            item: GetFoodResponse.AlternateItem,
            foodItem: GetFoodResponse.ConcessionItem,
            foodPos: Int)

        fun onComboClick(
            item: GetFoodResponse.ComboItem,
            position: Int,
            foodItem: GetFoodResponse.ConcessionItem,
            foodPos: Int
        )
    }

    class MyViewHolderFoodAddComboTitle(view: View) : RecyclerView.ViewHolder(view) {
        var textTitleCombo: TextView = view.findViewById(R.id.text_title_combo)
        var combo_title: TextView = view.findViewById(R.id.combo_title)
        var textView18: TextView = view.findViewById(R.id.textView18)
        var imageView: ImageView = view.findViewById(R.id.imageViewFlavor)
        var popcornUi: ConstraintLayout = view.findViewById(R.id.popcornUi)
        var comboUi: ConstraintLayout = view.findViewById(R.id.comboUi)
        val foodComboSubtitleList = view.findViewById<FlexboxLayout>(R.id.food_combo_subtitle_list)
    }
}