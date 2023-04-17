package com.cinescape1.ui.main.views.food

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinescape1.R
import com.cinescape1.data.models.requestModel.CancelTransRequest
import com.cinescape1.data.models.requestModel.GetFoodRequest
import com.cinescape1.data.models.requestModel.SaveFoodRequest
import com.cinescape1.data.models.responseModel.GetFoodResponse
import com.cinescape1.data.preference.AppPreferences
import com.cinescape1.databinding.ActivityFoodBinding
import com.cinescape1.ui.main.dailogs.LoaderDialog
import com.cinescape1.ui.main.dailogs.OptionDialog
import com.cinescape1.ui.main.views.adapters.foodAdapters.*
import com.cinescape1.ui.main.views.food.viewModel.FoodViewModel
import com.cinescape1.ui.main.views.home.HomeActivity
import com.cinescape1.ui.main.views.summery.SummeryActivity
import com.cinescape1.utils.*
import com.cinescape1.utils.Constant.Companion.ON_BACK_FOOD
import com.cinescape1.utils.Constant.IntentKey.Companion.TimerExtand
import com.cinescape1.utils.Constant.IntentKey.Companion.TimerTime
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.cancel_dialog.*
import kotlinx.android.synthetic.main.cancel_dialog.view.*
import javax.inject.Inject
import kotlin.math.roundToInt

@Suppress("DEPRECATION", "NAME_SHADOWING")
class FoodActivity : DaggerAppCompatActivity(),
    AdapterFoodSelectedItem.RecycleViewItemClickListener,
    AdapterFoodCombo.RecycleViewItemClickListener,
    AdapterCart.RecycleViewItemClickListener,
    AdapterFoodAddComboTitle.RecycleViewItemClickListener,
    AdapterIndividual.RecycleViewItemClickListener,
    AdapterFoodSelectedItem.TypeFaceListenerFoodItem,
    AdapterIndividual.TypeFaceListenerFoodIndividual,
    AdapterFoodCombo.TypeFaceListenerFoodCombo {

    private var itemFoodAmt: Double = 0.0
    private var itemFoodCount: Int = 0
    private var totalFoodAmt: Double = 0.0
    private var foodSelectedList: ArrayList<GetFoodResponse.ConcessionItem>? = ArrayList()
    private var foodCartList: ArrayList<SaveFoodRequest.ConcessionFood>? = ArrayList()
    private var foodCartListNew: ArrayList<GetFoodResponse.FoodDtls>? = ArrayList()
    private var foodAdapter: AdapterFoodCombo? = null
    private var individualAdapter: AdapterIndividual? = null
    private var foodCartAdapter: AdapterCart? = null
    private var mFoodCartDialog: AlertDialog? = null
    private var movieRatingColor = ""
    private var seatPrice = "0.0"
    private var textNumber: TextView? = null
    private var addBtn: TextView? = null
    private var tvDoneBtn: TextView? = null
    private var textDecrease: ImageView? = null
    private var emptyCart: TextView? = null
    private var textIncrease: TextView? = null
    private var tvFoodPrice: TextView? = null
    private var tvFood1Price: TextView? = null
    private var tvKdTotal: TextView? = null
    private var textTotal1: TextView? = null
    private var cartTime: TextView? = null
    private var tvClearItem: TextView? = null
    private var foodAlterDis = ""
    private var foodAlterId = "0"
    var check = true
    var check1 = true

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var preferences: AppPreferences
    private val foodViewModel: FoodViewModel by viewModels { viewModelFactory }
    private var binding: ActivityFoodBinding? = null
    private var cinemaId = ""
    private var transId = ""
    private var sessionId = ""
    private var booktype = ""
    private var skipBtn = ""
    private var loader: LoaderDialog? = null
    private var tabItem: GetFoodResponse.ConcessionTab? = null
    private var tabItemArr: List<GetFoodResponse.ConcessionTab> = ArrayList()
    private var broadcastReceiver: BroadcastReceiver? = null
    private var timeCount: Long = 0
    private var itemCheckPrice: Int = 0
    private var itemSetPrice: String = ""
    private var itemCartPrice: Int = 0
    private var secondLeft: Long = 0
    private var itemSetCartPrice: String = ""
    private var timeExtandClick: Boolean = false
    private var dialogShow: Long = 60
    private var countDownTimerPrimary: CountDownTimer? = null
    private var comboAdapter: AdapterFoodAddComboTitle? = null

    var foodSelectItemName1: TextView? = null

    // food Individual
    var foodTitleName1: TextView? = null
    var foodcomboName1: TextView? = null
    var foodKdName1: TextView? = null
    var btnDecrease1: TextView? = null
    var txtNumber1: TextView? = null
    var textItemAdded1: TextView? = null
    var btnIncrease1: TextView? = null

    // food combo
    var foodTitleName2: TextView? = null
    var foodcomboName2: TextView? = null
    var foodKdName2: TextView? = null
    var addBtn2: TextView? = null
    var btnDecrease2: TextView? = null
    var btnIncrease2: TextView? = null
    var totalItems2: TextView? = null

    var positionRemove = 0
    var foodItemRemove: ArrayList<GetFoodResponse.ConcessionItem> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodBinding.inflate(layoutInflater, null, false)
        val view = binding?.root

        when {
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "ar" -> {
                LocaleHelper.setLocale(this, "ar")
                val regular = ResourcesCompat.getFont(this, R.font.montserrat_regular)
                val bold = ResourcesCompat.getFont(this, R.font.montserrat_bold)
                val medium = ResourcesCompat.getFont(this, R.font.montserrat_medium)

                binding?.textFoodSelect?.typeface = bold   // semiBold
                binding?.skip?.typeface = bold
                binding?.textView47?.typeface = regular
                binding?.textCancel?.typeface = regular
                binding?.textTimeLeft?.typeface = regular
                binding?.textTimeToLeft?.typeface = bold   // heavy
                binding?.txtProceed?.typeface = bold

                foodSelectItemName1?.typeface = regular

                // food individual
                foodTitleName1?.typeface = bold
                foodcomboName1?.typeface = regular
                foodKdName1?.typeface = regular
                btnDecrease1?.typeface = regular
                txtNumber1?.typeface = regular
                textItemAdded1?.typeface = regular
                btnIncrease1?.typeface = regular

                // food combo
                foodTitleName2?.typeface = bold
                foodcomboName2?.typeface = regular
                foodKdName2?.typeface = regular
                addBtn2?.typeface = regular
                btnDecrease2?.typeface = regular
                btnIncrease2?.typeface = regular
                totalItems2?.typeface = regular

            }
            preferences.getString(Constant.IntentKey.SELECT_LANGUAGE) == "en" -> {
                LocaleHelper.setLocale(this, "en")
                val regular = ResourcesCompat.getFont(this, R.font.sf_pro_text_regular)
                val bold = ResourcesCompat.getFont(this, R.font.sf_pro_text_bold)
                val semiBold = ResourcesCompat.getFont(this, R.font.gibson_semibold)
                val heavy = ResourcesCompat.getFont(this, R.font.sf_pro_text_heavy)
                val medium = ResourcesCompat.getFont(this, R.font.sf_pro_text_medium)

                binding?.textFoodSelect?.typeface = semiBold   // semiBold
                binding?.skip?.typeface = bold
                binding?.textView47?.typeface = regular
                binding?.textCancel?.typeface = regular
                binding?.textTimeLeft?.typeface = regular
                binding?.textTimeToLeft?.typeface = heavy   // heavy
                binding?.txtProceed?.typeface = bold

                foodSelectItemName1?.typeface = regular
                // food individual
                foodTitleName1?.typeface = bold
                foodcomboName1?.typeface = regular
                foodKdName1?.typeface = regular
                btnDecrease1?.typeface = regular
                txtNumber1?.typeface = regular
                textItemAdded1?.typeface = regular
                btnIncrease1?.typeface = regular

                // food combo
                foodTitleName2?.typeface = bold
                foodcomboName2?.typeface = regular
                foodKdName2?.typeface = regular
                addBtn2?.typeface = regular
                btnDecrease2?.typeface = regular
                btnIncrease2?.typeface = regular
                totalItems2?.typeface = regular

            }

            else -> {
                LocaleHelper.setLocale(this, "en")
                val regular = ResourcesCompat.getFont(this, R.font.sf_pro_text_regular)
                val bold = ResourcesCompat.getFont(this, R.font.sf_pro_text_bold)
                val semiBold = ResourcesCompat.getFont(this, R.font.gibson_semibold)
                val heavy = ResourcesCompat.getFont(this, R.font.sf_pro_text_heavy)
                val medium = ResourcesCompat.getFont(this, R.font.sf_pro_text_medium)

                binding?.textFoodSelect?.typeface = semiBold   // semiBold
                binding?.skip?.typeface = bold
                binding?.textView47?.typeface = regular
                binding?.textCancel?.typeface = regular
                binding?.textTimeLeft?.typeface = regular
                binding?.textTimeToLeft?.typeface = heavy   // heavy
                binding?.txtProceed?.typeface = bold

                foodSelectItemName1?.typeface = regular
                // food individual
                foodTitleName1?.typeface = bold
                foodcomboName1?.typeface = regular
                foodKdName1?.typeface = regular
                btnDecrease1?.typeface = regular
                txtNumber1?.typeface = regular
                textItemAdded1?.typeface = regular
                btnIncrease1?.typeface = regular

                // food combo
                foodTitleName2?.typeface = bold
                foodcomboName2?.typeface = regular
                foodKdName2?.typeface = regular
                addBtn2?.typeface = regular
                btnDecrease2?.typeface = regular
                btnIncrease2?.typeface = regular
                totalItems2?.typeface = regular

            }
        }

        setContentView(view)

        try {
            cinemaId = intent.getStringExtra("CINEMA_ID").toString()
            sessionId = intent.getStringExtra("SESSION_ID").toString()
            booktype = intent.getStringExtra("BOOKING").toString()
            transId = intent.getStringExtra("TRANS_ID").toString()

            skipBtn = intent.getStringExtra("typeSkip").toString()

        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (skipBtn == "SkipButtonHide") {
            binding?.txtSkipBtn?.hide()
            binding?.txtProceed?.show()
        }

        movieRatingColor = intent.getStringExtra("movieRatingColor").toString()

        if (booktype != "FOOD") {
            binding?.viewCancel?.setOnClickListener {
                cancelDialog()
            }

            binding?.txtProceed?.setOnClickListener {
                println("foodCartListSizeFOOD---------->${foodCartList?.size}")

                if (!foodCartList.isNullOrEmpty()) {
                    try {
                        val foodRequest = SaveFoodRequest()
                        foodRequest.concessionFoods = foodCartList!!
                        foodRequest.transid = transId
                        foodRequest.cinemaId = cinemaId
                        foodRequest.booktype = booktype
                        foodRequest.userId = preferences.getString(Constant.USER_ID).toString()
                        foodRequest.sessionId = sessionId
                        saveFoods(foodRequest)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                } else {
                    val intent = Intent(this, SummeryActivity::class.java)
                    intent.putExtra("CINEMA_ID", cinemaId)
                    intent.putExtra("SESSION_ID", sessionId)
                    intent.putExtra("TRANS_ID", transId)
                    intent.putExtra("BOOKING", booktype)
                    TimerTime = timeCount
                    startActivity(intent)
                    finish()
                }
            }

            resendTimer()
            seatPrice = intent.getStringExtra("PRICE").toString()

        } else if (booktype == "ADDFOOD") {

            binding?.viewCancel?.setOnClickListener {
                cancelDialog()
            }
            //TEMPRORY CODE
//            toast("bookType--->${booktype}")
            binding?.textTimeToLeft?.hide()
            binding?.textTimeLeft?.hide()
            binding?.textView112?.hide()

            binding?.txtProceed?.setOnClickListener {
                println("foodCartListSizeADDFOOD---------->${foodCartList?.size}---->${booktype}")

                if (!foodCartList.isNullOrEmpty()) {
                    try {
                        val foodRequest = SaveFoodRequest()
                        foodRequest.concessionFoods = foodCartList!!
                        foodRequest.transid = transId
                        foodRequest.cinemaId = cinemaId
                        foodRequest.booktype = booktype
                        foodRequest.userId = preferences.getString(Constant.USER_ID).toString()
                        foodRequest.sessionId = sessionId
                        saveFoods(foodRequest)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                } else {
                    val intent = Intent(this, SummeryActivity::class.java)
                    intent.putExtra("CINEMA_ID", cinemaId)
                    intent.putExtra("SESSION_ID", sessionId)
                    intent.putExtra("TRANS_ID", transId)
                    intent.putExtra("BOOKING", booktype)
                    TimerTime = timeCount
                    startActivity(intent)
                    finish()
                }
            }

//            resendTimer()
            seatPrice = intent.getStringExtra("PRICE").toString()
        } else {
            binding?.textTimeToLeft?.hide()
            binding?.textTimeLeft?.hide()
            binding?.textView112?.hide()
            seatPrice = "0.0"
            binding?.viewCancel?.setOnClickListener {
                val intent = Intent(this@FoodActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }

            binding?.txtProceed?.setOnClickListener {
                println("foodCartListSize21---------->${foodCartList?.size}")

                if (foodCartList!!.size > 0) {
                    try {
                        val foodRequest = SaveFoodRequest()
                        foodRequest.concessionFoods = foodCartList!!
                        foodRequest.transid = transId
                        foodRequest.cinemaId = cinemaId
                        foodRequest.booktype = booktype
                        foodRequest.userId = preferences.getString(Constant.USER_ID).toString()
                        foodRequest.sessionId = sessionId
                        saveFoods(foodRequest)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        "Please select food!",
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {
                        },
                        negativeClick = {
                        })
                    dialog.show()
                }
            }
        }

        broadcastReceiver = MyReceiver()
        broadcastIntent()
        getFood()
        movedNext()

    }

    private fun broadcastIntent() {
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    private fun cancelDialog() {
        val dialog = AlertDialog.Builder(this, R.style.CustomAlertDialog).create()
        val view = layoutInflater.inflate(R.layout.cancel_dialog, null)
        dialog.setView(view)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()

        dialog.consSure?.setOnClickListener {
            finish()
        }

        dialog.negative_btn?.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun movedNext() {
        binding?.viewCart?.setOnClickListener {
            setCartDialog()
        }

//        binding?.txtSkipProceed?.setOnClickListener {

        binding?.txtSkipBtn?.setOnClickListener {
            val intent = Intent(this, SummeryActivity::class.java)
            intent.putExtra("CINEMA_ID", cinemaId)
            intent.putExtra("SESSION_ID", sessionId)
            intent.putExtra("TRANS_ID", transId)
            intent.putExtra("BOOKING", booktype)
            TimerTime = timeCount
            startActivity(intent)
            finish()
        }

    }

    private fun setFoodSelectAdapter(concessionTabs: ArrayList<GetFoodResponse.ConcessionTab>) {
        for (item in concessionTabs) {
            foodItemRemove = item.concessionItems
        }


        if (concessionTabs.isNotEmpty()) {
            binding?.uiFood?.show()
            val horizontalLayoutManagaer =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding?.recyclerFoodSelectItem?.layoutManager = LinearLayoutManager(this)
            val adapter = AdapterFoodSelectedItem(this, concessionTabs, this, this)
            binding?.recyclerFoodSelectItem?.layoutManager = horizontalLayoutManagaer
            binding?.recyclerFoodSelectItem?.adapter = adapter
            adapter.loadNewData(concessionTabs)
            foodSelectedList = concessionTabs[0].concessionItems
            tabItem = concessionTabs[0]
            setFoodComboAdapter(foodSelectedList!!)

        } else {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                getString(R.string.no_food_available),
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {
                    val intent = Intent(this, HomeActivity::class.java)
                    Constant.IntentKey.DialogShow = true
                    Constant.IntentKey.OPEN_FROM = 0
                    startActivity(intent)
                    finish()
                },
                negativeClick = {
                    finish()
                })
            dialog.show()

//            finish()

        }

    }

    override fun onTypeFaceFoodItem(foodSelectItemName: TextView) {
        foodSelectItemName1 = foodSelectItemName
    }

    private fun setFoodComboAdapter(concessionItems: ArrayList<GetFoodResponse.ConcessionItem>) {

        val gridLayout = GridLayoutManager(this@FoodActivity, 1, GridLayoutManager.VERTICAL, false)
        binding?.recyclerFoodCombo?.layoutManager = LinearLayoutManager(this)
        foodAdapter = AdapterFoodCombo(
            this@FoodActivity,
            concessionItems,
            this,
            concessionItems[0].foodtype, this
        )
        binding?.recyclerFoodCombo?.layoutManager = gridLayout
        binding?.recyclerFoodCombo?.adapter = foodAdapter
        foodAdapter?.loadNewData(concessionItems)

    }

    override fun onTypeFaceFoodCombo(
        foodTitleName: TextView,
        foodcomboName: TextView,
        foodKdName: TextView,
        addBtn: TextView,
        btnDecrease: TextView,
        btnIncrease: TextView,
        totalItems: TextView
    ) {
        foodTitleName2 = foodTitleName
        foodcomboName2 = foodcomboName
        foodKdName2 = foodKdName
        addBtn2 = addBtn
        btnDecrease2 = btnDecrease
        btnIncrease2 = btnIncrease
        totalItems2 = totalItems
    }

    override fun onTypeFaceFoodIndividual(
        foodTitleName: TextView,
        foodcomboName: TextView,
        foodKdName: TextView,
        btnDecrease: TextView,
        txtNumber: TextView,
        textItemAdded: TextView,
        btnIncrease: TextView
    ) {

        foodTitleName1 = foodTitleName
        foodcomboName1 = foodcomboName
        foodKdName1 = foodKdName
        btnDecrease1 = btnDecrease
        txtNumber1 = txtNumber
        textItemAdded1 = textItemAdded
        btnIncrease1 = btnIncrease
    }

    override fun onFoodCatClick(foodItem: GetFoodResponse.ConcessionTab, view: View) {
        focusOnView(view, binding?.recyclerFoodSelectItem!!)
        foodSelectedList = foodItem.concessionItems
        tabItem = foodItem
        setFoodComboAdapter(foodSelectedList!!)
    }

    private fun focusOnView(view: View, scrollView: RecyclerView) {
        Handler(Looper.getMainLooper()).post {
            val vLeft = view.left
            val vRight = view.right
            val sWidth = scrollView.width
            scrollView.scrollBy((vLeft + vRight - sWidth) / 2, 0)
        }
    }

    //Cart Dialog
    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun setCartDialog() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.food_cart_dailog_info, null)
        val mBuilder = AlertDialog.Builder(this, R.style.CustomAlertDialog).setView(mDialogView)

        if (!isFinishing) {
            mFoodCartDialog = mBuilder?.show()
        }

        mFoodCartDialog?.show()
        mBuilder.setCancelable(false)
        mFoodCartDialog?.window?.setBackgroundDrawableResource(R.color.transparent)
        val img1Close = mDialogView.findViewById<ImageView>(R.id.icon_cross)
        val window = mFoodCartDialog?.window
        val wlp: WindowManager.LayoutParams? = window?.attributes
        wlp?.gravity = Gravity.BOTTOM
        wlp?.flags = wlp?.flags?.and(WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv())
        window?.attributes = wlp
        val recyclerviewCartItem =
            mDialogView.findViewById<RecyclerView>(R.id.recyclerview_cart_item)
        val proceedBtn1 = mDialogView.findViewById<TextView>(R.id.proceed_btn1)
        tvClearItem = mDialogView.findViewById<TextView>(R.id.tv_clear_item)
        emptyCart = mDialogView.findViewById(R.id.emptyCart)
        val titleTicketPrice = mDialogView.findViewById<TextView>(R.id.title_ticketPrice)
        val movieDetails = mDialogView.findViewById<ConstraintLayout>(R.id.movieDetails)
        val totals1 = mDialogView.findViewById<TextView>(R.id.totals1)
        cartTime = mDialogView.findViewById(R.id.cartTime)
        val viewFood = mDialogView.findViewById<View>(R.id.viewFood)
        textTotal1 = mDialogView.findViewById(R.id.text_total1)
        tvFoodPrice = mDialogView.findViewById(R.id.tv_food_price)
        tvFood1Price = mDialogView.findViewById(R.id.tv_food1_price)
        val textTicket1Price = mDialogView.findViewById<TextView>(R.id.text_ticket1_price)

        if (booktype == "FOOD") {
            titleTicketPrice.hide()
            totals1.show()
            textTotal1?.show()
            textTicket1Price.hide()
            movieDetails.hide()
            viewFood.show()
            tvFoodPrice?.text = getAllFoodPrice()

            try {
                textTotal1?.text = getString(R.string.price_kd) + " ${
                    Constant.DECIFORMAT.format(
                        getAllFoodPrice().replace("KWD ", "").toDouble()
                    )
                }"
            } catch (e: Exception) {
                print("msg----->${e.message}")
                e.printStackTrace()
            }


        } else {
            viewFood.hide()
            titleTicketPrice.show()
            totals1.show()
            textTotal1?.show()
            textTicket1Price.show()
            movieDetails.show()
            if (getAllFoodPrice().isNullOrEmpty()) {
                tvFoodPrice?.hide()
                tvFood1Price?.hide()
            } else {
                tvFoodPrice?.show()
                tvFood1Price?.show()
                tvFoodPrice?.text = getAllFoodPrice()
            }

            textTicket1Price.text = seatPrice
            try {
                textTotal1?.text = getString(R.string.price_kd) + " ${
                    Constant.DECIFORMAT.format(
                        getAllFoodPrice().replace("KWD ", "").toDouble() + seatPrice.replace(
                            "KWD ",
                            ""
                        ).toDouble()
                    )
                }"
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val title = mDialogView.findViewById<TextView>(R.id.tv_seat_film_title)
            val rating = mDialogView.findViewById<TextView>(R.id.rating)
            val cinemaName = mDialogView.findViewById<TextView>(R.id.tv_cinema_name)
            val timing = mDialogView.findViewById<TextView>(R.id.tv_seat_timing_date)
            val type = mDialogView.findViewById<TextView>(R.id.text_type)
            val image = mDialogView.findViewById<ImageView>(R.id.imageView54)

            //Cinema
            //Image
            Glide.with(this)
                .load(intent.getStringExtra("movieImage").toString()) // image url
                .error(R.drawable.app_icon)  // any image in case of error
                .centerCrop()
                .into(image)  // imageview object

            // title
            title.text = intent.getStringExtra("movieTitle").toString()
            //RATING
            rating.text = intent.getStringExtra("movieRating").toString()
            //MovieCinema Name
            cinemaName.text = intent.getStringExtra("movieCinemaName").toString()
            timing.text = intent.getStringExtra("movieTimeDate").toString()
            type.text = intent.getStringExtra("movieType").toString()
            try {
                rating.setBackgroundColor(Color.parseColor(movieRatingColor))
            } catch (e: Exception) {
                println("foodError--------->${e.message}")
            }

        }

        itemCheckPrice = calculateTotal()
        itemSetPrice = Constant.DECIFORMAT.format(itemCheckPrice / 100.0)
        tvFoodPrice?.text = getAllFoodPrice()

        val gridLayout = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        recyclerviewCartItem.layoutManager = LinearLayoutManager(this)
        foodCartAdapter = AdapterCart(this, foodCartListNew!!, this)
        recyclerviewCartItem.layoutManager = gridLayout
        recyclerviewCartItem.adapter = foodCartAdapter
        foodCartAdapter?.loadNewData(foodCartListNew!!)
        println("foodCartListNewDetail--------->${foodCartListNew!!}")

        proceedBtn1.setOnClickListener {
            if (!foodCartList.isNullOrEmpty()) {
                try {
                    val foodRequest = SaveFoodRequest()
                    foodRequest.concessionFoods = foodCartList!!
                    foodRequest.transid = transId
                    foodRequest.cinemaId = cinemaId
                    foodRequest.booktype = booktype
                    foodRequest.userId = preferences.getString(Constant.USER_ID).toString()
                    foodRequest.sessionId = sessionId
                    saveFoods(foodRequest)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    "Please select food!",
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {
                    },
                    negativeClick = {
                    })
                dialog.show()
            }
        }

        if (foodCartList?.size!! > 0) {
            tvClearItem?.show()
        } else {

            tvClearItem?.invisible()
            emptyCart?.show()
//            proceedBtn1.setText(R.string.close)
            proceedBtn1.setText(R.string.proceed)
            proceedBtn1.setOnClickListener {
                mFoodCartDialog?.dismiss()
            }
        }

        img1Close.setOnClickListener {
            foodCartAdapter?.loadNewData(foodCartListNew!!)
            individualAdapter?.notifyDataSetChanged()
            mFoodCartDialog?.dismiss()
        }

        tvClearItem?.setOnClickListener {

            val dialog = AlertDialog.Builder(this, R.style.CustomAlertDialog).create()
            val view = layoutInflater.inflate(R.layout.cancel_dialog, null)
            dialog.setView(view)

            dialog.setCanceledOnTouchOutside(false)
            dialog.show()

            dialog.subtitle.text = getString(R.string.seatTypeReset)
            dialog.negative_btn.text = getString(R.string.yes)
            dialog.txtSureNew.text = getString(R.string.noComma)
            dialog.txtGoBack.text = getString(R.string.gobackComma)
            dialog.show()

            dialog.consSure?.setOnClickListener {
                dialog.dismiss()
//                    finish()
            }

            dialog.negative_btn?.setOnClickListener {

                foodCartListNew?.clear()
                foodCartList?.clear()
                updateFoodList()
                binding?.textCartCountNotiication?.text = ""
                binding?.textCartCountNotiication?.invisible()
                foodCartAdapter?.loadNewData(foodCartListNew!!)
                emptyCart?.show()
                tvClearItem?.hide()

                updateSelectedList(null, 1)
                foodCartAdapter?.notifyDataSetChanged()
                individualAdapter?.notifyDataSetChanged()
                mFoodCartDialog?.dismiss()

                binding?.txtProceed?.hide()
                binding?.txtSkipBtn?.show()


                overridePendingTransition(0, 0)
                startActivity(intent)
                finish()
            }


        }
    }

    //390
    private fun resendTimer() {
        countDownTimerPrimary = object : CountDownTimer((TimerTime * 1000), 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val second = millisUntilFinished / 1000 % 60
                val minutes = millisUntilFinished / (1000 * 60) % 60
                val display = java.lang.String.format("%02d:%02d", minutes, second)

                binding?.textTimeToLeft?.text = display
                cartTime?.text = display
                binding?.cartTime1?.text = display
                timeCount = minutes * 60 + second
                secondLeft = second
                println("timerrr--->${second}")
                if (timeCount == dialogShow) {
                    if (!Constant.IntentKey.TimerExtandCheck) {
                        extendTime()
                    } else if (Constant.IntentKey.TimerExtandCheck && TimerExtand > 0) {
                        object : CountDownTimer((TimerExtand * 1000), 1000) {
                            @SuppressLint("SetTextI18n")
                            override fun onTick(millisUntilFinished: Long) {
                                val second = millisUntilFinished / 1000 % 60
                                val minutes = millisUntilFinished / (1000 * 60) % 60
                                val display = java.lang.String.format("%02d:%02d", minutes, second)

                                binding?.textTimeToLeft?.text = display
                                cartTime?.text = display
                                binding?.cartTime1?.text = display
                                println("getTimeValue--->${cartTime?.text}")
                                TimerExtand = minutes * 60 + second
                            }

                            override fun onFinish() {
                                val dialog = OptionDialog(this@FoodActivity,
                                    R.mipmap.ic_launcher,
                                    R.string.app_name,
                                    getString(R.string.timeOut),
                                    positiveBtnText = R.string.ok,
                                    negativeBtnText = R.string.no,
                                    positiveClick = {
                                        TimerTime = 360
                                        TimerExtand = 90
                                        finish()
                                    },
                                    negativeClick = {
                                    })
                                if (!isFinishing) {
                                    dialog.show()
                                }

                            }
                        }.start()
                    } else if (Constant.IntentKey.TimerExtandCheck && TimerExtand < 0) {
                        val dialog = OptionDialog(this@FoodActivity,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            getString(R.string.timeOut),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {
                            },
                            negativeClick = {
                            })
                        dialog.show()
                    }
                }

            }

            override fun onFinish() {

                if (!timeExtandClick) {
                    TimerExtand = 90
                    TimerTime = 360
                    finish()
                }

            }
        }.start()
    }

    //90
    private fun extendTime() {
        val viewGroup = findViewById<ViewGroup>(android.R.id.content)
        val dialogView: View =
            LayoutInflater.from(this).inflate(R.layout.cancel_dialog, viewGroup, false)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        builder.setCancelable(false)
        val alertDialog: AlertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawableResource(R.color.transparent)
        if (this.hasWindowFocus()) {
            alertDialog.show()
        }
        dialogView.title.text = getString(R.string.app_name)
        dialogView.subtitle.text = getString(R.string.stillHere)
        dialogView.consSure?.setOnClickListener {
            countDownTimerPrimary?.cancel()
            timeExtandClick = true
            TimerExtand = 90 + secondLeft
            Constant.IntentKey.TimerExtandCheck = true
            alertDialog.dismiss()
            object : CountDownTimer((TimerExtand * 1000), 1000) {
                @SuppressLint("SetTextI18n")
                override fun onTick(millisUntilFinished: Long) {
                    val second = millisUntilFinished / 1000 % 60
                    val minutes = millisUntilFinished / (1000 * 60) % 60
                    binding?.textTimeToLeft?.text = "$minutes:$second"
                    cartTime?.text = "$minutes:$second"
                    binding?.cartTime1?.text = "$minutes:$second"
                    println("getTimeValue---2>${cartTime?.text}")

                    TimerExtand = minutes * 60 + second
                }

                override fun onFinish() {
                    TimerExtand = 90
                    TimerTime = 360
                    finish()
                }
            }.start()
        }

        dialogView.negative_btn?.setOnClickListener {
            TimerExtand = 90
            TimerTime = 360
            finish()
        }

    }

    private fun updateFoodList() {

        for (item in foodSelectedList!!) {
            if (item.quantity > 0) {
                item.quantity = 0
            }
        }

    }

    private fun getAllFoodPrice(): String {

        var price = 0
        for (data in foodCartListNew!!) {
            price += data.foodAmount.toInt()
            itemCheckPrice = data.foodAmount.toInt()
        }
        return getString(R.string.price_kd) + " " + "${Constant.DECIFORMAT.format(price / 100.0)}"

    }

    @SuppressLint("SetTextI18n")
    private fun getCartFoodPrice(): Int {
        var price = 0
        for (data in foodCartListNew!!) {
            price += data.foodAmount.toInt()
            itemCheckPrice = data.foodAmount.toInt()
        }
        try {
            textTotal1?.text = getString(R.string.price_kd) + " ${
                Constant.DECIFORMAT.format(
                    (price / 100.0) + seatPrice.replace("KD ", "").toDouble()
                )
            }"
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return price
    }

    private fun getFood() {
        foodViewModel.getFood(GetFoodRequest(cinemaId))
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.code == Constant.SUCCESS_CODE) {

                                    tabItemArr = it.data.output.concessionTabs
                                    setFoodSelectAdapter(it.data.output.concessionTabs)
                                } else {
                                    loader?.dismiss()
                                    val dialog = OptionDialog(this,
                                        R.mipmap.ic_launcher,
                                        R.string.app_name,
                                        it.data?.msg.toString(),
                                        positiveBtnText = R.string.ok,
                                        negativeBtnText = R.string.no,
                                        positiveClick = {
                                            finish()
                                        },
                                        negativeClick = {
                                            finish()
                                        })
                                    dialog.show()
                                }

                            }
                        }
                        Status.ERROR -> {
                            loader?.dismiss()
                            val dialog = OptionDialog(this,
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                it.message.toString(),
                                positiveBtnText = R.string.ok,
                                negativeBtnText = R.string.no,
                                positiveClick = {
                                },
                                negativeClick = {
                                })
                            dialog.show()
                        }
                        Status.LOADING -> {
                            loader = LoaderDialog(R.string.pleasewait)
                            loader?.show(supportFragmentManager, null)
                        }
                    }
                }
            }
    }

    // Save Foods
    private fun saveFoods(request: SaveFoodRequest) {
        foodViewModel.saveFood(request)
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.code == Constant.SUCCESS_CODE) {
                                    try {
                                        mFoodCartDialog?.dismiss()
                                        val intent =
                                            Intent(this, SummeryActivity::class.java)

                                        intent.putExtra("CINEMA_ID", cinemaId)
                                        intent.putExtra("SESSION_ID", sessionId)
                                        intent.putExtra("TRANS_ID", it.data.output.transid)
                                        intent.putExtra("BOOKING", it.data.output.booktype)
                                        intent.putExtra("booktype", booktype)

                                        TimerTime = timeCount
                                        startActivity(intent)
                                        finish()
                                    } catch (e: Exception) {
                                        println("updateUiCinemaSession ---> ${e.message}")
                                    }

                                } else {
                                    loader?.dismiss()
                                    val dialog = OptionDialog(this,
                                        R.mipmap.ic_launcher,
                                        R.string.app_name,
                                        it.data?.msg.toString(),
                                        positiveBtnText = R.string.ok,
                                        negativeBtnText = R.string.no,
                                        positiveClick = {
                                        },
                                        negativeClick = {
                                        })
                                    dialog.show()
                                }

                            }
                        }
                        Status.ERROR -> {
                            loader?.dismiss()
                            val dialog = OptionDialog(this,
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                it.message.toString(),
                                positiveBtnText = R.string.ok,
                                negativeBtnText = R.string.no,
                                positiveClick = {
                                },
                                negativeClick = {
                                })
                            dialog.show()
                        }
                        Status.LOADING -> {
                            loader = LoaderDialog(R.string.pleasewait)
                            loader?.show(supportFragmentManager, null)
                        }
                    }
                }
            }
    }

    //Add Food
    @SuppressLint("SetTextI18n")
    override fun onAddFood(
        foodItem: GetFoodResponse.ConcessionItem,
        position: Int,
        foodComboList: ArrayList<GetFoodResponse.ConcessionItem>
    ) {

        if (foodItem.foodtype == "Individual") {

            var amount = 0.0
            val a = foodItem.quantity + 1
            foodItem.quantity = a
            amount = (foodItem.quantity * foodItem.priceInCents).toDouble()
            foodItem.itemTotal = amount.toInt()
            updateCartList(foodItem)

            if (foodCartListNew?.size!! > 0) {
                binding?.textCartCountNotiication?.show()
                binding?.textCartCountNotiication?.text = foodCartListNew?.size.toString()

                binding?.txtProceed?.show()
                binding?.txtSkipBtn?.hide()
            } else {
                binding?.textCartCountNotiication?.text = "0"
                binding?.textCartCountNotiication?.show()

                binding?.txtProceed?.hide()
                binding?.txtSkipBtn?.show()
            }
            individualAdapter?.notifyDataSetChanged()
            comboAdapter?.notifyDataSetChanged()

        } else {

            //combo Food
            val mDialogView =
                LayoutInflater.from(this).inflate(R.layout.food_selected_add_alert_dailog, null)
            val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
            val mAlertDialog = mBuilder.show()
            mAlertDialog.show()
            mAlertDialog.window?.setBackgroundDrawableResource(R.color.transparent)

            val textComboKdPrice = mDialogView.findViewById<TextView>(R.id.text_combo_kd_price)
            val textComboSubtitle = mDialogView.findViewById<TextView>(R.id.text_combo_subtilte)
            val text_combo_head = mDialogView.findViewById<TextView>(R.id.text_combo_head)
            val imageView10 = mDialogView.findViewById<ImageView>(R.id.imageView10)
            val img1Close = mDialogView.findViewById<ImageView>(R.id.img1_close)

//        imageView10.setColorFilter(ContextCompat.getColor(applicationContext,R.color.foodBgColor))

            Glide.with(this)
                .load(foodItem.itemImageUrl)
                .placeholder(R.drawable.placeholder_icon)
                .into(imageView10)

            textComboKdPrice.text = foodItem.itemPrice
            textComboSubtitle.text = foodItem.description
            img1Close.setOnClickListener {
                mAlertDialog.dismiss()
            }

            if (foodItem.title != "") {
                text_combo_head.show()
            } else {
                text_combo_head.hide()
            }

            val recyclerviewComboTitle =
                mDialogView.findViewById<View>(R.id.recyclerview_combo_title) as RecyclerView
            textDecrease = mDialogView.findViewById<View>(R.id.text_decrease) as ImageView
            textIncrease = mDialogView.findViewById<View>(R.id.text_increase) as TextView
            textNumber = mDialogView.findViewById<View>(R.id.text_number) as TextView
            addBtn = mDialogView.findViewById(R.id.text_add_btn)
            tvDoneBtn = mDialogView.findViewById(R.id.tv_done_btn)
            tvKdTotal = mDialogView.findViewById(R.id.tv_kd_total)
//        val viewIncreaseDecreases: View = mDialogView.findViewById(R.id.view_increase_decrease)
            textIncrease?.isClickable = false
            textIncrease?.isEnabled = false
            textDecrease?.isClickable = false
            textDecrease?.isEnabled = false

            tvDoneBtn?.isClickable = false
            tvDoneBtn?.isEnabled = false
            tvDoneBtn?.background = ContextCompat.getDrawable(this, R.drawable.food_item_bg)

            itemFoodCount = foodItem.quantity
            itemFoodAmt = foodItem.itemTotal.toDouble()
            foodItem.quantity = 0
            foodItem.itemTotal = 0
            tvKdTotal?.text = getString(R.string.price_kd) + " " + "0.000"

            when (foodItem.foodtype) {
                "Flavour" -> {
                    try {
                        val bold = ResourcesCompat.getFont(this, R.font.sf_pro_text_bold)
                        textComboSubtitle.setTextColor(ContextCompat.getColor(this, R.color.white))
                        textComboSubtitle.textSize = 17f
                        textComboSubtitle.typeface = bold

                        val quantity = foodItem.quantity.toString()
                        textNumber?.text = quantity
                        var spancount = 1
                        println("foodItem.packageChildItems.size--->" + foodItem.alternateItems.size)
                        when (foodItem.alternateItems.size) {
                            3 -> {
                                spancount = 1
                            }
                            in 4..5 -> {
                                spancount = 2
                            }
                            in 7..8 -> {
                                spancount = 3
                            }
                            in 10..11 -> {
                                spancount = 4
                            }
                        }

                        val gridLayout =
                            GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
                        recyclerviewComboTitle.layoutManager = LinearLayoutManager(this)
                        val adapter = AdapterFoodAddComboTitle(
                            this,
                            foodItem.packageChildItems,
                            foodItem.alternateItems,
                            this, foodItem, position
                        )

//                    val layoutManager = FlexboxLayoutManager(this)
//                    layoutManager.flexDirection = FlexDirection.ROW
//                    layoutManager.justifyContent = JustifyContent.FLEX_START
//                    layoutManager.alignItems = AlignItems.FLEX_START

                        recyclerviewComboTitle.layoutManager = gridLayout

                        recyclerviewComboTitle.adapter = adapter
                        adapter.loadNewData(foodItem.packageChildItems, foodItem.alternateItems)

                        /*todo calculation for single flavour*/
                        textIncrease?.setOnClickListener {
                            if (foodItem.itemTotal > 0) {
                                if (textNumber?.text.toString() != "20") {
                                    var amount = 0.0
                                    val a = Integer.parseInt(textNumber?.text.toString()) + 1
                                    for (i in foodItem.alternateItems.indices) {
                                        if (foodItem.alternateItems[i].checkFlag) {
                                            foodItem.alternateItems[i].subItemCount = a
                                            amount =
                                                foodItem.alternateItems[i].subItemCount * foodItem.alternateItems[i].priceInCents.toDouble()
                                        }
                                    }
                                    foodItem.itemTotal = amount.roundToInt()
                                    foodItem.quantity = a
                                    textNumber?.text = a.toString()
                                    tvKdTotal?.text = getString(R.string.price_kd) + " ${
                                        Constant.DECIFORMAT.format(amount / 100)
                                    }"

                                } else {
                                    toast("Can not add more")
                                }
                            }
                        }

                        textDecrease?.setOnClickListener {
                            var amount = 0.0
                            if (textNumber?.text.toString().toInt() > 1) {
                                val a = Integer.parseInt(textNumber?.text.toString()) - 1
                                for (i in foodItem.alternateItems.indices) {
                                    if (foodItem.alternateItems[i].checkFlag) {
                                        foodItem.alternateItems[i].subItemCount = a
                                        amount =
                                            foodItem.alternateItems[i].subItemCount * foodItem.alternateItems[i].priceInCents.toDouble()
                                    }
                                }
                                foodItem.itemTotal = amount.roundToInt()
                                foodItem.quantity = a
                                textNumber?.text = a.toString()
                                tvKdTotal?.text =
                                    getString(R.string.price_kd) + " ${
                                        Constant.DECIFORMAT.format(
                                            amount / 100
                                        )
                                    }"
                            }
                        }

                        tvDoneBtn?.setOnClickListener {
                            var valflag = false
                            for (i in foodItem.alternateItems.indices) {
                                if (foodItem.alternateItems[i].checkFlag) {
                                    valflag = true
                                    break
                                }
                            }
                            if (!valflag) {
                                // message for select flavour
                                toast("Please select flavour")
                            } else if (textNumber?.text.toString() == "0") {
                                // Message for min quantity
                                toast("Message for min quantity")
                            } else {
                                updateCartList(foodItem)
                                mAlertDialog.dismiss()
                            }

                            if (foodCartListNew?.size!! > 0) {
                                binding?.textCartCountNotiication?.show()
                                binding?.textCartCountNotiication?.text =
                                    foodCartListNew?.size.toString()

                                binding?.txtProceed?.show()
                                binding?.txtSkipBtn?.hide()
                            } else {
                                binding?.textCartCountNotiication?.invisible()

                                if (booktype == "FOOD"){
                                    binding?.txtProceed?.show()
                                    binding?.txtSkipBtn?.hide()
                                }else{
                                    binding?.txtProceed?.hide()
                                    binding?.txtSkipBtn?.show()
                                }

                            }

                            updateItemUi(foodItem)
                            foodAdapter?.loadNewData(foodComboList)
                        }

                    } catch (e: Exception) {
                        println("Exception---${e.message}")
                    }
                }

                "combo" -> {

//                    text_combo_head.show()
                    if (foodItem.title.isNotEmpty()) {
                        text_combo_head.show()
                    } else {
                        text_combo_head.hide()
                    }

                    text_combo_head.text = foodItem.title
                    comboAdapter?.notifyDataSetChanged()
                    var spancount = 1
                    println("foodItem.packageChildItems.size--->" + foodItem.alternateItems.size)
                    when (foodItem.alternateItems.size) {
                        3 -> {
                            spancount = 1
                        }
                        in 4..5 -> {
                            spancount = 2
                        }
                        in 7..8 -> {
                            spancount = 3
                        }
                        in 10..11 -> {
                            spancount = 4
                        }
                    }
//                val gridLayout = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
//                recyclerviewComboTitle.layoutManager = LinearLayoutManager(this)

                    comboAdapter = AdapterFoodAddComboTitle(
                        this,
                        foodItem.packageChildItems,
                        foodItem.alternateItems,
                        this, foodItem, position
                    )

                    val layoutManager = FlexboxLayoutManager(this)
                    layoutManager.flexDirection = FlexDirection.ROW
                    layoutManager.justifyContent = JustifyContent.FLEX_START
                    layoutManager.alignItems = AlignItems.FLEX_START
                    recyclerviewComboTitle.layoutManager = layoutManager

                    recyclerviewComboTitle.adapter = comboAdapter
                    comboAdapter?.loadNewData(foodItem.packageChildItems, foodItem.alternateItems)
//                tv_kd_total?.text = foodItem.itemPrice
                    textNumber?.text = foodItem.quantity.toString()
                    var amount = 0.0
                    var a = 0

                    textIncrease?.setOnClickListener {
                        if (textNumber?.text.toString() != "20") {
                            a = Integer.parseInt(textNumber?.text.toString()) + 1
                            foodItem.quantity = a
                            amount = a * foodItem.priceInCents.toDouble()
                            foodItem.itemTotal = amount.roundToInt()
                            textNumber?.text = a.toString()
                            itemCheckPrice = amount.toInt()
                            tvKdTotal?.text =
                                getString(R.string.price_kd) + " ${Constant.DECIFORMAT.format(amount / 100)}"
                        }
                    }

                    textDecrease?.setOnClickListener {
                        if (textNumber?.text.toString().toInt() > 1) {
                            a = Integer.parseInt(textNumber?.text.toString()) - 1
                            foodItem.quantity = a
                            amount = a * foodItem.priceInCents.toDouble()
                            foodItem.itemTotal = amount.roundToInt()
                            textNumber?.text = a.toString()
                            itemCheckPrice = amount.toInt()
                            tvKdTotal?.text =
                                getString(R.string.price_kd) + " ${Constant.DECIFORMAT.format(amount / 100)}"
                        }
                    }

                    tvDoneBtn?.setOnClickListener {
                        var valflag = 0
                        for (i in 0 until foodItem.packageChildItems.size) {
                            for (j in 0 until foodItem.packageChildItems[i].alternateItems.size) {
                                if (foodItem.packageChildItems[i].alternateItems[j].checkFlag) {
                                    valflag++
                                    break
                                }
                            }
                        }
                        when {
                            valflag != foodItem.packageChildItems.size -> {
                                toast("Select combo items")
                            }
                            textNumber?.text.toString() == "0" -> {
                                toast("Select combo items quantity")
                            }
                            else -> {
                                mAlertDialog.dismiss()
                                updateCartList(foodItem)
                            }
                        }
                        if (foodCartListNew?.size!! > 0) {
                            binding?.textCartCountNotiication?.show()
                            binding?.textCartCountNotiication?.text =
                                foodCartListNew?.size.toString()

                            binding?.txtProceed?.show()
                            binding?.txtSkipBtn?.hide()
                        } else {
                            binding?.textCartCountNotiication?.invisible()

                            binding?.txtProceed?.hide()
                            binding?.txtSkipBtn?.show()
                        }

                        updateItemUi(foodItem)
                        foodAdapter?.loadNewData(foodComboList)

                    }

                }

            }
        }

    }

    private fun updateItemUi(crossinline: GetFoodResponse.ConcessionItem) {
        for (item in foodCartListNew!!) {
            if (item.foodId == crossinline.id) {
                crossinline.quantityUpdate = crossinline.quantityUpdate + crossinline.quantity
                break
            }
        }
        individualAdapter?.notifyDataSetChanged()
    }

    override fun onDecreaseFood(foodItem: GetFoodResponse.ConcessionItem, position: Int) {
        println("foodItem.foodtype-->${foodItem.foodtype}")
        if (foodItem.foodtype == "Individual") {
            var amount = 0.0
            if (foodItem.quantity > 0) {
                foodItem.quantity = foodItem.quantity - 1
                amount = (foodItem.quantity * foodItem.priceInCents).toDouble()
                foodItem.itemTotal = amount.toInt()
                updateCartList(foodItem)
                foodSelectedList?.removeAt(position)
                foodSelectedList?.add(position, foodItem)
                foodAdapter?.loadNewData(foodSelectedList!!)
            }
            if (foodCartListNew?.size!! > 0) {
                binding?.textCartCountNotiication?.show()
                binding?.textCartCountNotiication?.text = foodCartListNew?.size.toString()


                    binding?.txtProceed?.show()
                    binding?.txtSkipBtn?.hide()


            } else {
                binding?.textCartCountNotiication?.text = "0"
                binding?.textCartCountNotiication?.show()

                if (booktype == "FOOD"){
                    binding?.txtProceed?.show()
                    binding?.txtSkipBtn?.hide()
                }else{
                    binding?.txtProceed?.hide()
                    binding?.txtSkipBtn?.show()
                }

            }
            comboAdapter?.notifyDataSetChanged()

        } else {
            var num = foodItem.quantity
            if (num < 0 || num == 0) {
                Toast.makeText(this, "sorry", Toast.LENGTH_LONG).show()
            } else {
                num -= 1
                foodItem.quantity = num
                foodSelectedList?.removeAt(position)
                updateCartList(foodItem)
                foodSelectedList?.add(position, foodItem)
                foodAdapter?.loadNewData(foodSelectedList!!)

            }
        }

    }

    override fun onIncreaseFood(foodItem: GetFoodResponse.ConcessionItem, position: Int) {

        if (foodItem.foodtype == "Individual") {
            var amount = 0.0
            if (foodItem.quantity < 20) {
                foodItem.quantity = foodItem.quantity + 1
                println("CheckQt---->${foodItem.quantity}")
                amount = (foodItem.quantity * foodItem.priceInCents).toDouble()
                foodItem.itemTotal = amount.toInt()
                foodSelectedList?.removeAt(position)
                foodSelectedList?.add(position, foodItem)
                updateCartList(foodItem)
                foodAdapter?.loadNewData(foodSelectedList!!)
                println("CheckSize--->${foodCartListNew!![0].foodQuan}")
                comboAdapter?.notifyDataSetChanged()
            } else {
                toast("add less than 20 items")
            }
        } else {

            var num = foodItem.quantity
            if (num > 10 || num == 10) {
                Toast.makeText(this, "sorry 10 limit", Toast.LENGTH_LONG).show()
            } else {
                num += 1
                foodItem.quantity = num
                foodSelectedList?.removeAt(position)
                foodSelectedList?.add(position, foodItem)
                updateCartList(foodItem)
                foodAdapter?.loadNewData(foodSelectedList!!)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onIncreaseCart(foodItem: GetFoodResponse.FoodDtls, pos: Int) {
        if (foodCartListNew?.get(pos)?.foodQuan!! < 20) {
            foodCartListNew?.get(pos)?.foodQuan = foodCartListNew?.get(pos)?.foodQuan!! + 1
            foodCartListNew?.get(pos)?.foodAmount =
                foodCartListNew?.get(pos)?.foodQuan!! * foodCartListNew?.get(pos)?.itemPrice!!
            for (i in tabItemArr.indices) {
                for (j in 0 until tabItemArr[i].concessionItems.size) {
                    if (foodCartListNew?.get(pos)?.foodId == tabItemArr[i].concessionItems[j].id) {
                        tabItemArr[i].concessionItems[j].footItemCount =
                            foodCartListNew?.get(pos)?.foodQuan!!
                        tabItemArr[i].concessionItems[j].quantity =
                            foodCartListNew?.get(pos)?.foodQuan!!
                        tabItemArr[i].concessionItems[j].itemTotal =
                            foodCartListNew?.get(pos)?.foodAmount?.toInt()!!
                    }
                }
            }

            updateSelectedItem(true, pos, tabItem?.concessionItems!!, foodItem)

            if (foodItem.foodType.uppercase() == "INDIVIDUAL") {
                individualAdapter?.loadNewData(tabItem?.concessionItems!!)
            } else {
                updateSelectedList(foodItem, 2)
            }
            foodCartAdapter?.notifyDataSetChanged()
            foodAdapter?.notifyDataSetChanged()
        } else {

            toast("Select only 20 items")
        }
        itemCartPrice += getCartFoodPrice()
        itemSetCartPrice = Constant.DECIFORMAT.format(itemCheckPrice / 100.0)
        tvFoodPrice?.text = getAllFoodPrice()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onDecreaseCart(foodItem: GetFoodResponse.FoodDtls, pos: Int) {

        foodCartListNew?.get(pos)?.foodQuan?.minus(1)
        if (foodCartListNew?.get(pos)?.foodQuan!! > 0) {
//            Toast.makeText(this, "hello 00", Toast.LENGTH_SHORT).show()

            foodCartListNew?.get(pos)?.foodQuan = foodCartListNew?.get(pos)?.foodQuan!! - 1
            foodCartListNew?.get(pos)?.foodAmount =
                foodCartListNew?.get(pos)?.foodQuan!! * foodCartListNew?.get(pos)?.itemPrice!!

            for (i in tabItemArr.indices) {
                for (j in 0 until tabItemArr[i].concessionItems.size) {
                    if (foodCartListNew?.get(pos)?.foodId.equals(tabItemArr[i].concessionItems[j].id)) {
//                        Toast.makeText(this, "hello 22", Toast.LENGTH_SHORT).show()

                        tabItemArr[i].concessionItems[j].footItemCount =
                            foodCartListNew?.get(pos)?.foodQuan!!
                        tabItemArr[i].concessionItems[j].quantity =
                            foodCartListNew?.get(pos)?.foodQuan!!
                        tabItemArr[i].concessionItems[j].itemTotal =
                            foodCartListNew?.get(pos)?.foodAmount?.toInt()!!
                    }
                }
            }
            updateSelectedItem(false, pos, tabItem?.concessionItems!!, foodItem)
            foodCartAdapter?.notifyDataSetChanged()
            foodAdapter?.notifyDataSetChanged()
            if (foodItem.foodType.uppercase() == "INDIVIDUAL") {

                foodAdapter?.loadNewData(tabItem?.concessionItems!!)
//                Toast.makeText(this, "hello 44", Toast.LENGTH_SHORT).show()

            } else {

                updateSelectedList(foodItem, 3)
//                Toast.makeText(this, "hello 33", Toast.LENGTH_SHORT).show()
            }
        }

        itemCartPrice -= getCartFoodPrice()
        itemSetCartPrice = Constant.DECIFORMAT.format(itemCheckPrice / 100.0)
        tvFoodPrice?.text = getAllFoodPrice()
        if (foodCartListNew?.size == 0) {
            if (mFoodCartDialog?.isShowing == true)
                mFoodCartDialog?.dismiss()
            Toast.makeText(this, "hello 55", Toast.LENGTH_SHORT).show()
        }
    }

    private fun calculateTotal(): Int {
        var totalPrice = 0
        for (data in foodCartListNew!!) {
            totalPrice = data.itemPrice.toInt() * data.foodQuan
        }
        return totalPrice
    }

    private fun updateSelectedItem(
        isAdd: Boolean,
        position: Int,
        concessionItems: ArrayList<GetFoodResponse.ConcessionItem>,
        foodItem: GetFoodResponse.FoodDtls
    ) {
        if (isAdd) {
            if (foodItem.foodType.uppercase() == "INDIVIDUAL") {
                for (item in concessionItems) {
                    if (item.id == foodItem.foodId) {
                        item.quantity = foodItem.foodQuan
                    }
                }
            }
            //foodCartListNew?.get(position)?.foodQuan!! + 1
            totalFoodAmt += foodCartListNew?.get(position)?.itemPrice!!
            updateCartPrice()
        } else {
            if (foodItem.foodType.uppercase() == "INDIVIDUAL") {
                for (item in concessionItems) {
                    if (item.id == foodItem.foodId) {
                        item.quantity = foodItem.foodQuan
                    }
                }
            }
            //foodCartListNew?.get(position)?.foodQuan!! - 1

            updateCartPrice()
            totalFoodAmt -= foodCartListNew?.get(position)?.itemPrice!!
            if (foodCartListNew?.get(position)?.foodQuan == 0) {
                foodCartListNew?.removeAt(position)
            }
        }
        if (isAdd) {
            // foodCartListNew?.get(position)?.foodQuan = (foodCartListNew?.get(position)?.foodQuan!! + 1)
        } else {
            try {
                //foodCartListNew?.get(position)?.foodQuan = (foodCartListNew?.get(position)?.foodQuan!! - 1)
                if (foodCartListNew?.get(position)?.foodQuan == 0) {
                    foodCartListNew?.removeAt(position)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (totalFoodAmt < 0) totalFoodAmt = 0.0

        if (foodCartListNew?.size!! > 0) {
            binding?.textCartCountNotiication?.show()
            binding?.textCartCountNotiication?.text = foodCartListNew?.size.toString()

            binding?.txtProceed?.show()
            binding?.txtSkipBtn?.hide()
        } else {
            emptyCart?.show()
            binding?.textCartCountNotiication?.invisible()

            if (booktype == "FOOD"){
                binding?.txtProceed?.show()
                binding?.txtSkipBtn?.hide()
            }else{
                binding?.txtProceed?.hide()
                binding?.txtSkipBtn?.show()
            }

        }

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onRemoveCart(foodItem: GetFoodResponse.FoodDtls, pos: Int) {

        totalFoodAmt -= (foodCartListNew?.get(pos)?.foodQuan!! * foodCartListNew?.get(pos)?.itemPrice!!)

        if (pos <= (foodCartList?.size!! - 1))
            foodCartList?.removeAt(pos)

        if (pos <= (foodCartListNew?.size!! - 1))
            foodCartListNew?.removeAt(pos)

        if (totalFoodAmt < 0) totalFoodAmt = 0.0

        if (foodCartListNew?.size!! > 0) {

            binding?.textCartCountNotiication?.show()
            binding?.textCartCountNotiication?.text = foodCartListNew?.size.toString()

            binding?.txtProceed?.show()
            binding?.txtSkipBtn?.hide()

            if (mFoodCartDialog?.isShowing == true) {
                mFoodCartDialog?.dismiss()

                foodAdapter?.loadNewData(foodSelectedList!!)
                foodAdapter?.notifyDataSetChanged()
            }


            for (item in foodItemRemove) {
                println("item.foodtypeCase------->${item.foodtype.uppercase()}")

                if (item.foodtype.uppercase() == "INDIVIDUAL") {

                    foodAdapter?.loadNewData(tabItem?.concessionItems!!)
                    foodAdapter?.notifyDataSetChanged()

//                    Toast.makeText(this, "hello 00", Toast.LENGTH_SHORT).show()

                } else {
//                    Toast.makeText(this, "hello 11", Toast.LENGTH_SHORT).show()

                    if (mFoodCartDialog?.isShowing == true)
                        mFoodCartDialog?.dismiss()

                }

            }


        } else {

            if (pos <= (foodCartList?.size!! -1))
                foodCartList?.removeAt(pos)

            if (pos <= (foodCartListNew?.size!! -1))
                foodCartListNew?.removeAt(pos)

            for (item in foodItemRemove) {

                println("foodtypeCaseValues------->${item.foodtype.uppercase()}")

                if (item.foodtype.uppercase() == "INDIVIDUAL") {
                    if (mFoodCartDialog?.isShowing == true)
                        mFoodCartDialog?.dismiss()

                    foodAdapter?.loadNewData(tabItem?.concessionItems!!)
                    foodAdapter?.notifyDataSetChanged()
//                    Toast.makeText(this, "hello 12", Toast.LENGTH_SHORT).show()
                } else {
//                    Toast.makeText(this, "hello 21", Toast.LENGTH_SHORT).show()

                    if (mFoodCartDialog?.isShowing == true)
                        mFoodCartDialog?.dismiss()

                    foodAdapter?.loadNewData(tabItem?.concessionItems!!)
                    foodAdapter?.notifyDataSetChanged()
                }

            }

            binding?.textCartCountNotiication?.text = "0"
            binding?.textCartCountNotiication?.show()

            if (booktype == "FOOD") {
                binding?.txtProceed?.show()
                binding?.txtSkipBtn?.hide()
            } else {
                binding?.txtProceed?.hide()
                binding?.txtSkipBtn?.show()
            }


        }

        updateSelectedList(foodItem, 0)
        itemCartPrice -= getCartFoodPrice()
        itemSetCartPrice = Constant.DECIFORMAT.format(itemCheckPrice / 100.0)
        tvFoodPrice?.text = getAllFoodPrice()
        if (foodCartListNew?.size == 0) {
            if (mFoodCartDialog?.isShowing == true)
                mFoodCartDialog?.dismiss()
        }

    }

    private fun updateSelectedList(foodItem: GetFoodResponse.FoodDtls?, type: Int) {
        println("updateSelectedList----$type")
        for (item in foodSelectedList!!) {
            if (type == 0) {
                if (item.id == foodItem?.foodId) {
                    item.quantityUpdate = item.quantityUpdate - foodItem.foodQuan
                    break
                }

            } else if (type == 2) {
                if (item.id == foodItem?.foodId) {
                    item.quantityUpdate = item.quantityUpdate + 1
                    break
                }
            } else if (type == 3) {
                if (item.id == foodItem?.foodId) {
                    item.quantityUpdate = item.quantityUpdate - 1
                    break
                }
            } else {
                item.quantityUpdate = 0
            }
        }
        foodAdapter?.notifyDataSetChanged()
    }

    private fun updateCartList(foodItem: GetFoodResponse.ConcessionItem) {
        val foodRequestData = SaveFoodRequest.ConcessionFood()
//        foodRequestData.id = foodItem.id

        if (foodItem.quantity > 0) {
            println("amtSend--->${foodItem.priceInCents}")
            foodRequestData.priceInCents = foodItem.priceInCents.toString()
            foodRequestData.itemId = foodItem.id
            foodRequestData.id = foodItem.id
            foodRequestData.quantity = foodItem.quantity
            foodRequestData.itemType = foodItem.foodtype
            foodRequestData.itemPrice = foodItem.itemPrice
            foodRequestData.description = foodItem.description
            foodRequestData.itemImageUrl = foodItem.itemImageUrl
            foodRequestData.descriptionAlt = foodItem.descriptionAlt

            val arrModify = ArrayList<SaveFoodRequest.Modifier>()
            var modifiersName = ""

            for (k in foodItem.alternateItems.indices) {
                if (foodItem.alternateItems[k].checkFlag) {
                    foodRequestData.itemId = foodItem.alternateItems[k].id
                    foodRequestData.itemImageUrl = foodItem.itemImageUrl
                    foodRequestData.itemPrice = foodItem.alternateItems[k].itemPrice
                    foodRequestData.headOfficeItemCode =
                        foodItem.alternateItems[k].headOfficeItemCode
                    foodRequestData.description =
                        foodItem.description + " ( " + foodItem.alternateItems[k].description + " ) "
                    foodRequestData.descriptionAlt =
                        foodItem.description + " ( " + foodItem.alternateItems[k].descriptionAlt.toString() + " ) "
                    println("foodItem.alternateItems---" + foodRequestData.description)
                    for (l in foodItem.alternateItems[k].modifierGroups.indices) {
                        for (m in foodItem.alternateItems[k].modifierGroups[l].Modifiers.indices) {
                            val modifyModel = SaveFoodRequest.Modifier()
                            if (foodItem.alternateItems[k].modifierGroups[l].Modifiers[m].checkFlag) {
                                modifyModel.quantity = 0
                                modifyModel.modifierItemId =
                                    foodItem.alternateItems.get(k).modifierGroups.get(l).Modifiers[m].id
                                arrModify.add(modifyModel)
                                foodRequestData.modifiers = arrModify
                                modifiersName =
                                    foodItem.alternateItems.get(k).description + ", " + foodItem.alternateItems[k].modifierGroups[l].Modifiers[m].description
                            }
                        }
                    }
                    foodRequestData.ModifiersText = modifiersName
                }
            }

            if (foodItem.foodtype.uppercase() == "INDIVIDUAL") {
                println("CheckFoodType--->${foodItem.foodtype.uppercase()}")

                var modifiers = ""
                foodRequestData.headOfficeItemCode = foodItem.headOfficeItemCode
                foodRequestData.description = foodItem.description
                foodRequestData.itemImageUrl = foodItem.itemImageUrl
                foodRequestData.descriptionAlt = foodItem.descriptionAlt
                for (l in foodItem.modifierGroups.indices) {
                    try {
                        foodRequestData.description = foodItem.description
                        foodRequestData.itemImageUrl = foodItem.itemImageUrl
                        foodRequestData.descriptionAlt =
                            foodItem.descriptionAlt
                        for (m in foodItem.modifierGroups[l].Modifiers.indices) {
                            val modifyModel = SaveFoodRequest.Modifier()
                            if (foodItem.modifierGroups[l].Modifiers[m].checkFlag) {

                                modifyModel.quantity = 0
                                modifyModel.modifierItemId =
                                    foodItem.modifierGroups[l].Modifiers[m].id
                                arrModify.add(modifyModel)
                                foodRequestData.modifiers = arrModify
                                modifiers =
                                    foodItem.modifierGroups[l].Description + ", " + foodItem.modifierGroups[l].Modifiers[m].description
                            }
                        }
                    } catch (e: Exception) {
                        println("SingleAddFoodException--->${e.message} ")
                    }

                }
                foodRequestData.ModifiersText = modifiers
            }

//            /*todo combo items add*/

            if (foodItem.packageChildItems.isNotEmpty()) {
                foodRequestData.headOfficeItemCode = foodItem.headOfficeItemCode
                val arrPackage = ArrayList<SaveFoodRequest.Item>()
                for (n in 0 until foodItem.packageChildItems.size) {
                    foodRequestData.description = foodItem.packageChildItems[n].description
                    foodRequestData.itemImageUrl = foodItem.itemImageUrl
                    foodRequestData.descriptionAlt =
                        foodItem.packageChildItems[n].descriptionAlt.toString()
                    try {
                        for (o in 0 until foodItem.packageChildItems[n].alternateItems.size) {
                            val packageModel = SaveFoodRequest.Item()
                            if (foodItem.packageChildItems[n].alternateItems[o].checkFlag) {
                                packageModel.parentId = foodItem.packageChildItems[n].id
                                packageModel.description =
                                    foodItem.packageChildItems[n].alternateItems[o].description
                                packageModel.descriptionAlt =
                                    foodItem.packageChildItems[n].alternateItems[o].descriptionAlt
                                packageModel.itemId =
                                    foodItem.packageChildItems[n].alternateItems[o].id
                                arrPackage.add(packageModel)
                                foodRequestData.items = arrPackage
                            }
                        }
                    } catch (e: Exception) {
                        println("ExceptionFood--->${e.message}")
                        e.printStackTrace()
                    }
                }
            }

            if (foodCartList?.size!! > 0) {
                if (itemExistCartList(foodRequestData)) {
                    for (item in foodCartList!!) {
                        if (item.id == foodRequestData.id) {
                            if (foodRequestData.itemType == "Individual") {
                                item.quantity = foodRequestData.quantity
                            } else {
                                item.quantity = item.quantity + foodRequestData.quantity
                            }
                            println("item.quantity---" + foodRequestData.itemType)
                            break
                        }
                    }
                } else {
                    foodCartList?.add(foodRequestData)
                }


            } else {
                foodCartList?.add(foodRequestData)
            }

            if (foodCartList?.size!! > 0) {
                val arrFoodDetails = foodCartList?.distinctBy {
                    Triple(
                        it.description,
                        it.description,
                        it.ModifiersText
                    )
                }

                for (j in arrFoodDetails?.indices!!) {
                    var quantity = 0
                    var amount = 0.0
                    for (i in 0 until foodCartList?.size!!) {
                        if (foodCartList!![i].description == arrFoodDetails[j].description) {
                            if (arrFoodDetails[j].ModifiersText.isNotEmpty()) {
                                if (foodCartList!![i].ModifiersText == arrFoodDetails[j].ModifiersText) {
                                    quantity += foodCartList!![i].quantity.toInt()
                                    amount = arrFoodDetails[j].priceInCents.toDouble()
                                }
                            } else if (arrFoodDetails[j].description.isNotEmpty()) {
                                if (foodCartList!![i].description == arrFoodDetails[j].description) {
                                    quantity += foodCartList!![i].quantity.toInt()
                                    amount = arrFoodDetails[j].priceInCents.toDouble()
                                }
                            } else {
                                quantity += foodCartList!![i].quantity.toInt()
                                amount = arrFoodDetails[j].priceInCents.toDouble()
                            }
                        }
                    }
                    arrFoodDetails[j].quantity = quantity
                    arrFoodDetails[j].finalPriceInCents = amount.toString()
                }

                foodCartList = ArrayList(arrFoodDetails)
            }

            val foodDtls = GetFoodResponse.FoodDtls()
            var foodSubName = ""
            var foodName = ""
            if (foodItem.quantity > 0) {

                foodDtls.foodAmount = foodItem.itemTotal.toDouble()
                foodDtls.foodId = foodItem.id
                foodDtls.foodQuan = foodItem.quantity
                foodDtls.foodType = foodItem.foodtype
                foodDtls.foodUrl = foodItem.itemImageUrl
//                foodDtls.title = foodItem.title
                foodDtls.title = foodItem.title
                foodName = foodItem.description
                foodDtls.itemPrice = foodItem.priceInCents.toDouble()
                for (k in foodItem.alternateItems.indices) {
                    if (foodItem.alternateItems[k].checkFlag) {
                        foodDtls.itemPrice = foodItem.alternateItems[k].priceInCents.toDouble()
                        foodSubName = foodItem.alternateItems[k].description
                        for (l in foodItem.alternateItems[k].modifierGroups.indices) {
                            // foodSubName = foodSubName + " + " + concessionItems.alternateItems.get(k).modifierGroups.get(l).description
                            for (m in foodItem.alternateItems[k].modifierGroups[l].Modifiers.indices) {
                                if (foodItem.alternateItems[k].modifierGroups[l].Modifiers[m].checkFlag) {
                                    foodSubName =
                                        foodSubName + ", " + foodItem.alternateItems[k].modifierGroups[l].Modifiers[m].description
                                    foodDtls.foodModifierId =
                                        foodItem.alternateItems[k].modifierGroups[l].Modifiers[m].id
                                }
                            }
                        }
                    }
                }

                if (!foodItem.packageChildItems.isNullOrEmpty()) {
                    for (n in 0 until foodItem.packageChildItems.size) {
                        for (o in 0 until foodItem.packageChildItems[n].alternateItems.size) {
                            if (foodItem.packageChildItems[n].alternateItems[o].checkFlag) {
                                println("foodCartListNewdata--->${foodItem.packageChildItems[n].alternateItems[o].description}---")
                                foodSubName =
                                    foodSubName + ", " + foodItem.packageChildItems[n].alternateItems[o].description

                            }
                        }
                    }
                }
                if (foodSubName.isEmpty()) {
                    foodDtls.foodName = tabItem?.name!!
                    foodDtls.foodModifiers = foodName
                } else {
                    foodDtls.foodName = foodName
                    foodDtls.foodModifiers = foodSubName
                }

                if (foodCartListNew?.size!! > 0) {
                    if (itemExist(foodDtls)) {
                        for (item in foodCartListNew!!) {
                            if (item.foodModifiers == foodDtls.foodModifiers && item.foodId == foodDtls.foodId) {
                                if (foodDtls.foodType == "Individual") {
                                    item.foodQuan = foodDtls.foodQuan
                                } else {
                                    println("foodQtCheck--->${item.foodQuan}---${foodDtls.foodQuan}")
                                    item.foodQuan = item.foodQuan + foodDtls.foodQuan
                                }
                                break
                            }
                        }
                    } else {
                        foodCartListNew?.add(foodDtls)
                    }

                } else {
                    foodCartListNew?.add(foodDtls)
                }
                //foodCartListNew?.add(foodDtls)
            }

        } else {
            for (i in foodCartListNew?.indices!!) {
                try {
                    if (foodItem.id == foodCartListNew!![i].foodId) {
                        foodCartListNew?.removeAt(i)
                    }
                } catch (e: Exception) {
                    println("SingleAddFoodRemoveException--->${e.message} ")
                }
            }
            for (i in foodCartList?.indices!!) {
                try {
                    if (foodItem.id == foodCartList!![i].id) {
                        foodCartList?.removeAt(i)
                    }
                } catch (e: Exception) {
                    println("SingleAddFoodExceptionTwo--->${e.message} ")
                }
            }
        }

        totalFoodAmt = 0.0
        if (foodCartListNew?.size!! > 0) {
            val arrFoodDetails =
                foodCartListNew?.distinctBy { Pair(it.foodName, it.foodModifiers) }
            for (j in arrFoodDetails?.indices!!) {
                var quantity = 0
                var amount = 0.0
                for (i in 0 until foodCartListNew?.size!!) {
                    if (foodCartListNew!![i].foodName == arrFoodDetails[j].foodName) {
                        if (arrFoodDetails[j].foodModifiers != null) {
                            if (foodCartListNew!![i].foodModifiers == arrFoodDetails[j].foodModifiers) {
                                quantity = foodCartListNew!![i].foodQuan
                                amount = quantity * arrFoodDetails[j].itemPrice
                            }
                        }
                    }
                }
                arrFoodDetails[j].foodQuan = quantity
                arrFoodDetails[j].foodAmount = amount
                totalFoodAmt += arrFoodDetails[j].foodAmount
            }


            foodCartListNew = ArrayList(arrFoodDetails)
        }

        if (foodCartListNew?.size!! > 0) {
            binding?.textCartCountNotiication?.show()
            binding?.textCartCountNotiication?.text = foodCartListNew?.size.toString()

            binding?.txtProceed?.show()
            binding?.txtSkipBtn?.hide()
        } else {
            binding?.textCartCountNotiication?.text = "0"
            binding?.textCartCountNotiication?.show()

            binding?.txtProceed?.hide()
            binding?.txtSkipBtn?.show()
        }

        foodCartAdapter?.loadNewData(foodCartListNew!!)
        itemCartPrice = getCartFoodPrice()
        itemSetCartPrice = Constant.DECIFORMAT.format(itemCheckPrice / 100.0)
        tvFoodPrice?.text = getAllFoodPrice()
        println("foodCartListNew--->" + foodCartList?.size)
    }

    private fun itemExistCartList(foodCartListC: SaveFoodRequest.ConcessionFood): Boolean {
        for (item in foodCartList!!) {
            if (item.description == foodCartListC.description && item.id == foodCartListC.id) {
                return true
            }
        }
        return false
    }

    private fun itemExist(foodDtls: GetFoodResponse.FoodDtls): Boolean {
        for (item in foodCartListNew!!) {
            if (item.foodModifiers == foodDtls.foodModifiers && item.foodId == foodDtls.foodId) {
                return true
            }
        }
        return false
    }

    @SuppressLint("SetTextI18n")
    override fun onAlternateClick(
        item: GetFoodResponse.AlternateItem,
        foodItem: GetFoodResponse.ConcessionItem,
        foodPos: Int
    ) {

        textNumber?.text = "1"
        textIncrease?.isClickable = true
        textIncrease?.isEnabled = true

        tvDoneBtn?.isClickable = true
        tvDoneBtn?.isEnabled = true
        tvDoneBtn?.background = ContextCompat.getDrawable(this, R.drawable.btn_corner_bg_red)

        textDecrease?.isClickable = true
        textDecrease?.isEnabled = true
        foodAlterDis = item.description
        foodAlterId = item.id
        val amt = item.itemPrice
        item.checkFlag = true
        item.subItemCount = 1
        item.itemTotal = amt
        foodItem.itemTotal = item.priceInCents
        foodItem.quantity = textNumber?.text?.toString()?.toInt()!!
        tvKdTotal?.text = amt

    }

    override fun onComboClick(
        item: GetFoodResponse.ComboItem,
        position: Int,
        foodItem: GetFoodResponse.ConcessionItem,
        foodPos: Int
    ) {

        if (position == 0) {
            check = false
        }

        if (position == 1) {
            check1 = false
        }

        if (check1 == check) {
            check = true
            check1 = true
            println("onComboClickPosition---->${position}")
            textNumber?.text = "1"
            tvKdTotal?.text = foodItem.itemPrice
            textIncrease?.isClickable = true

            textIncrease?.isEnabled = true
            textDecrease?.isClickable = true
            textDecrease?.isEnabled = true

            tvDoneBtn?.isClickable = true
            tvDoneBtn?.isEnabled = true
            tvDoneBtn?.background = ContextCompat.getDrawable(this, R.drawable.btn_corner_bg_red)

            foodItem.quantity = 1
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onAddIndiVidual(foodItem: GetFoodResponse.ConcessionItem, position: Int) {
        var amount = 0.0
        val a = foodItem.quantity + 1
        foodItem.quantity = a
        amount = (foodItem.quantity * foodItem.priceInCents).toDouble()
        foodItem.itemTotal = amount.toInt()
        updateCartList(foodItem)
        if (foodCartListNew?.size!! > 0) {

            binding?.textCartCountNotiication?.show()
            binding?.textCartCountNotiication?.text = foodCartListNew?.size.toString()
            binding?.txtProceed?.show()
            binding?.txtSkipBtn?.hide()
        } else {

            binding?.textCartCountNotiication?.text = "0"
            binding?.textCartCountNotiication?.show()

            binding?.txtProceed?.hide()
            binding?.txtSkipBtn?.show()
        }
        individualAdapter?.notifyDataSetChanged()
    }

    override fun onDecreaseIndiVidual(foodItem: GetFoodResponse.ConcessionItem, position: Int) {

        var amount = 0.0
        if (foodItem.quantity > 0) {
            foodItem.quantity = foodItem.quantity - 1
            amount = (foodItem.quantity * foodItem.priceInCents).toDouble()
            foodItem.itemTotal = amount.toInt()
            updateCartList(foodItem)
            foodSelectedList?.removeAt(position)
            foodSelectedList?.add(position, foodItem)
            individualAdapter?.loadNewData(foodSelectedList!!)
        }
        if (foodCartListNew?.size!! > 0) {
            binding?.textCartCountNotiication?.show()
            binding?.textCartCountNotiication?.text = foodCartListNew?.size.toString()

            binding?.txtProceed?.show()
            binding?.txtSkipBtn?.hide()
        } else {
            binding?.textCartCountNotiication?.text = "0"
            binding?.textCartCountNotiication?.show()

            binding?.txtProceed?.hide()
            binding?.txtSkipBtn?.show()
        }

    }


    override fun onIncreaseIndiVidual(foodItem: GetFoodResponse.ConcessionItem, position: Int) {
        var amount = 0.0
        if (foodItem.quantity < 20) {
            foodItem.quantity = foodItem.quantity + 1
            println("CheckQt---->${foodItem.quantity}")
            amount = (foodItem.quantity * foodItem.priceInCents).toDouble()
            foodItem.itemTotal = amount.toInt()
            foodSelectedList?.removeAt(position)
            foodSelectedList?.add(position, foodItem)
            updateCartList(foodItem)
            individualAdapter?.loadNewData(foodSelectedList!!)
            println("CheckSize--->${foodCartListNew!![0].foodQuan}")
        } else {
            toast("add less than 20 items")
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        Constant.IntentKey.TimerExtandCheck = true
        TimerExtand = 90
        TimerTime = 360
        ON_BACK_FOOD = 1
        cancelTrans(CancelTransRequest("", transId))
    }

    private fun cancelTrans(cancelTransRequest: CancelTransRequest) {
        foodViewModel.cancelTrans(cancelTransRequest)
            .observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            loader?.dismiss()
                            resource.data?.let { it ->
                                if (it.data?.result == Constant.status && it.data.code == Constant.SUCCESS_CODE) {
                                    try {

                                    } catch (e: Exception) {
                                        println("updateUiCinemaSession ---> ${e.message}")
                                    }
                                } else {
                                    loader?.dismiss()
                                    val dialog = OptionDialog(this,
                                        R.mipmap.ic_launcher,
                                        R.string.app_name,
                                        it.data?.msg.toString(),
                                        positiveBtnText = R.string.ok,
                                        negativeBtnText = R.string.no,
                                        positiveClick = {
                                        },
                                        negativeClick = {
                                        })
                                    dialog.show()
                                }

                            }
                        }
                        Status.ERROR -> {
                            loader?.dismiss()
                            val dialog = OptionDialog(this,
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                it.message.toString(),
                                positiveBtnText = R.string.ok,
                                negativeBtnText = R.string.no,
                                positiveClick = {
                                },
                                negativeClick = {
                                })
                            dialog.show()
                        }
                        Status.LOADING -> {
                        }
                    }
                }
            }
    }

    private fun updateCartPrice() {
        if (booktype == "FOOD") {
            textTotal1?.text = getString(R.string.price_kd) + " ${
                Constant.DECIFORMAT.format(
                    getAllFoodPrice().replace("KWD ", "").toDouble()
                )
            }"
        } else {

            try {
                textTotal1?.text = getString(R.string.price_kd) + " ${
                    Constant.DECIFORMAT.format(
                        getAllFoodPrice().replace("KWD ", "").toDouble() + seatPrice.replace(
                            "KWD ", ""
                        ).toDouble()
                    )
                }"

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }


}