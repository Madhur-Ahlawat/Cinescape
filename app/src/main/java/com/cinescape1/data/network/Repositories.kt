package com.cinescape1.data.network

import com.cinescape1.data.models.requestModel.*
import com.cinescape1.data.models.responseModel.*
import com.cinescape1.ui.main.views.home.fragments.account.response.RechargeAmountResponse
import com.cinescape1.ui.main.views.payment.paymentFaield.reponse.PaymentFailedResponse
import com.cinescape1.ui.main.views.payment.paymentList.BankOfferRequest
import com.cinescape1.ui.main.views.payment.paymentList.response.BankOfferApply
import com.cinescape1.ui.main.views.payment.paymentList.response.GiftCardRemove
import com.cinescape1.ui.main.views.payment.paymentList.response.OfferRemove
import com.cinescape1.ui.main.views.payment.paymentList.response.PaymentListResponse
import com.cinescape1.ui.main.views.splash.response.SplashResponse
import com.cinescape1.ui.main.views.summery.response.GiftCardResponse
import okhttp3.MultipartBody
import javax.inject.Inject

class Repositories @Inject constructor(private val api: DataServices) : SafeApiRequest() {

    suspend fun getSplashText(): Result<SplashResponse> {
        return apiRequest { api.getSplashText() }
    }

    suspend fun offerDetails(idCheck: String?): Result<OfferDetailsResponse> {
        return apiRequest { api.offerDetails(idCheck!!) }
    }

    suspend fun getNowShowing(): Result<NowShowingResponse> {
        return apiRequest { api.getNowShowing() }
    }

    suspend fun getHomeData(): Result<HomeDataResponse> {
        return apiRequest { api.getHomeData() }
    }

    suspend fun getMoviesData(movieRequest: MovieRequest): Result<MoviesResponse> {
        return apiRequest { api.getMoviesData(movieRequest) }
    }

    suspend fun getMsessionsNew(jsonRequest: CinemaSessionRequest): Result<CinemaSessionResponse> {
        return apiRequest { api.getMsessionsNew(jsonRequest) }
    }

    suspend fun movieDetails(movieId: String): Result<GetMovieResponse> {
        return apiRequest { api.movieDetails(movieId) }
    }

    suspend fun getCinemaData(jsonRequest: CinemaSessionRequest): Result<CSessionResponse> {
        return apiRequest { api.getCinemaData(jsonRequest) }
    }

    suspend fun getMovieDetail(mid: String = ""): Result<MovieDetailResponse> {
        return apiRequest { api.getComingSoonMovieInfo(mid) }
    }

    suspend fun userLogin(
        request: LoginRequest
    ): Result<LoginResponse> {
        return apiRequest { api.userLogin(request) }
    }

    suspend fun resisters(
        request: RegisterRequest
    ): Result<SignupResponse> {
        return apiRequest { api.userRegister(request) }
    }

    suspend fun getSeatLayout(
        request: SeatLayoutRequest
    ): Result<SeatLayoutResponse> {
        return apiRequest { api.getSeatLayout(request) }
    }

    suspend fun reserveSeat(
        request: ReserveSeatRequest
    ): Result<ReserveSeatResponse> {
        return apiRequest { api.reserveSeat(request) }
    }

    suspend fun getFood(
        request: GetFoodRequest
    ): Result<GetFoodResponse> {
        return apiRequest { api.getFood(request) }
    }

    suspend fun saveFood(
        request: SaveFoodRequest
    ): Result<GetFoodResponse> {
        return apiRequest { api.saveFood(request) }
    }

    suspend fun offer(): Result<OfferResponse> {
        return apiRequest { api.offer() }
    }

    suspend fun tckSummary(
        request: TicketSummaryRequest
    ): Result<TicketSummaryResponse> {
        return apiRequest { api.tckSummary(request) }
    }

    suspend fun paymentList(
        request: TicketSummaryRequest
    ): Result<PaymentListResponse> {
        return apiRequest { api.paymentList(request) }
    }

    suspend fun cancelTrans(
        request: CancelTransRequest
    ): Result<TicketSummaryResponse> {
        return apiRequest { api.cancelTrans(request) }
    }

    suspend fun paymentKnetHmac(
        request: HmacKnetRequest
    ): Result<HmacKnetResponse> {
        return apiRequest { api.paymentKnetHmac(request) }
    }

    suspend fun paymentWallet(
        request: HmacKnetRequest
    ): Result<WalletResponse> {
        return apiRequest { api.paymentWallet(request) }
    }

    suspend fun bnankApply(
        request: BankOfferRequest
    ): Result<BankOfferApply> {
        return apiRequest { api.bankApply(request) }
    }

    suspend fun bnankRemove(
        request: BankOfferRequest
    ): Result<OfferRemove> {
        return apiRequest { api.bankRemove(request) }
    }

    suspend fun giftCardRequest(
        request: GiftCardRequest
    ): Result<GiftCardResponse> {
        return apiRequest { api.giftCardApply(request) }
    }

    suspend fun giftCardRemove(
        request: GiftCardRequest
    ): Result<GiftCardRemove> {
        return apiRequest { api.giftCardRemove(request) }
    }

    suspend fun voucherApply(
        request: GiftCardRequest
    ): Result<GiftCardResponse> {
        return apiRequest { api.voucherApply(request) }
    }

    suspend fun creditCardInit(request: HmacKnetRequest): Result<PaymentTokenResponse> {
        return apiRequest { api.creditCardInit(request) }
    }

    suspend fun postCardData(request: PostCardRequest): Result<PostCardResponse> {
        return apiRequest { api.postCardData(request) }
    }

    suspend fun validateJWT(request: ValidateJWTRequest): Result<ValidateResponse> {
        return apiRequest { api.validateJWT(request) }
    }

    suspend fun tckBooked(request: FinalTicketRequest): Result<TicketSummaryResponse> {
        return apiRequest { api.tckBooked(request) }
    }

    suspend fun tckFailed(request: FinalTicketRequest): Result<PaymentFailedResponse> {
        return apiRequest { api.tckFailed(request) }
    }

    suspend fun myBooking(request: MyBookingRequest): Result<HistoryResponse> {
        return apiRequest { api.myBooking(request) }
    }


    suspend fun myTicketSingle(request: MySingleTicketRequest): Result<TicketSummaryResponse> {
        return apiRequest { api.myTicketSingle(request) }
    }

    suspend fun cancelReservation(finalTicketRequest: FinalTicketRequest): Result<ContactUsResponse> {
        return apiRequest { api.cancelReservation(finalTicketRequest) }
    }

    suspend fun resendMail(resendRequest: ResendRequest): Result<ContactUsResponse> {
        return apiRequest { api.resendMail(resendRequest) }
    }


    suspend fun nextBooking(request: NextBookingsRequest): Result<NextBookingResponse> {
        return apiRequest { api.nextBookings(request) }
    }

    suspend fun foodPickup(request: FoodPrepareRequest): Result<FoodPrepareResponse> {
        return apiRequest { api.pickupFood(request) }
    }

    suspend fun addClubRechargeCard(request: AddClubRechargeRequest): Result<RechargeCardResponse> {
        return apiRequest { api.addRechargeCard(request) }
    }


    suspend fun foodResponse(): Result<FoodResponse> {
        return apiRequest { api.foodResponse("Food") }
    }

    suspend fun countryCode(): Result<CountryCodeResponse> {
        return apiRequest { api.countryCode() }
    }

    suspend fun updateAccount(request: UpdateAccountRequest): Result<UpdateAccountResponse> {
        return apiRequest { api.updateAccount(request) }
    }

    suspend fun getAmount(): Result<RechargeAmountResponse> {
        return apiRequest { api.getAmount() }
    }

    suspend fun getProfile(request: ProfileRequest): Result<ProfileResponse> {
        return apiRequest { api.getProfile(request) }
    }

//    suspend fun contctUs(request: ContactUsRequest): Result<ContactUsResponse> {
//        return apiRequest { api.contactUs(request.email,request.name,request.mobile,request.msg,request.file) }
//    }

    suspend fun contctUs(
        email: String,
        name: String,
        mobile: String,
        msg: String,
        frontPhoto: MultipartBody.Part
    ): Result<ContactUsResponse> {
        return apiRequest {
            api.contactUs(
                email,
                name,
                mobile,
                msg,
                frontPhoto
            )
        }
    }

    suspend fun contctUsWithoutPhoto(
        email: String,
        name: String,
        mobile: String,
        msg: String
    ): Result<ContactUsResponse> {
        return apiRequest {
            api.contactUsWithoutPhoto(
                email,
                name,
                mobile,
                msg
            )
        }
    }


    suspend fun moreTabs(): Result<MoreTabResponse> {
        return apiRequest { api.moreTabs() }
    }

    suspend fun preference(request: PreferenceRequest): Result<PrefrenceResponse> {
        return apiRequest { api.preference(request) }
    }

    suspend fun continueGuest(request: GuestRequest): Result<ContinueGuestResponse> {
        return apiRequest { api.continueGuest(request) }
    }

    suspend fun otpVirfy(request: OtpVerifyRequest): Result<ContinueGuestResponse> {
        return apiRequest { api.otpVerify(request) }
    }

    suspend fun forgotPassword(request: ForgotPasswordRequest): Result<ForgotOtpsendResponse> {
        return apiRequest { api.forgotPassword(request) }
    }

    suspend fun updatePassword(request: UpdatePasswordRequest): Result<ForgotOtpVerifyResponse> {
        return apiRequest { api.updatePassword(request) }
    }

    suspend fun changePassword(request: ChangePasswordRequest): Result<ChangePasswordResponses> {
        return apiRequest { api.changePassword(request) }
    }

    suspend fun activateCard(request: ActivatwWalletRequest): Result<ForgotOtpVerifyResponse> {
        return apiRequest { api.activateCard(request) }
    }
}