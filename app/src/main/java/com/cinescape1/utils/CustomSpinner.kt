package com.cinescape1.utils

import android.content.Context
import android.content.res.Resources.Theme
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSpinner
import com.cinescape1.databinding.BankOfferBinding
import com.cinescape1.databinding.ItemBankOfferBinding
import com.cinescape1.ui.main.views.payment.paymentList.adapter.PaymentListAdapter

class CustomSpinner : AppCompatSpinner {
    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, mode: Int) : super(context!!, mode) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
    }
    /**
     * An interface which a client of this Spinner could use to receive
     * open/closed events for this Spinner.
     */
    interface OnSpinnerEventsListener {
        /**
         * Callback triggered when the spinner was opened.
         */
        fun onSpinnerOpened(spinner: AppCompatSpinner?)

        /**
         * Callback triggered when the spinner was closed.
         */
        fun onSpinnerClosed(spinner: AppCompatSpinner?)
    }

    private var mListener: OnSpinnerEventsListener? = null
    private var mOpenInitiated = false

    fun setListenerCallback(adapter:PaymentListAdapter){
        this.mListener=adapter
    }

    // implement the Spinner constructors that you need
    override fun performClick(): Boolean {
        // register that the Spinner was opened so we have a status
        // indicator for when the container holding this Spinner may lose focus
        mOpenInitiated = true
        if (mListener != null) {
            mListener!!.onSpinnerOpened(this)
        }
        return super.performClick()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasBeenOpened() && hasFocus) {
            performClosedEvent()
        }
    }

    /**
     * Register the listener which will listen for events.
     */
    fun setSpinnerEventsListener(
        onSpinnerEventsListener: OnSpinnerEventsListener?
    ) {
        mListener = onSpinnerEventsListener
    }

    /**
     * Propagate the closed Spinner event to the listener from outside if needed.
     */
    fun performClosedEvent() {
        mOpenInitiated = false
        if (mListener != null) {
            mListener!!.onSpinnerClosed(this)
        }
    }


    /**
     * A boolean flag indicating that the Spinner triggered an open event.
     *
     * @return true for opened Spinner
     */
    fun hasBeenOpened(): Boolean {
        return mOpenInitiated
    }
}