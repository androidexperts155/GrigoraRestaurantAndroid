package com.rvtechnologies.grigorahq.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.about_us.AboutUsActivity
import com.rvtechnologies.grigorahq.contact_us.ContactUsActivity
import com.rvtechnologies.grigorahq.language_selection.LanuageSelectionActivity
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tv_contact.setOnClickListener {
            startActivity(Intent(activity, ContactUsActivity::class.java))
        }

        tv_reset_password.setOnClickListener {
            startActivity(Intent(activity!!, ResetPasswordActivity::class.java))
        }
        tv_about_us.setOnClickListener {
            startActivity(Intent(activity!!, AboutUsActivity::class.java))
        }

        tv_change_lang.setOnClickListener {
            startActivity(
                Intent(
                    activity,
                    LanuageSelectionActivity::class.java
                ).putExtra("type_lang", "settings")
            )

        }

    }


    companion object {

        @JvmStatic
        fun newInstance() = SettingsFragment()
    }
}

