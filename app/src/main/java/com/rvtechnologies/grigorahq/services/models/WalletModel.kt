package com.rvtechnologies.grigorahq.services.models


import com.google.gson.annotations.SerializedName
import java.io.Serializable

  data class WalletModel(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
) {
    data class Data(
        @SerializedName("history")
        val history: List<History>,
        @SerializedName("naira_to_points")
        val nairaToPoints: Int,
        @SerializedName("wallet")
        val wallet: String,
        @SerializedName("wallet_id")
        val walletId: String
    ) {
        data class History(
            @SerializedName("amount")
            val amount: String,
            @SerializedName("created_at")
            val createdAt: String,
            @SerializedName("customer_address")
            val customerAddress: String,
            @SerializedName("customer_email")
            val customerEmail: String,
            @SerializedName("customer_image")
            val customerImage: String,
            @SerializedName("customer_name")
            val customerName: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("order_id")
            val orderId: Int,
            @SerializedName("other_user_email")
            val otherUserEmail: String,
            @SerializedName("other_user_image")
            val otherUserImage: String,
            @SerializedName("other_user_name")
            val otherUserName: String,
            @SerializedName("reason")
            val reason: String,
            @SerializedName("reference")
            val reference: String,
            @SerializedName("transaction_data")
            val transactionData: String,
            @SerializedName("type")
            val type: String,
            @SerializedName("updated_at")
            val updatedAt: String,
            @SerializedName("user_account_number")
            val userAccountNumber: String,
            @SerializedName("user_email")
            val userEmail: String,
            @SerializedName("user_id")
            val userId: Int,
            @SerializedName("user_image")
            val userImage: String,
            @SerializedName("user_name")
            val userName: String,
            @SerializedName("user_wallet_id")
            val userWalletId: String
        )
    }
}