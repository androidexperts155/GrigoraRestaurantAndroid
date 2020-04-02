package com.rvtechnologies.grigorahq.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.rvtechnologies.grigorahq.ApiRepo
import com.rvtechnologies.grigorahq.services.models.LoginResponseModel


class LoginFragmentViewModel : ViewModel() {
    /*
    Params
     */
    var email: MutableLiveData<String> = MutableLiveData()
    var password: MutableLiveData<String> = MutableLiveData()

    /*
    Observers
     */
    var isLoading = MutableLiveData<Boolean>()
    var loginResult = MutableLiveData<Any>()

    /*
    Login Method require email password
     */
    fun phoneLogin() {
        isLoading.value = true
        if (isValidPhone()) {
            ApiRepo.getInstance()
                .phoneLogin(
                    email.value.toString().trim()
                ) { success, result ->
                    isLoading.value = false
                    if (success) {
                        loginResult.value =
                            Gson().fromJson(
                                result as JsonElement,
                                LoginResponseModel::class.java
                            )
                    } else {
                        loginResult.value = result
                    }
                }
        }
    }

    fun isValidPhone(): Boolean {
        return if (email.value.isNullOrBlank()) {
            isLoading.value = false
            loginResult.value = "Invalid Phone"
            false
        } else if (email.value.toString().length < 9) {
            isLoading.value = false
            loginResult.value = "Invalid Phone"
            false
        } else
            true
    }

}