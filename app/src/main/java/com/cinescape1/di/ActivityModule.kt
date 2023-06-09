package com.cinescape1.di

import com.cinescape1.di.scoped.ActivityScoped
import com.cinescape1.ui.main.views.finalTicket.FinalTicketActivity
import com.cinescape1.ui.main.modules.*
import com.cinescape1.ui.main.views.login.activeWallet.ActivateWalletActivity
import com.cinescape1.ui.main.views.login.activeWallet.module.ActivateWalletModule
import com.cinescape1.ui.main.views.deleteAccount.DeleteAccountActivity
import com.cinescape1.ui.main.views.deleteAccount.module.DeleteAccountModule
import com.cinescape1.ui.main.views.details.nowShowing.ShowTimesActivity
import com.cinescape1.ui.main.views.details.nowShowing.module.CinemaLocationModule
import com.cinescape1.ui.main.views.details.nowShowing.module.ShowTimesModule
import com.cinescape1.ui.main.views.finalTicket.module.FinalTicketModule
import com.cinescape1.ui.main.views.food.FoodActivity
import com.cinescape1.ui.main.views.food.module.FoodModule
import com.cinescape1.ui.main.views.home.HomeActivity
import com.cinescape1.ui.main.views.details.cinemaLocation.CinemaLocationActivity
import com.cinescape1.ui.main.views.details.commingSoon.ComingSoonActivity
import com.cinescape1.ui.main.views.details.commingSoon.module.ComingSoonModule
import com.cinescape1.ui.main.views.home.fragments.home.offerDetails.OfferDetailsActivity
import com.cinescape1.ui.main.views.login.LoginActivity
import com.cinescape1.ui.main.views.login.resetPassword.ResetPasswordActivity
import com.cinescape1.ui.main.views.player.PlayerActivity
import com.cinescape1.ui.main.views.prefrence.UserPreferencesActivity
import com.cinescape1.ui.main.views.seatLayout.SeatScreenMainActivity
import com.cinescape1.ui.main.views.home.fragments.home.seeAll.SeeAllActivity
import com.cinescape1.ui.main.views.home.fragments.home.seeAll.module.SeeAllModule
import com.cinescape1.ui.main.views.home.fragments.home.seeAll.module.HomeModule
import com.cinescape1.ui.main.views.login.guest.ContinueGuestActivity
import com.cinescape1.ui.main.views.login.guest.module.ContinueGuestModule
import com.cinescape1.ui.main.views.login.module.LoginModule
import com.cinescape1.ui.main.views.login.otpVerification.OtpVerificationActivity
import com.cinescape1.ui.main.views.login.otpVerification.module.OtpVerificationModule
import com.cinescape1.ui.main.views.login.resetPassword.module.ResetPasswordModule
import com.cinescape1.ui.main.views.payment.PaymentWebActivity
import com.cinescape1.ui.main.views.payment.paymentFaield.PaymentFailedActivity
import com.cinescape1.ui.main.views.payment.paymentFaield.module.PaymentFailedModule
import com.cinescape1.ui.main.views.payment.paymentList.PaymentListActivity
import com.cinescape1.ui.main.views.payment.paymentList.module.PaymentListModule
import com.cinescape1.ui.main.views.payment.paymentList.module.PaymentModule
import com.cinescape1.ui.main.views.player.module.PlayerModule
import com.cinescape1.ui.main.views.prefrence.module.UserPreferencesModule
import com.cinescape1.ui.main.views.seatLayout.module.SeatScreenMainModule
import com.cinescape1.ui.main.views.splash.SplashActivity
import com.cinescape1.ui.main.views.splash.module.SplashModule
import com.cinescape1.ui.main.views.summery.SummeryActivity
import com.cinescape1.ui.main.views.summery.module.CheckoutWithFoodModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [SplashModule::class])
    abstract fun splashActivity(): SplashActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [LoginModule::class])
    abstract fun loginActivity(): LoginActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [UserPreferencesModule::class])
    abstract fun  userPreferencesActivity(): UserPreferencesActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [HomeModule::class])
    abstract fun  homeActivity(): HomeActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [ShowTimesModule::class])
    abstract fun  showTimesActivity(): ShowTimesActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [FoodModule::class])
    abstract fun  foodActivity(): FoodActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [CheckoutWithFoodModule::class])
    abstract fun  checkoutWithFoodActivity(): SummeryActivity


    @ActivityScoped
    @ContributesAndroidInjector(modules = [SeatScreenMainModule::class])
    abstract fun  seatScreenMainActivity(): SeatScreenMainActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [SplashModule::class])
    abstract fun paymentWebActivity(): PaymentWebActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [FinalTicketModule::class])
    abstract fun finalTicketActivity(): FinalTicketActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [SeeAllModule::class])
    abstract fun seeAllActivity(): SeeAllActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [PlayerModule::class])
    abstract fun playerActivity(): PlayerActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [OfferDetailsModule::class])
    abstract fun offerActivity(): OfferDetailsActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [ActivateWalletModule::class])
    abstract fun activate(): ActivateWalletActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [ContinueGuestModule::class])
    abstract fun continueGuestActivity(): ContinueGuestActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [ResetPasswordModule::class])
    abstract fun resetPasswordActivity(): ResetPasswordActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [CinemaLocationModule::class])
    abstract fun cinemaLocationActivity(): CinemaLocationActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [PaymentFailedModule::class])
    abstract fun paymentFailedActivity(): PaymentFailedActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [OtpVerificationModule::class])
    abstract fun otpVerificationActivity(): OtpVerificationActivity


    @ActivityScoped
    @ContributesAndroidInjector(modules = [PaymentListModule::class])
    abstract fun paymentListActivity(): PaymentListActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [ComingSoonModule::class])
    abstract fun comActivity(): ComingSoonActivity


    @ActivityScoped
    @ContributesAndroidInjector(modules = [DeleteAccountModule::class])
    abstract fun deleteAccActivity(): DeleteAccountActivity


}