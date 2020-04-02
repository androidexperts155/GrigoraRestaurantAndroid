package com.rvtechnologies.grigorahq

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.rvtechnologies.grigorahq.navigation.NavigationActivity.Companion.drawer_layout
import com.rvtechnologies.grigorahq.network.ConnectionNetwork
import com.rvtechnologies.grigorahq.network.EventBroadcaster
import com.rvtechnologies.grigorahq.network.NetworkConstants
import com.rvtechnologies.grigorahq.network.NetworkConstants.Companion.quiz_question
import com.rvtechnologies.grigorahq.services.models.JoinQuizModel
import com.rvtechnologies.grigorahq.services.models.QuizModel
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.join_quiz_num
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.quiz_question_num
import com.rvtechnologies.grigorahq.utils.CommonUtils
import com.rvtechnologies.grigorahq.utils.PrefConstants
import kotlinx.android.synthetic.main.fragment_quiz.*
import kotlinx.android.synthetic.main.fragment_quiz.view.*
import kotlinx.android.synthetic.main.no_driver_assigned.*


class QuizFragment : Fragment(), EventBroadcaster {
    private var root: View? = null
    private var quiz_id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_quiz, container, false)  // initialize it here
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setApi()
        iv_menu.setOnClickListener {
            drawer_layout =
                (activity as Activity).findViewById(R.id.drawerlayout);
            if (drawer_layout!!.isDrawerVisible(GravityCompat.START)) {
                drawer_layout!!.closeDrawer(GravityCompat.START)
            } else {
                drawer_layout!!.openDrawer(GravityCompat.START)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            QuizFragment().apply {
                arguments = Bundle().apply {}
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

    private fun setApi() {
        var headerMAp = HashMap<String, String>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(activity!!, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")
        ConnectionNetwork.getData(
            quiz_question,
            headerMAp,
            activity!!,
            root!!.quiz_scrool,
            quiz_question_num
        )
    }

    private fun applyQuiz() {
        var headerMAp = HashMap<String, Any?>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(activity!!, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")
        var data = HashMap<String, Any?>()
        data.put("question_id", quiz_id)
        ConnectionNetwork.postFormData(
            NetworkConstants.join_quiz,
            headerMAp,
            data,
            "",
            activity!!,
            quiz_scrool,
            join_quiz_num
        )
    }

    override fun broadcast(code: Int, data: Any?) {
        if (code == quiz_question_num) {
            var pojo = Gson().fromJson(data.toString(), QuizModel::class.java)
            if (pojo.status) {
                quiz_id = pojo.data.id
                tv_off.setText(pojo.data.offerPoints + getString(R.string.points))
                if (pojo.data.joinQuiz == 1) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        btn_join.setBackgroundColor(
                            resources.getColor(
                                R.color.grey,
                                activity!!.theme
                            )
                        )
                    } else
                        btn_join.setBackgroundColor(resources.getColor(R.color.grey))

                    btn_join.setOnClickListener {
                        val mDialogView =
                            LayoutInflater.from(context).inflate(R.layout.no_driver_assigned, null)
                        val mBuilder = AlertDialog.Builder(context)
                            .setView(mDialogView)

                        val mAlertDialog = mBuilder.show()
                        mAlertDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        mAlertDialog.tv_title.setText(getString(R.string.join_quiz))
                        mAlertDialog.tv_msg.setText(getString(R.string.you_have_already_joined_this_quiz))
                        mAlertDialog.tv_ok.setOnClickListener {
                            mAlertDialog.dismiss()
                        }
                    }

                } else {
                    btn_join.setOnClickListener {
                        if(!btn_join.equals(getString(R.string.joined)))
                            applyQuiz()

                    }
                }
            }
        } else if (code == join_quiz_num) {
            var pojo = Gson().fromJson(data.toString(), JoinQuizModel::class.java)
            if (pojo.status) {
                btn_join.setText(getString(R.string.joined))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    btn_join.setBackgroundColor(resources.getColor(R.color.grey, activity!!.theme))
                } else {
                    btn_join.setBackgroundColor(resources.getColor(R.color.grey))
                }
            }
        }
    }
}
