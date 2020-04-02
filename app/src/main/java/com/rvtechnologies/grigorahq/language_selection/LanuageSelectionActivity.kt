package com.rvtechnologies.grigorahq.language_selection

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.rvtechnologies.grigorahq.MyApplication
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.navigation.NavigationActivity
import com.rvtechnologies.grigorahq.network.ConnectionNetwork
import com.rvtechnologies.grigorahq.network.EventBroadcaster
import com.rvtechnologies.grigorahq.network.NetworkConstants.Companion.change_language
import com.rvtechnologies.grigorahq.services.models.ChangeLangModel
import com.rvtechnologies.grigorahq.ui.login_signup.fragments.LogInFragment
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.change_language_num
import com.rvtechnologies.grigorahq.utils.CommonUtils
import com.rvtechnologies.grigorahq.utils.PrefConstants
import kotlinx.android.synthetic.main.activity_lanuage_selection.*
import java.util.*


class LanuageSelectionActivity : AppCompatActivity(),EventBroadcaster {

    var lang_type: String = ""
    var type: String = ""
    var lang_id=0

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        updateLang(this, "eng")
        setContentView(R.layout.activity_lanuage_selection)
        if (intent != null) {
            type = intent.getStringExtra("type_lang")
        }
        if (type.equals("splash")) {
            tv_choose.visibility = VISIBLE
            lang_back.visibility = View.GONE
            engSelection()
        } else {
            tv_choose.visibility = GONE
            if (CommonUtils.getPrefLang(this, PrefConstants.LANGUAGE).equals("en")) {
                engSelection()
            } else {
                frnSelection()

            }
        }

        ll_eng.setOnClickListener {
            engSelection()
        }

        ll_frn.setOnClickListener {
            frnSelection()
        }


//
//        rg_langauage.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
//            if (checkedId == R.id.rb_english) {
//                lang_type = "eng"
//                changeLang()
//            } else if (checkedId == R.id.rb_french) {
//                lang_type = "fr"
//                changeLang()
//            }
//        })
        tv_continue.setOnClickListener {
            if (type.equals("settings")) {
                setLangApi(lang_id)
                startActivity(Intent(this, NavigationActivity::class.java))
                finish()
            } else {
                setLangApi(lang_id)
                startActivity(Intent(this, LogInFragment::class.java))
            }
        }
        lang_back.setOnClickListener {
            finish()
        }
    }

    private fun frnSelection() {
        changeLang("fr")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tv_eng.setTextColor(resources.getColor(R.color.dark_gray, theme))
            ll_eng.background = resources.getDrawable(R.drawable.langauge_unselection_bg, theme)
            ll_frn.background = resources.getDrawable(R.drawable.lang_selection_bg, theme)
            tv_frn.setTextColor(resources.getColor(R.color.white, theme))

        } else {
            tv_eng.setTextColor(resources.getColor(R.color.dark_gray))
            ll_eng.background = resources.getDrawable(R.drawable.langauge_unselection_bg)
            ll_frn.background = resources.getDrawable(R.drawable.lang_selection_bg)
            tv_frn.setTextColor(resources.getColor(R.color.white))
        }
    }

    private fun engSelection() {
        changeLang("eng")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tv_eng.setTextColor(resources.getColor(R.color.white, theme))
            ll_eng.background = resources.getDrawable(R.drawable.lang_selection_bg, theme)
            ll_frn.background = resources.getDrawable(R.drawable.langauge_unselection_bg, theme)
            tv_frn.setTextColor(resources.getColor(R.color.dark_gray, theme))

        } else {
            tv_eng.setTextColor(resources.getColor(R.color.white))
            ll_eng.background = resources.getDrawable(R.drawable.lang_selection_bg)
            ll_frn.background = resources.getDrawable(R.drawable.langauge_unselection_bg)
            tv_frn.setTextColor(resources.getColor(R.color.dark_gray))
        }
    }

    private fun changeLang(lng: String) {
        if (lng.isEmpty()) {
            ConnectionNetwork.showSnack(
                false,
                this,
                layout_parent_lang,
                getString(R.string.please_select_a_language)
            )
        } else {
            if (lng.equals("eng")) {
                updateLang(this, "eng")
                CommonUtils.savePrefsLang(this, PrefConstants.LANGUAGE, "en")
                lang_id=1

            } else if (lng.equals("fr")) {
                updateLang(this, "fr")
                CommonUtils.savePrefsLang(this, PrefConstants.LANGUAGE, "fr")
                lang_id=2

            }

        }
        select_lang.setText(getString(R.string.select_language))
        tv_eng.setText(getString(R.string.english))
        tv_frn.setText(getString(R.string.french))
        tv_continue.setText(getString(R.string.continue_caps))
        tv_choose.setText(getString(R.string.wecome_to_n_grigora))
    }

    private fun setLangApi(i: Int) {
        var headerMAp = HashMap<String, String>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(this, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")

        ConnectionNetwork.getData(
            change_language+i,
            headerMAp,
            this,
            layout_parent_lang,
            change_language_num
        )
    }

    fun updateLang(context: Context, lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.locale = locale
        context.resources.updateConfiguration(
            config,
            context.resources.displayMetrics
        )
    }

    override fun broadcast(code: Int, data: Any?) {
        if(code==change_language_num){
            var pojo= Gson().fromJson(data.toString(),ChangeLangModel::class.java)
            if(pojo.status){
                if(pojo.data.language=="1"){
                    CommonUtils.savePrefsLang(this, PrefConstants.LANGUAGE, "en")
                }else{
                    CommonUtils.savePrefsLang(this, PrefConstants.LANGUAGE, "fr")
                }
            }
        }

    }
    override fun onPause() {
        super.onPause()
        MyApplication.instance!!.deRegisterListener(this)
    }
    override fun onResume() {
        super.onResume()
        MyApplication.instance!!.registerListener(this)
    }
}
