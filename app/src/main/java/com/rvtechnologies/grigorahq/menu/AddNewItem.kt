package com.rvtechnologies.grigorahq.menu

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import co.csadev.kwikpicker.KwikPicker
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.rvtechnologies.grigorahq.MyApplication
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.network.ConnectionNetwork
import com.rvtechnologies.grigorahq.network.EventBroadcaster
import com.rvtechnologies.grigorahq.network.NetworkConstants
import com.rvtechnologies.grigorahq.services.models.AddItemModel
import com.rvtechnologies.grigorahq.services.models.EditItemModel
import com.rvtechnologies.grigorahq.services.models.GetRestaurentItemsModel
import com.rvtechnologies.grigorahq.utils.BroadcastConstants
import com.rvtechnologies.grigorahq.utils.CommonUtils
import com.rvtechnologies.grigorahq.utils.PrefConstants
import kotlinx.android.synthetic.main.activity_add_new_item.*
import kotlinx.android.synthetic.main.content_add_new_item.*
import java.io.File

class AddNewItem : AppCompatActivity(), EventBroadcaster {
    private var img_url: String = ""
    var dish_type: Int = 3
    var cat_id: Int = 0
    var item_id: Int = 0
    var _type: String = ""
    var in_offer: Int = 1

    lateinit var pojo: GetRestaurentItemsModel.Data
    override fun broadcast(code: Int, data: Any?) {
        if (code == BroadcastConstants.add_item_num) {
            var pojo = Gson().fromJson(data.toString(), AddItemModel::class.java)
            if (pojo.status) {
                ConnectionNetwork.showSnack(
                    false,
                    this,
                    parent_layout_item,
                    getString(R.string.ite_added_successfully)
                );

                finish()

            } else {
                CommonUtils.showToast(this, getString(R.string.went_wrong))

            }
        } else if (code == BroadcastConstants.edit_item_num) {
            var pojo = Gson().fromJson(data.toString(), EditItemModel::class.java)
            if (pojo.status) {
                ConnectionNetwork.showSnack(
                    false,
                    this,
                    parent_layout_item,
                    pojo.message
                );
                finish()
            } else {
                CommonUtils.showToast(this, getString(R.string.went_wrong))

            }

        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_item)
        setSupportActionBar(toolbar)

        btnBack.setOnClickListener { onBackPressed() }
        if (intent != null) {
            if (intent.getStringExtra("typeItem").equals("AddNew")) {
                _type = intent.getStringExtra("typeItem");
                cat_id = intent.getIntExtra("cat_id", 0)
            } else if (intent.getStringExtra("typeItem").equals("edit")) {
                _type = intent.getStringExtra("typeItem");
                pojo = intent.getSerializableExtra("data") as GetRestaurentItemsModel.Data
                tv_itemTitle.setText(getString(R.string.edit_item))
                Glide.with(this).load(pojo.image).into(iv_dish)
                ed_name.setText(pojo.name)
                ed_normal_price.setText(pojo.price.toString())
                ed_offer_price.setText(pojo.offerPrice.toString())
                ed_desc.setText(pojo.description.toString())

                item_id = pojo.id
                cat_id = pojo.categoryId
                if (pojo.pureVeg.equals("0")) {
                    rb_veg.isChecked = true
                    dish_type = 0;
                } else if (pojo.pureVeg.equals("1")) {
                    rb_non.isChecked = true
                    dish_type = 1;
                } else if (pojo.pureVeg.equals("2")) {
                    rb_egg.isChecked = true
                    dish_type = 2;

                }
                if (pojo.inOffer.equals("0")) {
                    switch_offer.isChecked = true
                    in_offer = 0
                } else {
                    switch_offer.isChecked = false
                    in_offer = 1
                }

            }
        }
        radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rb_veg) {
                dish_type = 0;
            } else if (checkedId == R.id.rb_non) {
                dish_type = 1
            } else if (checkedId == R.id.rb_egg) {
                dish_type = 2
            }
        })
        btn_save.setOnClickListener {
            if (_type.equals("AddNew")) {
                if (validation()) {

                    setApi("add", NetworkConstants.add_item, BroadcastConstants.add_item_num)
                }
            } else if (_type.equals("edit")) {
                if (validation())
                    setApi("edit", NetworkConstants.edit_item, BroadcastConstants.edit_item_num)

            }
        }
        iv_dish.setOnClickListener {
            imageSelection()
        }

        switch_offer.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                in_offer = 0
            } else {
                // The switch is disabled
                in_offer = 1
            }
        }
    }

    private fun validation(): Boolean {
        if (ed_name.text.toString().length <= 0) {
            ConnectionNetwork.showSnack(
                false,
                this,
                parent_layout_item,
                getString(R.string.enter_dish_name)
            );
            return false
        } else if (ed_normal_price.text.toString().length <= 0) {
            ConnectionNetwork.showSnack(
                false,
                this,
                parent_layout_item,
                getString(R.string.please_enter_normal_price)
            );
            return false
        } else if (ed_normal_price.text.toString().toFloat() <= 0) {
            ConnectionNetwork.showSnack(
                false,
                this,
                parent_layout_item,
                getString(R.string.valid_price)
            );
            return false
        } else if (ed_offer_price.text.toString().length <= 0) {
            ConnectionNetwork.showSnack(
                false,
                this,
                parent_layout_item,
                getString(R.string.please_enter_offer_price)
            );
            return false
        } else if (ed_offer_price.text.toString().toFloat() <= 0) {
            ConnectionNetwork.showSnack(
                false,
                this,
                parent_layout_item,
                getString(R.string.valid_offer_price)
            );
            return false
        } else if (ed_offer_price.text.toString().toFloat() > ed_normal_price.text.toString().toFloat()) {
            ConnectionNetwork.showSnack(
                false,
                this,
                parent_layout_item,
                getString(R.string.less_than_offer_price)
            );
            return false
        } else if (ed_desc.text.toString().length <= 0) {
            ConnectionNetwork.showSnack(
                false,
                this,
                parent_layout_item,
                getString(R.string.please_enter_description)
            );
            return false
        } else if (dish_type == 3) {
            ConnectionNetwork.showSnack(
                false,
                this,
                parent_layout_item,
                getString(R.string.please_select_one_of_the_dish_type)
            );
            return false
        } else if (_type.equals("AddNew") && img_url.length <= 0) {
            ConnectionNetwork.showSnack(
                false,
                this,
                parent_layout_item,
                getString(R.string.please_select_category_image)
            )
            return false
        }
        return true
    }

    override fun onPause() {
        super.onPause()
        MyApplication.instance!!.deRegisterListener(this)

    }

    override fun onResume() {
        super.onResume()
        MyApplication.instance!!.registerListener(this)

    }

    fun imageSelection() {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                val bottomSheetDialogFragment = KwikPicker.Builder(
                    this@AddNewItem,
                    imageProvider = { iv_dish, uri ->
                        Glide.with(this@AddNewItem)
                            .load(uri)
                            .into(iv_dish)
                    },
                    onImageSelectedListener = { uri: Uri ->
                        iv_dish.post {
                            3
                            Glide.with(this@AddNewItem)
                                .load(uri)
                                .into(iv_dish)

                            img_url = uri.path!!
                        }
                    },
                    peekHeight = 1200
                )
                    .create(this@AddNewItem)
                bottomSheetDialogFragment.show(this@AddNewItem.supportFragmentManager)
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(
                    this@AddNewItem,
                    "Permission Denied\n" + deniedPermissions.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        TedPermission.with(this@AddNewItem)
            .setPermissionListener(permissionListener)
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .check()


    }

    fun setApi(api_type: String, url: String, url_num: Int) {
        var data = HashMap<String, Any>()
        var fileMap = HashMap<String, File>()
        var headerMAp = HashMap<String, String>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(this, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")
        data.put("name", ed_name.text.toString())
        data.put("category_id", cat_id)
        data.put("description", ed_desc.text.toString())
        data.put("price", ed_normal_price.text.toString())
        data.put("offer_price", ed_offer_price.text.toString())
        data.put("pure_veg", dish_type)
        data.put("in_offer", in_offer)

        if (api_type.equals("edit")) {
            data.put("item_id", item_id)
        }

        if (img_url.isNullOrEmpty()) {
            data.put("image", File(img_url).absoluteFile)
        } else {
            fileMap.put("image", File(img_url).absoluteFile)
        }

        var list = ArrayList<String>()
        list.add("2")
        ConnectionNetwork.getData(
            url,
            headerMAp,
            ConnectionNetwork.convertRequestBodyfromMap(this, data),
            ConnectionNetwork.converRequestBodyFromMapImage(fileMap, list),
            this,
            parent_layout_item,
            url_num
        )


    }


}
