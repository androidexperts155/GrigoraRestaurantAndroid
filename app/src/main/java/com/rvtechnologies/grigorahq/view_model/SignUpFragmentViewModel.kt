//package com.rvtechnologies.grigorahq.view_model
//
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import com.google.gson.Gson
//import com.google.gson.JsonElement
//import com.rvtechnologies.grigorahq.model.ApiRepo
//import com.rvtechnologies.grigorahq.utils.CommonUtils
//import com.rvtechnologies.grigorahq.model.models.LoginResponseModel
//
//
//class SignUpFragmentViewModel : ViewModel() {
//    /*
//    Params
//     */
//    var name: MutableLiveData<String> = MutableLiveData()
//    var phone: MutableLiveData<String> = MutableLiveData()
//    var email: MutableLiveData<String> = MutableLiveData()
//    var password: MutableLiveData<String> = MutableLiveData()
//    var confirm_password: MutableLiveData<String> = MutableLiveData()
//    var profile_img: MutableLiveData<String> = MutableLiveData()
//    var proof_img: MutableLiveData<String> = MutableLiveData()
//
//    /*
//    Observers
//     */
//    var isLoading = MutableLiveData<Boolean>()
//    var signUpResult = MutableLiveData<Any>()
//
//    /*
//    Login Method require email password
//     */
//    fun signUp() {
////        isLoading.value = true
////        if (isValidData()) {
////            ApiRepo.getInstance()
////                .signUp(
////                    name.value.toString().trim(),
////                    email.value.toString().trim(),
////                    phone.value.toString().trim(),
////                    password.value.toString().trim(),
////                    confirm_password.value.toString().trim()
////                ) { success, result ->
////                    isLoading.value = false
////                    if (success) {
////                        signUpResult.value =
////                            Gson().fromJson(
////                                result as JsonElement,
////                                LoginResponseModel::class.java
////                            )
////                    } else {
////                        signUpResult.value = result
////                    }
////                }
////        }
//    }
//
//    /*
//    Validate login credentials from user
//     */
//    private fun isValidData(): Boolean {
//        if (name.value.isNullOrBlank()) {
//            isLoading.value = false
//            signUpResult.value = "Invalid name"
//            return false
//        } else if (email.value.isNullOrBlank() || !CommonUtils.isValidEmail(email.value.toString())) {
//            isLoading.value = false
//            signUpResult.value = "Invalid Email"
//            return false
//        } else if (password.value.isNullOrBlank()) {
//            isLoading.value = false
//            signUpResult.value = "Invalid Password"
//            return false
//        } else if (phone.value.isNullOrBlank() || phone.value.toString().length != 10) {
//            isLoading.value = false
//            signUpResult.value = "Invalid phone number"
//            return false
//        } else if (password.value.isNullOrBlank() != confirm_password.value.isNullOrBlank()) {
//            isLoading.value = false
//            signUpResult.value = "Please confirm password"
//            return false
//        } else
//            return true
//    }
//}