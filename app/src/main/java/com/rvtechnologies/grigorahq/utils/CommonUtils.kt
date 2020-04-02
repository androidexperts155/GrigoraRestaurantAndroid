package com.rvtechnologies.grigorahq.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.services.models.CommonResponseModel
import kotlinx.android.synthetic.main.loader_layout.view.*
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


object CommonUtils {
    fun showToast(context: Context?, msg: String) {
        if (context != null)
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun savePrefs(context: Context?, key: String, value: String?) {
        if (context != null && value != null) {
            val prefs = context.getSharedPreferences(PrefConstants.PREF_NAME, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putString(key, value)
            editor.apply()
        }
    }

    fun showMessage(view: View?, msg: String) {
        if (view != null)
            Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    fun savePrefsLang(context: Context?, key: String, value: String?) {
        if (context != null && value != null) {
            val prefs = context.getSharedPreferences("lang_pref", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putString(key, value)
            editor.apply()
        }
    }

    fun getPrefLang(context: Context?, key: String): String {
        val prefs = context?.getSharedPreferences("lang_pref", Context.MODE_PRIVATE)
        return prefs?.getString(key, "").toString()
    }

    fun getBooleanPrefValue(context: Context?, key: String): Boolean {
        val prefs = context?.getSharedPreferences(PrefConstants.PREF_NAME, Context.MODE_PRIVATE)
        return prefs?.getBoolean(key, false)!!
    }

    fun saveBooleanPrefs(context: Context?, key: String, value: Boolean) {
        if (context != null) {
            val prefs = context.getSharedPreferences(PrefConstants.PREF_NAME, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putBoolean(key, value)
            editor.apply()
        }
    }

    fun getPrefValue(context: Context?, key: String): String {
        val prefs = context?.getSharedPreferences(PrefConstants.PREF_NAME, Context.MODE_PRIVATE)
        return prefs?.getString(key, "").toString()
    }

    fun delPrefValue(context: Context?): Boolean {
        if (context == null)
            return false
        val prefs = context.getSharedPreferences(PrefConstants.PREF_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.clear()
        editor.commit()
        return true
    }

    fun changeStatusBarColor(color: Int, activity: AppCompatActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var decor = activity.window.decorView
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            activity.window.statusBarColor = color
            activity.window.statusBarColor = color
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.statusBarColor = color
        }
    }

    private var progressBarDialog: Dialog? = null

    fun showLoader(context: Context, message: String) {
        if (progressBarDialog == null) {
            val view = LayoutInflater.from(context).inflate(R.layout.loader_layout, null)
            progressBarDialog = Dialog(context)
            progressBarDialog?.window!!.requestFeature(Window.FEATURE_NO_TITLE)
            progressBarDialog?.setContentView(view)
            progressBarDialog?.setCancelable(false)
            progressBarDialog?.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            view.bringToFront()
            view.loadingMessage.text = message
            progressBarDialog?.setCancelable(false)
            progressBarDialog?.show()
        }
    }

    fun change24To12(context: Context, time: String): String {
        var inputPattern = ""
        var outputPattern = ""
        if (time.length == 8) {
            inputPattern = "HH:mm:ss"
            outputPattern = "h:MM aa"
        } else {
            inputPattern = "yyyy-MM-dd HH:mm:ss"
            outputPattern = "h:MM aa  dd MMM yyyy"
        }
        val sdf = SimpleDateFormat(inputPattern);
        val dateObj = sdf.parse(time);
        return SimpleDateFormat(outputPattern).format(dateObj)
    }




    fun change24To12(time: String): String {
        var inputPattern = ""
        var outputPattern = ""
        if (time.length == 8) {

            inputPattern = "HH:mm"
            outputPattern = "h:mm aa"
        } else if (time.length == 5) {
            inputPattern = "HH:mm"
            outputPattern = "h:mm aa"
        } else {
            inputPattern = "yyyy-MM-dd HH:mm:ss"
            outputPattern = "h:mm aa  dd MMM yyyy"
        }

        val sdf = SimpleDateFormat(inputPattern);
        val dateObj = sdf.parse(time);
        return SimpleDateFormat(outputPattern).format(dateObj)
    }

    fun changeToUTC(dateStr: String): String {

        val df =
            SimpleDateFormat("HH:mm", Locale.ENGLISH)
        df.timeZone = TimeZone.getDefault()
        val date = df.parse(dateStr)
        df.timeZone = TimeZone.getTimeZone("UTC")
        return df.format(date)

    }

    fun changeUTC_to_local(time: String): String {
        var resultDate = ""
        if (time.length >= 19) {
            val df =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            df.timeZone = TimeZone.getTimeZone("UTC")
            val date = df.parse(time)
            df.timeZone = TimeZone.getDefault()
            resultDate = df.format(date);
        } else {
            val df =
                SimpleDateFormat("HH:mm", Locale.ENGLISH)
            df.timeZone = TimeZone.getTimeZone("UTC")
            val date = df.parse(time)
            df.timeZone = TimeZone.getDefault()

            resultDate = df.format(date);
        }
        return resultDate
    }

    fun hideLoader() {
        if (progressBarDialog != null) {
            progressBarDialog?.dismiss()
            progressBarDialog = null
        }
    }

    fun parseError(response: Response<JsonElement>?): String? {

        return when (response?.code()) {
            400 -> {
                if (response.errorBody() != null) {
                    val error: CommonResponseModel<*> = Gson().fromJson(
                        response.errorBody()?.string(),
                        CommonResponseModel::class.java
                    )
                    return error.message
                } else {
                    return "Something went wrong"
                }
            }
            401 -> "Unauthorized"
            403 -> "Forbidden"
            404 -> {
                if (response.errorBody() != null) {
                    val error: CommonResponseModel<*> = Gson().fromJson(
                        response.errorBody()?.string(),
                        CommonResponseModel::class.java
                    )
                    return error.message
                } else {
                    return "Something went wrong"
                }

            }
            405 -> "Method not allowed"
            422 -> {
                if (response.errorBody() != null) {
                    val error: CommonResponseModel<*> = Gson().fromJson(
                        response.errorBody()?.string(),
                        CommonResponseModel::class.java
                    )
                    return error.message
                } else {
                    return "Something went wrong"
                }
            }
            429 -> "Too Many Requests"
            500 -> "Internal Server Error"
            503 -> "Service Unavailable"
            else -> "Something went wrong"
        }
    }

    fun isValidEmail(email: String): Boolean {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches())
    }
}