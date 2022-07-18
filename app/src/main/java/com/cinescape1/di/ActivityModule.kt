package com.cinescape1.di

import com.cinescape1.di.scoped.ActivityScoped
import com.cinescape1.ui.main.FinalTicketActivity
import com.cinescape1.ui.main.modules.*
import com.cinescape1.ui.main.views.*
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
    @ContributesAndroidInjector(modules = [SignupModule::class])
    abstract fun signupActivity(): SignUpActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [UserPreferencesModule::class])
    abstract fun  userPreferencesActivity(): UserPreferencesActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [HomeModule::class])
    abstract fun  homeActivity(): HomeActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [CinemaSessionModule::class])
    abstract fun  cinemaSessionsActivity(): CinemaSessionActivity


    @ActivityScoped
    @ContributesAndroidInjector(modules = [ComingSoonInfoModule::class])
    abstract fun  comingSoonInfoActivity(): ComingSoonMovieInfoActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [ShowTimesModule::class])
    abstract fun  showTimesActivity(): ShowTimesActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [FoodModule::class])
    abstract fun  foodActivity(): FoodActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [CheckoutWithFoodModule::class])
    abstract fun  checkoutWithFoodActivity(): CheckoutWithFoodActivity


    @ActivityScoped
    @ContributesAndroidInjector(modules = [MoreInfoModule::class])
    abstract fun  moreInfoActivity(): MoreInfoActivity


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
    @ContributesAndroidInjector(modules = [SearchModule::class])
    abstract fun searchActivity(): SearchActivity

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

}