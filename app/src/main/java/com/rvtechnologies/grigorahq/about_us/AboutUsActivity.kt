package com.rvtechnologies.grigorahq.about_us

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.utils.CommonUtils
import com.rvtechnologies.grigorahq.utils.PrefConstants
import kotlinx.android.synthetic.main.activity_about_us.*

class AboutUsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        setContentView(R.layout.activity_about_us)
        iv_back.setOnClickListener {
            finish()
        }
        if (CommonUtils.getPrefLang(this, PrefConstants.LANGUAGE).equals("en")) {
            wv_abt.loadUrl("http://3.13.78.53/about_us/en/")
        } else
            wv_abt.loadUrl("http://3.13.78.53/about_us/fr/")

    }
}
