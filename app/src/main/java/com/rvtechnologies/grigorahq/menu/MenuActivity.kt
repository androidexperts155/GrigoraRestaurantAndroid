package com.rvtechnologies.grigorahq.menu

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.rvtechnologies.grigorahq.MyApplication
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.network.ConnectionNetwork
import com.rvtechnologies.grigorahq.network.EventBroadcaster
import com.rvtechnologies.grigorahq.network.NetworkConstants
import com.rvtechnologies.grigorahq.network.NetworkConstants.Companion.delete_item
import com.rvtechnologies.grigorahq.services.models.DeleteItemModel
import com.rvtechnologies.grigorahq.services.models.GetRestaurentItemsModel
import com.rvtechnologies.grigorahq.utils.*
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.delete_item_num
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.content_menu.*

class MenuActivity : AppCompatActivity(), EventBroadcaster, IDeleteOnClick {
    lateinit var mdialog: AlertDialog;
    override fun deleteOnClick(pojo: GetRestaurentItemsModel.Data, dialog: AlertDialog) {
        mdialog = dialog;
        setDeleteItemApi(pojo.id)
    }

    val mList: ArrayList<GetRestaurentItemsModel.Data> = ArrayList()
    var cat_id: Int = 0

    override fun broadcast(code: Int, data: Any?) {
        if (code == BroadcastConstants.category_item_list_num) {
            var pojo = Gson().fromJson(data.toString(), GetRestaurentItemsModel::class.java)
            if (pojo.status) {
                if (mList.size > 0) {
                    mList.clear()
                }
                mList.addAll(pojo.data)
                if (mList.size <= 0) {
                    rvMenu.visibility = GONE
                    ll_empty.visibility = VISIBLE
                    iv_add.visibility = GONE
                } else {
                    rvMenu.visibility = VISIBLE
                    ll_empty.visibility = GONE
                    iv_add.visibility = VISIBLE
                    rvMenu.adapter = MenuAdapter(mList, this)
                }

            } else {
                CommonUtils.showToast(this, getString(R.string.went_wrong))

            }
        } else if (code == delete_item_num) {
            var pojo = Gson().fromJson(data.toString(), DeleteItemModel::class.java)
            if (pojo.status) {
                mdialog.dismiss()
                ConnectionNetwork.showSnack(
                    false,
                    this,
                    parentlayout_item,
                    pojo.message
                )
                setApi()
            }


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        setSupportActionBar(toolbar)

        btnBack.setOnClickListener { onBackPressed() }
        rvMenu.layoutManager = GridLayoutManager(this, 2)
        rvMenu.addItemDecoration(
            GridSpacingItemDecoration(
                2,
                CommonMethods.dpsToPixel(this, 8),
                true
            )
        )
        if (intent != null) {
            cat_id = intent.getIntExtra("cat_id", 0)
        }
        button6.setOnClickListener {
            startActivity(
                Intent(this, AddNewItem::class.java).putExtra(
                    "typeItem",
                    "AddNew"
                ).putExtra("cat_id", cat_id)
            )
        }
        iv_add.setOnClickListener {
            startActivity(
                Intent(this, AddNewItem::class.java).putExtra(
                    "typeItem",
                    "AddNew"
                ).putExtra("cat_id", cat_id)
            )
        }
    }

    fun setApi() {
        var headerMAp = HashMap<String, String>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(this, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")

        ConnectionNetwork.getData(
            NetworkConstants.category_item_list + cat_id,
            headerMAp,
            this,
            parentlayout_item,
            BroadcastConstants.category_item_list_num
        )

    }

    override fun onPause() {
        super.onPause()
        MyApplication.instance!!.deRegisterListener(this)

    }

    override fun onResume() {
        super.onResume()
        MyApplication.instance!!.registerListener(this)
        setApi()
    }

    private fun setDeleteItemApi(id: Int) {
        var headerMAp = HashMap<String, String>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(this, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")

        ConnectionNetwork.getData(
            delete_item + id,
            headerMAp,
            this,
            parentlayout_item,
            delete_item_num
        )

    }


}

