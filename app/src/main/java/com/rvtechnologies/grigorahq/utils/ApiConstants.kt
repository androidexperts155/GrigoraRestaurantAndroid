package com.rvtechnologies.grigorahq.utils

import retrofit2.http.GET

object ApiConstants {
    const val BASE_URL = "http://3.13.78.53/GriGora/public/api/"
    const val PLACE_API_KEY = "AIzaSyBUxnaQYt2gfojKC3lcYgtG88ERduqyjIU"
    var SOCKET_URL = "http://3.13.78.53:3000/"

    const val LOGIN_URL = "login"
    const val GET_NEAR_BY_RESTAURANT = "getNearByCustomer"
    const val SIGNUP_URL = "signup"
    const val PHONE_LOGIN_URL = "phone-login"
    const val GET_CUISINE_URL = "get-cuisine"
    const val GET_RESTAURANTS_URL = "restaurant-list"
}