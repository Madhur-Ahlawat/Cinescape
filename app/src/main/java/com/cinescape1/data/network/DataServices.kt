package com.cinescape1.data.network

import com.cinescape1.data.models.requestModel.*
import com.cinescape1.data.models.responseModel.*
import com.cinescape1.ui.main.views.home.fragments.account.response.GetAmountResponse
import com.cinescape1.ui.main.views.home.fragments.account.response.RechargeAmountResponse
import com.cinescape1.ui.main.views.payment.paymentFaield.reponse.PaymentFailedResponse
import com.cinescape1.ui.main.views.splash.response.SplashResponse
import com.cinescape1.ui.main.views.summery.response.GiftCardResponse
import com.cinescape1.utils.Constant
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import retrofit2.http.Headers
import java.util.concurrent.TimeUnit


interface DataServices {
    @Headers(
        "Content-Type: application/json",
        "Accept: application/json"
    )

    @POST("content/splashtxt")
    suspend fun getSplashText(): Response<SplashResponse>

    @POST("more/offer/detail")
    suspend fun offerDetails(@Query("id") idCheck:String): Response<OfferDetailsResponse>

    @POST("more/offer")
    suspend fun offer(): Response<OfferResponse>

    @POST("customer/login")
    suspend fun userLogin(@Body request: LoginRequest): Response<LoginResponse>

    @POST("customer/register")
    suspend fun userRegister(@Body request: RegisterRequest): Response<SignupResponse>

    @POST("content/home")
    suspend fun getHomeData(): Response<HomeDataResponse>

    @POST("content/movies")
    suspend fun getMoviesData(@Body movieRequest: MovieRequest): Response<MoviesResponse>

    @POST("content/comingsoon")
    suspend fun getComingSoon(): Response<HomeDataResponse>

    @POST("content/nowshowing")
    suspend fun getNowShowing(): Response<NowShowingResponse>

    @POST("content/msessionsnew")
    suspend fun getMsessionsNew(@Body request: CinemaSessionRequest): Response<CinemaSessionResponse>

    @POST("content/getmovie")
    suspend fun movieDetails (@Query("mid") movieId: String): Response<GetMovieResponse>

    @POST("content/csessions")
    suspend fun getCinemaData(@Body request: CinemaSessionRequest): Response<CSessionResponse>

    @POST("content/getmovie")
    suspend fun getComingSoonMovieInfo(@Query("mid") movieId: String): Response<MovieDetailResponse>

    @POST("content/trans/seatlayout")
    suspend fun getSeatLayout(@Body request: SeatLayoutRequest): Response<SeatLayoutResponse>

    @POST("content/trans/reserveseats")
    suspend fun reserveSeat(@Body request: ReserveSeatRequest): Response<ReserveSeatResponse>

    @POST("content/trans/tcksummary")
    suspend fun tckSummary(@Body request: TicketSummaryRequest): Response<TicketSummaryResponse>

    @POST("content/trans/cancel")
    suspend fun cancelTrans(@Body request: CancelTransRequest): Response<TicketSummaryResponse>

    @POST("payment/knet/hmac")
    suspend fun paymentKnetHmac(@Body request: HmacKnetRequest): Response<HmacKnetResponse>

    @POST("clubcard/clubPayment")
    suspend fun paymentWallet(@Body request: HmacKnetRequest): Response<WalletResponse>

    @POST("giftcard/apply")
    suspend fun giftCardApply(@Body request: GiftCardRequest): Response<GiftCardResponse>

    @POST("voucher/payment/apply")
    suspend fun voucherApply(@Body request: GiftCardRequest): Response<GiftCardResponse>

    @POST("payment/cybersource/initiate")
    suspend fun creditCardInit(@Body request: HmacKnetRequest): Response<PaymentTokenResponse>

    @POST("payment/cybersource/payerauth")
    suspend fun postCardData(@Body request: PostCardRequest): Response<PostCardResponse>

    @POST("payment/cybersource/payerresponse")
    suspend fun validateJWT(@Body request: ValidateJWTRequest): Response<ValidateResponse>

    @POST("content/food/getfood")
    suspend fun getFood(@Body request: GetFoodRequest): Response<GetFoodResponse>

    @POST("content/food/addconcession")
    suspend fun saveFood(@Body request: SaveFoodRequest): Response<GetFoodResponse>

    @POST("content/trans/tckbooked")
    suspend fun tckBooked(@Body request: FinalTicketRequest): Response<TicketSummaryResponse>

    @POST("content/trans/tckfailed")
    suspend fun tckFailed(@Body request: FinalTicketRequest): Response<PaymentFailedResponse>


    @POST("history/mybookings")
    suspend fun myBooking(@Body request: MyBookingRequest): Response<HistoryResponse>


    @POST("history/myticketsingle")
    suspend fun myTicketSingle(@Body request: MySingleTicketRequest): Response<TicketSummaryResponse>

    @POST("trans/cancelbooking")
    suspend fun cancelReservation(@Body finalTicketRequest: FinalTicketRequest): Response<ContactUsResponse>

    @POST("history/resend")
    suspend fun resendMail(@Body resendRequest: ResendRequest): Response<ContactUsResponse>

    @POST("history/nextbookings")
    suspend fun nextBookings(@Body request: NextBookingsRequest): Response<NextBookingResponse>

    @POST("clubcard/addRechargeCard")
    suspend fun addRechargeCard(@Body request: AddClubRechargeRequest): Response<RechargeCardResponse>

    @GET("content/cinemas")
    suspend fun foodResponse(): Response<FoodResponse>

    @GET("customer/getcountry?id=O")
    suspend fun countryCode(): Response<CountryCodeResponse>

    @POST("customer/updateprofile")
    suspend fun updateAccount(@Body request: UpdateAccountRequest): Response<UpdateAccountResponse>

    @GET("clubcard/getamounts")
    suspend fun getAmount(): Response<RechargeAmountResponse>

    @POST("more/tabs")
    suspend fun moreTabs(): Response<MoreTabResponse>

    @POST("customer/getprofile")
    suspend fun getProfile(@Body request: ProfileRequest): Response<ProfileResponse>

    @Multipart
    @POST("more/contactus")
    suspend fun contactUs(
        @Part ("email") email: RequestBody,
        @Part ("name") name: RequestBody,
        @Part ("mobile") mobile: RequestBody,
        @Part ("msg") msg: RequestBody,
        @Part file: MultipartBody.Part
    ): Response<ContactUsResponse>

    @POST("customer/setpref")
    suspend fun preference(@Body request: PreferenceRequest): Response<PrefrenceResponse>

    @POST("customer/guestlogin")
    suspend fun continueGuest(@Body request: GuestRequest): Response<ContinueGuestResponse>

    @POST("customer/mverify")
    suspend fun otpVerify(@Body request: OtpVerifyRequest): Response<ContinueGuestResponse>

    @POST("customer/forgotpassword")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): Response<ForgotOtpsendResponse>

    @POST("customer/updatepassword")
    suspend fun updatePassword(@Body request: UpdatePasswordRequest): Response<ForgotOtpVerifyResponse>

    @POST("customer/activatecard")
    suspend fun activateCard(@Body request: ActivatwWalletRequest): Response<ForgotOtpVerifyResponse>

    companion object {
        operator fun invoke(string: String?,lanuage:String): DataServices {
            println("LanguageString::=> ${lanuage}")
//            val baseUrl = "http://192.168.1.7:8084/api/"
//            val baseUrl = "https://cinescapeapi.wemonde.co/api/"
            val baseUrl = "https://uatapi.cinescape.com.kw/api/"
            val client = OkHttpClient.Builder()
            client.readTimeout(600, TimeUnit.SECONDS)
            client.connectTimeout(600, TimeUnit.SECONDS)
                client.addInterceptor(Interceptor { chain ->
                    val newRequest: Request = chain.request().newBuilder()
                        .addHeader("Authorization", string.toString())
                        .addHeader("appversion", Constant.version)
                        .addHeader("platform", Constant.platform)
                        .addHeader("Accept-Language", lanuage)
                        .addHeader("Content-Type", "application/json")
                        .build()
                    chain.proceed(newRequest)
                })

           // if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                client.addInterceptor(logging)
           // }

            return Retrofit.Builder()
                .client(client.build())
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(DataServices::class.java)
        }
    }
}

