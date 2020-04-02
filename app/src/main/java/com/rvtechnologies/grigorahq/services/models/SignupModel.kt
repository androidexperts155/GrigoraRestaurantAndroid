package com.rvtechnologies.grigorahq.services.models


import com.google.gson.annotations.SerializedName

data class SignupModel(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("token_type")
    val tokenType: String
) {
    data class Data(
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("email")
        val email: String,
        @SerializedName("french_name")
        val frenchName: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("id_proof")
        val idProof: String,
        @SerializedName("image")
        val image: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("phone")
        val phone: String,
        @SerializedName("role")
        val role: String,
        @SerializedName("updated_at")
        val updatedAt: String
    )
}