package com.rvtechnologies.grigorahq.services.api

import com.google.gson.JsonElement
import com.rvtechnologies.grigorahq.complete_profile.pojo.PlaceDetails_Pojo
import com.rvtechnologies.grigorahq.complete_profile.pojo.PlacePredictions_Pojo
import com.rvtechnologies.grigorahq.services.models.OrderModel
import com.rvtechnologies.grigorahq.utils.ApiConstants
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @FormUrlEncoded
    @POST(ApiConstants.LOGIN_URL)
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("role") role: String
    ): Call<JsonElement>


    @Multipart
    @POST(ApiConstants.SIGNUP_URL)
    fun signUp(
        @Part("name") name: String,
        @Part("email") email: String,
        @Part("phone") phone: String,
        @Part("password") password: String,
        @Part("password_confirmation") password_confirmation: String,
        @Part("role") role: String,
        @Part profileImage: MultipartBody.Part,
        @Part proofImage: MultipartBody.Part

    ): Call<JsonElement>

    @GET(ApiConstants.GET_CUISINE_URL)
    fun getCuisine(
        @Header("Authorization") token: String
    ): Call<JsonElement>

    @Multipart
    @POST
    fun postData(
        @Url url: String,
        @HeaderMap authenticator: Map<String, String>,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part file: ArrayList<MultipartBody.Part>,
        @Part file1: ArrayList<MultipartBody.Part>,
        @Part file2: ArrayList<MultipartBody.Part>
    ): Call<JsonElement>

    @FormUrlEncoded
    @POST
    fun postData(
        @Url url: String,
        @FieldMap body: HashMap<String, Any?>
    ): Call<JsonElement>

    @FormUrlEncoded
    @POST
    fun postData(
        @Url url: String,
        @HeaderMap authenticator: HashMap<String, Any?>,
        @FieldMap body: HashMap<String, Any?>
    ): Call<JsonElement>

    @Multipart
    @POST
    fun postData(
        @Url url: String,
        @HeaderMap authenticator: Map<String, String>,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part file: ArrayList<MultipartBody.Part>
    ): Call<JsonElement>

    @GET
    fun getData(
        @Url url: String,
        @HeaderMap headers: Map<String, String>
    ): Call<JsonElement>

    @POST
    fun logout(
        @Url url: String,
        @HeaderMap headers: Map<String, String>
    ): Call<JsonElement>

    @GET
    fun getLocations(@Url url: String): Call<PlacePredictions_Pojo>

    @GET
    fun getLocationDetails(@Url url: String): Call<PlaceDetails_Pojo>

    @GET("restaurant-orders")
     fun getrestaurant_orders(@HeaderMap authenticator: Map<String, String>): Call<OrderModel>

    @FormUrlEncoded
    @POST(ApiConstants.PHONE_LOGIN_URL)
    fun phoneLogin(
        @Field("phone") email: String,
        @Field("role") role: String
    ):Call<JsonElement>
}

