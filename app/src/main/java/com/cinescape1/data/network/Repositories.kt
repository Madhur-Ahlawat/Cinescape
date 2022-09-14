package com.cinescape1.data.network

import com.cinescape1.data.models.requestModel.*
import com.cinescape1.data.models.responseModel.*
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

    suspend fun addClubRechargeCard(request: AddClubRechargeRequest): Result<RechargeCardResponse> {
        return apiRequest { api.addRechargeCard(request) }
    }

    suspend fun getClubAmounts(av: String = "", pt: String = ""): Result<GetAmountResponse> {
        return apiRequest { api.getAmounts(av, pt) }
    }

    suspend fun foodResponse(): Result<FoodResponse> {
        return apiRequest { api.foodResponse() }
    }

    suspend fun countryCode(): Result<CountryCodeResponse> {
        return apiRequest { api.countryCode() }
    }
    suspend fun updateAccount(request: UpdateAccountRequest): Result<UpdateAccountResponse> {
        return apiRequest { api.updateAccount(request) }
    }

    suspend fun getAmount(): Result<GetAmmountResponse> {
        return apiRequest { api.getAmount() }
    }
    suspend fun getProfile(request: ProfileRequest): Result<ProfileResponse> {
        return apiRequest { api.getProfile(request) }
    }
    suspend fun contctUs(request: ContactUsRequest): Result<ContactUsResponse> {
        return apiRequest { api.contactUs(request.email,request.name,request.mobile,request.msg,request.file) }
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
    suspend fun activateCard(request: ActivatwWalletRequest): Result<ForgotOtpVerifyResponse> {
        return apiRequest { api.activateCard(request) }
    }
}