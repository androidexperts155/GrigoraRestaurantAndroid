package com.rvtechnologies.grigorahq.view.ui.login_signup.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rvtechnologies.grigorahq.R


class ChooseLanguageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_choose_language, container, false)
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            ChooseLanguageFragment()
    }
}
