package com.rvtechnologies.grigorahq.utils

import android.app.Activity
import android.content.pm.PackageManager
import android.util.DisplayMetrics
import android.util.TypedValue
import com.google.gson.Gson
import com.rvtechnologies.grigorahq.complete_profile.pojo.LocalAddress
import org.json.JSONObject

object CommonMethods {


    fun getScreenWidth(context: Activity): Int {
        val displayMetrics = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
//        var height = displayMetrics.heightPixels
        return displayMetrics.widthPixels

    }

    fun dpsToPixel(activity: Activity, dps: Int): Int {
        val r = activity.resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dps.toFloat(),
            r.displayMetrics
        ).toInt()
    }


    fun addressConversion(model: LocalAddress): JSONObject {
        return JSONObject(Gson().toJson(model))
    }

    fun getVersionNumber(activity: Activity): String {
        var version = ""
        try {
            val pInfo = activity.packageManager.getPackageInfo(activity.getPackageName(), 0)
            version = pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return version

    }


}


