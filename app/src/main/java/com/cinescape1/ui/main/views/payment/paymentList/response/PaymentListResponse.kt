package com.cinescape1.ui.main.views.payment.paymentList.response

data class PaymentListResponse(
    val code: Int,
    val msg: String,
    val output: Output,
    val result: String
):java.io.Serializable{
    data class Output(
        val amount: String,
        val payInfo: ArrayList<PayInfo>,
        val payMode: ArrayList<PayMode>
    ):java.io.Serializable{
        data class PayInfo(
            val amt: String,
            val highlight: Boolean,
            val key: String
        )
        data class PayMode(
            val name: String,
            val payType: String,
            val priority: Int,
            val respPayModes: ArrayList<RespPayMode>
        ):java.io.Serializable{
            data class RespPayMode(
                val activeImageUrl: Any,
                val balance: String,
                val caption: String,
                val id: Int,
                val imageUrl: String,
                val name: String,
                val payModeBanks: ArrayList<PayModeBank>,
                val pid: String,
                val priority: Int,
                val tnc: String,
                val type: String
            ):java.io.Serializable {
                data class PayModeBank(
                    val id: Int,
                    var name: String
                )
            }
        }
    }
}