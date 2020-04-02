package com.rvtechnologies.grigorahq.ui.login_signup

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.complete_profile.CompleteProfileAtivity
import com.rvtechnologies.grigorahq.language_selection.LanuageSelectionActivity
import com.rvtechnologies.grigorahq.navigation.NavigationActivity
import com.rvtechnologies.grigorahq.utils.CommonUtils
import com.rvtechnologies.grigorahq.utils.GrigoraApp
import com.rvtechnologies.grigorahq.utils.PrefConstants
import java.util.*

class
Splash : AppCompatActivity() {
    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000
    var lang_type: String = ""

    internal val mRunnable: Runnable = Runnable {

        if (CommonUtils.getPrefLang(this, PrefConstants.LANGUAGE).isBlank()) {
            startActivity(
                Intent(this, LanuageSelectionActivity::class.java).putExtra(
                    "type_lang",
                    "splash"
                )
            )
            finish()
        } else if (CommonUtils.getPrefValue(this, PrefConstants.TOKEN).isBlank()) {
            setLang()
            startActivity(
                Intent(this, LanuageSelectionActivity::class.java).putExtra(
                    "type_lang",
                    "splash"
                )
            )
            finish()


        } else if (CommonUtils.getPrefValue(this, PrefConstants.ADDRESS).isNullOrEmpty()) {
            setLang()
            startActivity(Intent(this, CompleteProfileAtivity::class.java))
            finish()
        } else {
            setLang()
            startActivity(Intent(this, NavigationActivity::class.java))
            finish()

        }
    }

    private fun setLang() {
        lang_type = CommonUtils.getPrefLang(this, PrefConstants.LANGUAGE)

        if (lang_type.equals("en")) {
            val locale = Locale("en")
            Locale.setDefault(locale)
            val config = baseContext.resources.configuration
            config.locale = locale
            baseContext.resources.updateConfiguration(
                config,
                baseContext.resources.displayMetrics
            )
            CommonUtils.savePrefsLang(this, PrefConstants.LANGUAGE, "en")


        } else if (lang_type.equals("fr")) {
            val locale = Locale("fr")
            Locale.setDefault(locale)
            val config = baseContext.resources.configuration
            config.locale = locale
            baseContext.resources.updateConfiguration(
                config,
                baseContext.resources.displayMetrics
            )
            CommonUtils.savePrefsLang(this, PrefConstants.LANGUAGE, "fr")

        }

    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        setContentView(R.layout.activity_splash)


        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)

    }

    override fun onResume() {
        super.onResume()
        GrigoraApp().activity = this
    }

    public override fun onDestroy() {
        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }
        super.onDestroy()
    }
}
