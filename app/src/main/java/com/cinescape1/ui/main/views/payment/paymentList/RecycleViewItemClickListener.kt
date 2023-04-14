package com.cinescape1.ui.main.views.payment.paymentList

import com.cinescape1.data.models.responseModel.GetMovieResponse
import com.cinescape1.ui.main.views.payment.paymentList.response.PaymentListResponse

interface RecycleViewItemClickListener {
            fun walletItemApply(view: PaymentListResponse.Output.PayMode)
            fun bankItemApply(
                view: String,
                cardNo: String
            )

            fun bankItemRemove(
                view: String,
                cardNo: String
            )

            fun onSimilarItemClick(view: GetMovieResponse.Output.Similar)

            fun onVoucherApply(
                view: PaymentListResponse.Output.PayMode,
                offerCode: String,
                clickName: String,
                clickId: String
            )

            fun onGiftCardItemRemove(
                item: PaymentListResponse.Output.PayMode,
                offerCode: String,
                clickName: String,
                clickId: String
            )
        }