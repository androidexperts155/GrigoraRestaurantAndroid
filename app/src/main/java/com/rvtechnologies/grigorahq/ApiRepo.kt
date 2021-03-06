package com.rvtechnologies.grigorahq

import android.util.Log
import com.google.gson.JsonElement
import com.rvtechnologies.grigorahq.services.api.ApiClient
import com.rvtechnologies.grigorahq.utils.CommonUtils
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiRepo {
    companion object {
        private var ApiRepoInstance: ApiRepo? = null
        fun getInstance() = ApiRepoInstance
            ?: ApiRepo().also {
                ApiRepoInstance = it
            }
    }

    /*
   Set Language data
    */


    /*
    Login repo
     */


    fun phoneLogin(
        phone: String,
        onResult: (isSuccess: Boolean, response: Any?) -> Unit
    ) {

        ApiClient.getApiServices().phoneLogin(phone, "2")
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>?,
                    response: Response<JsonElement>?
                ) {
                    if (response != null && response.isSuccessful)
                        onResult(true, response.body()!!)
                    else {
                        onResult(false, CommonUtils.parseError(response))
                    }
                }

                override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                    onResult(false, t?.message)
                }
            })
    }
}








    /*
Cuisine repo



    fun getCurrentOrders(
        token: String,
        onResult: (isSuccess: Boolean, response: Any?) -> Unit
    ) {

        ApiClient.getClient().getCurrentOrders(token)
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>?,
                    response: Response<JsonElement>?
                ) {
                    if (response != null && response.isSuccessful)
                        onResult(true, response.body()!!)
                    else {
                        onResult(false, CommonUtils.parseError(response))
                    }
                }

                override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                    onResult(false, t?.message)
                }
            })
    }

    fun getPastOrders(
        token: String,
        onResult: (isSuccess: Boolean, response: Any?) -> Unit
    ) {

        ApiClient.getClient().getPastOrders(token)
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>?,
                    response: Response<JsonElement>?
                ) {
                    if (response != null && response.isSuccessful)
                        onResult(true, response.body()!!)
                    else {
                        onResult(false, CommonUtils.parseError(response))
                    }
                }

                override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                    onResult(false, t?.message)
                }
            })
    }


    fun rateDriver(
        token: String,
        receiverId: String,
        rating: String,
        review: String,
        orderId: String,
        onResult: (isSuccess: Boolean, response: Any?) -> Unit
    ) {

        ApiClient.getClient().rateDriver(
            token = token,
            receiverId = receiverId,
            orderId = orderId,
            rating = rating,
            receiver_type = "3",
            review = review
        )
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>?,
                    response: Response<JsonElement>?
                ) {
                    if (response != null && response.isSuccessful)
                        onResult(true, response.body()!!)
                    else {
                        onResult(false, CommonUtils.parseError(response))
                    }
                }

                override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                    onResult(false, t?.message)
                }
            })
    }

    fun rateRestaurant(
        token: String,
        receiverId: String,
        rating: String,
        review: String,
        orderId: String,
        onResult: (isSuccess: Boolean, response: Any?) -> Unit
    ) {

        ApiClient.getClient().rateRestaurant(
            token = token,
            receiverId = receiverId,
            orderId = orderId,
            rating = rating,
            receiver_type = "2",
            review = review
        )
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>?,
                    response: Response<JsonElement>?
                ) {
                    if (response != null && response.isSuccessful)
                        onResult(true, response.body()!!)
                    else {
                        onResult(false, CommonUtils.parseError(response))
                    }
                }

                override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                    onResult(false, t?.message)
                }
            })
    }

    fun rateMeals(
        token: String,
        rating: String,
        orderId: String,
        onResult: (isSuccess: Boolean, response: Any?) -> Unit
    ) {
        ApiClient.getClient().rateMeals(
            token = token,
            rating = rating,
            orderId = orderId
        )
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>?,
                    response: Response<JsonElement>?
                ) {
                    if (response != null && response.isSuccessful)
                        onResult(true, response.body()!!)
                    else {
                        onResult(false, CommonUtils.parseError(response))
                    }
                }

                override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                    onResult(false, t?.message)
                }
            })
    }


    fun likeOrUnlike(
        token: String,
        restaurantId: String,
        status: String,

        onResult: (isSuccess: Boolean, response: Any?) -> Unit
    ) {

        ApiClient.getClient().likeOrUnlike(
            token = token,
            restaurant_id = restaurantId,
            status = status
        )
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>?,
                    response: Response<JsonElement>?
                ) {
                    if (response != null && response.isSuccessful) {
                        onResult(true, response.body()!!)
                        Log.e("fav", response.body().toString())
                    } else {
                        onResult(false, CommonUtils.parseError(response))
                    }
                }

                override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                    onResult(false, t?.message)
                }
            })
    }


    fun getAllCategories(
        onResult: (isSuccess: Boolean, response: Any?) -> Unit
    ) {

        ApiClient.getClient().getAllCategories()
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>?,
                    response: Response<JsonElement>?
                ) {
                    if (response != null && response.isSuccessful)
                        onResult(true, response.body()!!)
                    else {
                        onResult(false, CommonUtils.parseError(response))
                    }
                }

                override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                    onResult(false, t?.message)
                }
            })
    }


    fun getRestaurants(
        lat: String,
        lng: String,
        sort: String,
        search: String,
        user_id: String,
        onResult: (isSuccess: Boolean, response: Any?) -> Unit
    ) {
        ApiClient.getClient().getRestaurants(lat, lng, sort, search, user_id)
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>?,
                    response: Response<JsonElement>?
                ) {
                    if (response != null && response.isSuccessful) {
                        onResult(true, response.body()!!)
                        Log.e("rest", response.body().toString())
                    } else {
                        onResult(false, CommonUtils.parseError(response))
                        Log.e("rest unsucces", CommonUtils.parseError(response)!!)

                    }
                }

                override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                    onResult(false, t?.message)
                    Log.e("rest unsucces", t?.message!!)

                }

            })
    }

    fun getPopularRestaurants(
        id: String,
        lat: String,
        lng: String,

        onResult: (isSuccess: Boolean, response: Any?) -> Unit
    ) {
//        ApiClient.getClient().getPopularRestaurants(token, id,lat, lng)
        ApiClient.getClient().getPopularRestaurants(id = id, lat = lat, lng = lng)
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>?,
                    response: Response<JsonElement>?
                ) {
                    if (response != null && response.isSuccessful)
                        onResult(true, response.body()!!)
                    else {
                        onResult(false, CommonUtils.parseError(response))
                    }
                }

                override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                    onResult(false, t?.message)
                }
            })
    }

    fun getRestaurantsByCuisine(
        token: String,
        id: String,
        onResult: (isSuccess: Boolean, response: Any?) -> Unit
    ) {
        ApiClient.getClient().getRestaurantsByCuisine(token, id)
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>?,
                    response: Response<JsonElement>?
                ) {
                    if (response != null && response.isSuccessful)
                        onResult(true, response.body()!!)
                    else {
                        onResult(false, CommonUtils.parseError(response))
                    }
                }

                override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                    onResult(false, t?.message)
                }

            })
    }

    fun getMenuItemByCategory(
        id: String, lat: String, lang: String,
        user_id: String,
        onResult: (isSuccess: Boolean, response: Any?) -> Unit
    ) {
        ApiClient.getClient()
            .getMenuItemByCategory(id = id, lat = lat, long = lang, user_id = user_id)
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>?,
                    response: Response<JsonElement>?
                ) {
                    if (response != null && response.isSuccessful) {


                        onResult(true, response.body()!!)
                    } else {
                        onResult(false, CommonUtils.parseError(response))
                    }
                }

                override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                    onResult(false, t?.message)
                }

            })
    }


    fun getRestaurantsDetails(
        token: String,
        user_id: String,
        restaurant_id: String,
        onResult: (isSuccess: Boolean, response: Any?) -> Unit
    ) {
        ApiClient.getClient().getRestaurantDetails(token, user_id, restaurant_id)
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>?,
                    response: Response<JsonElement>?
                ) {
                    if (response != null && response.isSuccessful)
                        onResult(true, response.body()!!)
                    else {
                        onResult(false, CommonUtils.parseError(response))
                    }
                }

                override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                    onResult(false, t?.message)
                }

            })
    }


    fun getItemCart(
        token: String,
        itemId: String,
        onResult: (isSuccess: Boolean, response: Any?) -> Unit
    ) {
        ApiClient.getClient().getItemCart(token, itemId)
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>?,
                    response: Response<JsonElement>?
                ) {
                    if (response != null && response.isSuccessful)
                        onResult(true, response.body()!!)
                    else {
                        onResult(false, CommonUtils.parseError(response))
                    }
                }

                override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                    onResult(false, t?.message)
                }

            })
    }


    fun clearCart(
        token: String,
        onResult: (isSuccess: Boolean, response: Any?) -> Unit
    ) {
        ApiClient.getClient().removeCart(token)
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>?,
                    response: Response<JsonElement>?
                ) {
                    if (response != null && response.isSuccessful)
                        onResult(true, response.body()!!)
                    else {
                        onResult(false, CommonUtils.parseError(response))
                    }
                }

                override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                    onResult(false, t?.message)
                }

            })
    }


    fun getOffers(
        token: String, id: String,
        onResult: (isSuccess: Boolean, response: Any?) -> Unit
    ) {
        ApiClient.getClient().getOffers(token, id)
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>?,
                    response: Response<JsonElement>?
                ) {
                    if (response != null && response.isSuccessful)
                        onResult(true, response.body()!!)
                    else {
                        onResult(false, CommonUtils.parseError(response))
                    }
                }

                override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                    onResult(false, t?.message)
                }
            })
    }

    fun addItemToCart(
        token: String,
        restaurantId: String,
        itemId: String,
        quantity: String,
        price: String,
        itemChoices: String,
        onResult: (isSuccess: Boolean, response: Any?) -> Unit
    ) {
        ApiClient.getClient().addItemToCart(
            token = token,
            restaurantId = restaurantId,
            itemId = itemId,
            price = price,
            quantity = quantity,
            item_choices = itemChoices
        )
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>?,
                    response: Response<JsonElement>?
                ) {
                    if (response != null && response.isSuccessful)
                        onResult(true, response.body()!!)
                    else {
                        onResult(false, CommonUtils.parseError(response))
                    }
                }

                override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                    onResult(false, t?.message)
                }

            })
    }


    fun updateCartQty(
        token: String,
        cartItemId: String,
        quantity: String,
        cart_id: String,

        onResult: (isSuccess: Boolean, response: Any?) -> Unit
    ) {
        ApiClient.getClient().updateCartQty(
            token = token,
            cartItemId = cartItemId,
            cartId = cart_id,
            quantity = quantity
        )
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>?,
                    response: Response<JsonElement>?
                ) {
                    if (response != null && response.isSuccessful)
                        onResult(true, response.body()!!)
                    else {
                        onResult(false, CommonUtils.parseError(response))
                    }
                }

                override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                    onResult(false, t?.message)
                }

            })
    }


    fun viewCart(
        token: String,
        latitude: String,
        logitude: String,
        onResult: (isSuccess: Boolean, response: Any?) -> Unit
    ) {
        ApiClient.getClient().viewCart(token = token, latitude = latitude, logitude = logitude)
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>?,
                    response: Response<JsonElement>?
                ) {
                    if (response != null && response.isSuccessful)
                        onResult(true, response.body()!!)
                    else {
                        onResult(false, CommonUtils.parseError(response))
                    }
                }

                override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                    onResult(false, t?.message)
                }

            })
    }


    fun placeOrder(
        token: String,
        cart_id: String,
        promo_id: String,
        app_fee: String,
        delivery_fee: String,
        price_before_promo: String,
        price_after_promo: String,
        final_price: String,
        payment_method: String,
        reference: String,
        delivery_address: String,
        delivery_lat: String,
        delivery_long: String,
        delivery_note: String,
        onResult: (isSuccess: Boolean, response: Any?) -> Unit
    ) {
        ApiClient.getClient().placeOrder(
            token = token,
            cart_id = cart_id,
            promo_id = promo_id,
            app_fee = app_fee,
            delivery_fee = delivery_fee,
            price_before_promo = price_before_promo,
            price_after_promo = price_after_promo,
            final_price = final_price,
            payment_method = payment_method,
            reference = reference,
            delivery_address = delivery_address,
            delivery_lat = delivery_lat,
            delivery_long = delivery_long,
            delivery_note = delivery_note
        )
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>?,
                    response: Response<JsonElement>?
                ) {
                    if (response != null && response.isSuccessful)
                        onResult(true, response.body()!!)
                    else {
                        onResult(false, CommonUtils.parseError(response))
                    }
                }

                override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                    onResult(false, t?.message)
                }

            })
    }


    fun payStack(
        token: String,
        amount: String,
        email: String,
        reference: String,
        onResult: (isSuccess: Boolean, response: Any?) -> Unit
    ) {
        ApiClient.getClient()
            .getPayStack(token = token, amount = amount, email = email, reference = reference)
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>?,
                    response: Response<JsonElement>?
                ) {
                    if (response != null && response.isSuccessful)
                        onResult(true, response.body()!!)
                    else {
                        onResult(false, CommonUtils.parseError(response))
                    }
                }

                override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                    onResult(false, t?.message)
                }

            })
    }


    fun getLocationTypes(
        onResult: (isSuccess: Boolean, response: Any?) -> Unit
    ) {
        ApiClient.getClient().getLocationTypes()
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>?,
                    response: Response<JsonElement>?
                ) {
                    if (response != null && response.isSuccessful)
                        onResult(true, response.body()!!)
                    else {
                        onResult(false, CommonUtils.parseError(response))
                    }
                }

                override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                    onResult(false, t?.message)
                }

            })
    }

    fun getOrderDetails(
        token: String,
        orderId: String,
        onResult: (isSuccess: Boolean, response: Any?) -> Unit
    ) {
        ApiClient.getClient().getOrderDetails(token = token, orderId = orderId)
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>?,
                    response: Response<JsonElement>?
                ) {
                    if (response != null && response.isSuccessful)
                        onResult(true, response.body()!!)
                    else {
                        onResult(false, CommonUtils.parseError(response))
                    }
                }

                override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                    onResult(false, t?.message)
                }
            })
    }

    fun getProfileData(
        token: String,
        onResult: (isSuccess: Boolean, response: Any?) -> Unit
    ) {
        ApiClient.getClient().getProfileData(token = token)
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>?,
                    response: Response<JsonElement>?
                ) {
                    if (response != null && response.isSuccessful)
                        onResult(true, response.body()!!)
                    else {
                        onResult(false, CommonUtils.parseError(response))
                    }
                }

                override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                    onResult(false, t?.message)
                }

            })
    }


    fun saveProfile(
        token: String,
        name: RequestBody,
        phone: RequestBody,
        image: MultipartBody.Part,
        onResult: (isSuccess: Boolean, response: Any?) -> Unit
    ) {
        ApiClient.getClient().saveProfile(token, name, phone, image)
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>?,
                    response: Response<JsonElement>?
                ) {
                    if (response != null && response.isSuccessful)
                        onResult(true, response.body()!!)
                    else {
                        onResult(false, CommonUtils.parseError(response))
                    }
                }

                override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                    onResult(false, t?.message)
                }

            })
    }


    fun changePassword(
        token: String,
        oldPassword: String,
        password: String,
        onResult: (isSuccess: Boolean, response: Any?) -> Unit
    ) {
        ApiClient.getClient().changePassword(token, oldPassword, password)
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>?,
                    response: Response<JsonElement>?
                ) {
                    if (response != null && response.isSuccessful)
                        onResult(true, response.body()!!)
                    else {
                        onResult(false, CommonUtils.parseError(response))
                    }
                }

                override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                    onResult(false, t?.message)
                }

            })
    }


    fun getRestaurantReviews(
        restaurantId: String,
        onResult: (isSuccess: Boolean, response: Any?) -> Unit
    ) {
        ApiClient.getClient().getRestaurantReviews(restaurantId)
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>?,
                    response: Response<JsonElement>?
                ) {
                    if (response != null && response.isSuccessful)
                        onResult(true, response.body()!!)
                    else {
                        onResult(false, CommonUtils.parseError(response))
                    }
                }

                override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                    onResult(false, t?.message)
                }

            })
    }


    fun cancelOrder(
        token: String,
        orderId: String,
        onResult: (isSuccess: Boolean, response: Any?) -> Unit
    ) {
        ApiClient.getClient().cancelOrder(orderId, token)
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>?,
                    response: Response<JsonElement>?
                ) {
                    if (response != null && response.isSuccessful)
                        onResult(true, response.body()!!)
                    else {
                        onResult(false, CommonUtils.parseError(response))
                    }
                }

                override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                    onResult(false, t?.message)
                }

            })
    }

    fun searchUser(
        token: String,
        email: String,

        onResult: (isSuccess: Boolean, response: Any?) -> Unit
    ) {
        ApiClient.getClient().searchUser(token = token, email = email)
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>?,
                    response: Response<JsonElement>?
                ) {
                    if (response != null && response.isSuccessful)
                        onResult(true, response.body()!!)
                    else {
                        onResult(false, CommonUtils.parseError(response))
                    }
                }

                override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                    onResult(false, t?.message)
                }

            })
    }


    fun transferMoney(
        token: String,
        email: String,
        amount: String,
        reason: String,

        onResult: (isSuccess: Boolean, response: Any?) -> Unit
    ) {
        ApiClient.getClient()
            .transferMoney(token = token, email = email, amount = amount, reason = reason)
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>?,
                    response: Response<JsonElement>?
                ) {
                    if (response != null && response.isSuccessful)
                        onResult(true, response.body()!!)
                    else {
                        onResult(false, CommonUtils.parseError(response))
                    }
                }

                override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                    onResult(false, t?.message)
                }

            })
    }

    fun walletHistory(
        token: String,
        onResult: (isSuccess: Boolean, response: Any?) -> Unit
    ) {
        ApiClient.getClient()
            .walletHistory(token = token)
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>?,
                    response: Response<JsonElement>?
                ) {
                    if (response != null && response.isSuccessful)
                        onResult(true, response.body()!!)
                    else {
                        onResult(false, CommonUtils.parseError(response))
                    }
                }

                override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                    onResult(false, t?.message)
                }

            })
    }

    fun addMoney(
        token: String,
        transactionData: String,
        amount: String,
        onResult: (isSuccess: Boolean, response: Any?) -> Unit
    ) {
        ApiClient.getClient()
            .addMoney(token = token, amount = amount, transactionData = transactionData)
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>?,
                    response: Response<JsonElement>?
                ) {
                    if (response != null && response.isSuccessful)
                        onResult(true, response.body()!!)
                    else {
                        onResult(false, CommonUtils.parseError(response))
                    }
                }

                override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                    onResult(false, t?.message)
                }

            })
    }

    fun getDashboardData(
        map: HashMap<String, Any>,
        onResult: (isSuccess: Boolean, response: Any?) -> Unit
    ) {
        ApiClient.getClient()
            .getDashboardData(body = map, token = map["token"].toString())
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>?,
                    response: Response<JsonElement>?
                ) {
                    if (response != null && response.isSuccessful)
                        onResult(true, response.body()!!)
                    else {
                        onResult(false, CommonUtils.parseError(response))
                    }
                }

                override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                    onResult(false, t?.message)
                }

            })
    }


}
     */