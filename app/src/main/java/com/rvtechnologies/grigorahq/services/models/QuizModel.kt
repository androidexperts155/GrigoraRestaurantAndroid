package com.rvtechnologies.grigorahq.services.models


import com.google.gson.annotations.SerializedName

data class QuizModel(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
) {
    data class Data(
        @SerializedName("answer")
        val answer: String,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("image")
        val image: String,
        @SerializedName("min_order_value")
        val minOrderValue: Int,
        @SerializedName("no_of_winners")
        val noOfWinners: Int,
        @SerializedName("offer_expiry")
        val offerExpiry: String,
        @SerializedName("offer_points")
        val offerPoints: String,
        @SerializedName("offer_percentage")
        val offerPercentage: String,
        @SerializedName("option1")
        val option1: String,
        @SerializedName("option2")
        val option2: String,
        @SerializedName("option3")
        val option3: String,
        @SerializedName("option4")
        val option4: String,
        @SerializedName("question")
        val question: String,
        @SerializedName("status")
        val status: String,
        @SerializedName("join_quiz")
        val joinQuiz: Int,
        @SerializedName("updated_at")
        val updatedAt: String
    )
}