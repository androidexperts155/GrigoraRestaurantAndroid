package com.rvtechnologies.grigorahq.complete_profile

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.rvtechnologies.grigorahq.R
import kotlinx.android.synthetic.main.activity_search_address.*

class SearchAddressActivity : AppCompatActivity() {
    lateinit var handler: Handler
    lateinit var controller: AddAddress_Controller
    var currentLocation = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        setContentView(R.layout.activity_search_address)


        controller = AddAddress_Controller(this)

        iv_back.setOnClickListener {
            finish()
        }


        ed_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length!! > 0) {
                    android.transition.TransitionManager.beginDelayedTransition(parent_layout_main)

                    if (s.length > 2) {
                        if (::handler.isInitialized)
                            handler.removeCallbacksAndMessages(null)
                        else
                            handler = Handler()

                        if (!controller.temp.equals(ed_search.text.toString())) {
                            controller.ExternalThread(count, s).execute()

                            controller.handleVisibility(2)
                        }
                    }
                } else {
                    android.transition.TransitionManager.beginDelayedTransition(parent_layout_main)

//            vw.list_search.visibility = View.GONE
                    controller.handleVisibility(1)
                    controller.placesList.clear()
                }
            }
        })
        list_search.onItemClickListener =
            AdapterView.OnItemClickListener { parent, v, position, id ->
                controller.listItemClicked(position)
            }
    }


}
