package com.rvtechnologies.grigorahq.categories

import android.Manifest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import co.csadev.kwikpicker.KwikPicker
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.rvtechnologies.grigorahq.MyApplication
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.categories.adapter.ItemCatAdapter
import com.rvtechnologies.grigorahq.menu.ChooseCatAdapter
import com.rvtechnologies.grigorahq.menu.RecClick
import com.rvtechnologies.grigorahq.network.ConnectionNetwork
import com.rvtechnologies.grigorahq.network.EventBroadcaster
import com.rvtechnologies.grigorahq.network.NetworkConstants
import com.rvtechnologies.grigorahq.network.NetworkConstants.Companion.update_item
import com.rvtechnologies.grigorahq.services.models.*
import com.rvtechnologies.grigorahq.utils.*
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.add_item_num
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.delete_item_num
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.get_cuisine_num
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.update_item_num
import com.rvtechnologies.grigorahq.utils.PrefConstants.TOKEN
import kotlinx.android.synthetic.main.activity_add_category.*
import kotlinx.android.synthetic.main.content_add_category.*
import kotlinx.android.synthetic.main.dialog_contact_us.view.*
import java.io.File


class AddCategory : AppCompatActivity(), EventBroadcaster, IAddCategory, IAddSubCategory, RecClick,
    View.OnClickListener {


    var category: String = ""
    var cusine_id: Int = 0
    lateinit var list: ArrayList<DishModel.Data.ItemCategory>
    var catList = ArrayList<CusinesModel.Data>()
    var itemType: String = "3"
    var edit_dish = false
    var item_id: Int = 0
    lateinit var chooseCatAdapter: ChooseCatAdapter
    lateinit var dishModel: DishModel.Data

    override fun broadcast(code: Int, data: Any?) {
        if (code == add_item_num) {
            var pojo = Gson().fromJson(data.toString(), AddItemModel::class.java)
            if (pojo.status) {
                showSetTimeDialog()
            }

        } else if (code == update_item_num) {
            var pojo = Gson().fromJson(data.toString(), UpdateItemModel::class.java)
            if (pojo.status) {
                finish()
            }
        } else if (code == delete_item_num) {
            var pojo = Gson().fromJson(data.toString(), DeleteItemModel::class.java)
            if (pojo.status) {
                finish()
            }

        } else if (code == get_cuisine_num) {
            var pojo = Gson().fromJson(data.toString(), CusinesModel::class.java)
            if (pojo.status) {

                if (catList.size > 0) {
                    catList.clear()
                }
                if (pojo.data.size > 0)
                    catList.addAll(pojo.data)
                chooseCatAdapter = ChooseCatAdapter(this, catList)
                rv_choose_cat.adapter = chooseCatAdapter

            }


        }
    }

    private var img_url: String = ""

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        setContentView(R.layout.activity_add_category)
        btnBack.setOnClickListener { onBackPressed() }
        list = ArrayList()
        rv_subCat.adapter = ItemCatAdapter(list, this)
        rv_subCat.layoutManager = LinearLayoutManager(this)
        rv_choose_cat.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        tv_item_title.setText(getString(R.string.add_dish))
        btnSave.setText(getString(R.string.save))
        getCousines()
        rb_custom.setOnClickListener(this)

        iv_item.setOnClickListener {
            imageSelection()
        }


        ll_addItem.setOnClickListener {
            addItem()
        }

        btnSave.setOnClickListener {
            saveItem()
        }

        rv_subCat.isNestedScrollingEnabled = false
        if (ed_editcat.isVisible) {
            category = ed_editcat.text.toString()
        }
        rg_type.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rb_veg) {
                itemType = "1"
            } else if (checkedId == R.id.rb_non) {
                itemType = "0"
            } else if (checkedId == R.id.rb_egg) {
                itemType = "2"
            }

        })

        if (intent != null && intent.getSerializableExtra("edit_item") != null) {
            dishModel = intent.getSerializableExtra("edit_item") as DishModel.Data
            setEditIntentdata()
        }

        iv_delete.setOnClickListener {
            setDeleteItemApi()
        }

    }


    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.rb_custom -> {
                ed_editcat.visibility = VISIBLE
            }
        }
    }

    private fun setEditIntentdata() {
        edit_dish = true
        tv_item_title.setText(getString(R.string.update_dish))
        btnSave.setText(getString(R.string.update))
        iv_delete.visibility = VISIBLE
        item_id = dishModel.id
        ed_item_name.setText(dishModel.name)
        ed_item_desc.setText(dishModel.description)
        ed_est_time.setText(dishModel.approx_prep_time)
        Glide.with(this).load(dishModel.image).into(iv_item)
        ed_item_price.setText(dishModel.price.toString())
        if (dishModel.pureVeg.equals("0")) {
            rb_non.isChecked = true
        } else if (dishModel.pureVeg.equals("1")) {
            rb_veg.isChecked = true
        } else if (dishModel.pureVeg.equals("2")) {
            rb_egg.isChecked = true
        } else {
            category = dishModel.categoryName
            ed_editcat.setText(dishModel.categoryName)
        }

        for (i in 0 until dishModel.itemCategories.size) {
            val pojo = DishModel.Data.ItemCategory(
                dishModel.itemCategories.get(i).updatedAt,
                dishModel.itemCategories.get(i).frenchName,
                dishModel.itemCategories.get(i).id,
                dishModel.itemCategories.get(i).itemId,
                dishModel.itemCategories.get(i).itemSubCategory,
                dishModel.itemCategories.get(i).name,
                dishModel.itemCategories.get(i).selection,
                dishModel.itemCategories.get(i).status,
                dishModel.itemCategories.get(i).updatedAt
            )
            list.add(pojo)
            rv_subCat.adapter?.notifyItemInserted(list.size)
        }
    }
    private fun showSetTimeDialog() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_contact_us, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)

        val mAlertDialog = mBuilder.show()

        mDialogView.ok.setOnClickListener {
            mAlertDialog.dismiss()
            finish()
        }
    }
    private fun saveItem() {
        Log.e("My pojo :::: ", list.toString())
        if (btnSave.text.equals(getString(R.string.save))) {
            if (validation())
                setApi(NetworkConstants.add_item, add_item_num)
        } else {
            setApi(update_item, update_item_num)
        }
    }

    private fun addItem() {
        val pojo = DishModel.Data.ItemCategory(
            "",
            "",
            0,
            0,
            ArrayList<DishModel.Data.ItemCategory.ItemSubCategory>(),
            "",
            "1",
            "",
            ""
        )
        list.add(pojo)
        rv_subCat.adapter?.notifyItemInserted(list.size)

    }

    fun imageSelection() {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                val bottomSheetDialogFragment = KwikPicker.Builder(
                    this@AddCategory,
                    imageProvider = { image_view, uri ->
                        Glide.with(this@AddCategory)
                            .load(uri)
                            .into(image_view)
                    },
                    onImageSelectedListener = { uri: Uri ->
                        iv_item.post {
                            Glide.with(this@AddCategory)
                                .load(uri)
                                .into(iv_item)
                            iv_upload.visibility = GONE

                            img_url = uri.path!!
                        }
                    },
                    peekHeight = 1200
                )
                    .create(this@AddCategory)
                bottomSheetDialogFragment.show(this@AddCategory.supportFragmentManager)
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(
                    this@AddCategory,
                    "Permission Denied\n" + deniedPermissions.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        TedPermission.with(this@AddCategory)
            .setPermissionListener(permissionListener)
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .check()


    }

    override fun onPause() {
        super.onPause()
        MyApplication.instance!!.deRegisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        MyApplication.instance!!.registerListener(this)

    }

    private fun validation(): Boolean {
        if (ed_item_name.text.toString().length <= 0) {
            ConnectionNetwork.showSnack(
                false,
                this,
                parent_layout,
                getString(R.string.please_enter_item_name)
            );
            return false
        } else if (ed_item_price.text.toString().length <= 0) {
            ConnectionNetwork.showSnack(
                false,
                this,
                parent_layout,
                getString(R.string.please_enter_item_price)
            );
            return false
        } else if (ed_item_desc.text.length <= 0) {
            ConnectionNetwork.showSnack(
                false,
                this,
                parent_layout,
                getString(R.string.please_enter_item_description)
            )
            return false
        } else if (itemType.equals("3")) {
            ConnectionNetwork.showSnack(
                false,
                this,
                parent_layout,
                getString(R.string.please_select_one_of_the_dish_type)
            )
            return false
        } else if (ed_est_time.text.isNullOrEmpty()) {
            ConnectionNetwork.showSnack(
                false,
                this,
                parent_layout,
                getString(R.string.error_preparing_time)
            )
            return false
        } else if (category.equals("")) {
            ConnectionNetwork.showSnack(
                false,
                this,
                parent_layout,
                getString(com.rvtechnologies.grigorahq.R.string.please_select_one_of_the_category_type)
            )
            return false
        } else if (img_url.length <= 0) {
            ConnectionNetwork.showSnack(
                false,
                this,
                parent_layout,
                getString(R.string.please_select_category_image)
            )
            return false
        } else if (cusine_id == 0) {
            ConnectionNetwork.showSnack(
                false,
                this,
                parent_layout,
                getString(R.string.please_select_cusine)
            )
        } else if (!filter()) {
            return false
        }
        return true
    }

    fun filter(): Boolean {
        var valid = true

        for (item in list) {
            if (item.itemSubCategory.size > 0) {

                for (sub in item.itemSubCategory) {
                    if (item.name.isNullOrBlank() || sub.name.isNullOrBlank() || sub.addOnPrice.isNullOrBlank()) {
                        valid = false
                        // show toast for cat do not have sub cat
                        ConnectionNetwork.showSnack(
                            false,
                            this,
                            parent_layout,
                            getString(R.string.error_subcat)

                        )
                        break

                    }

                }
            } else {
                ConnectionNetwork.showSnack(
                    false,
                    this,
                    parent_layout,
                    getString(R.string.error_type_price_subcat)

                )
                valid = false
                break
            }
//            if(item.name.isNullOrBlank()){
//                ConnectionNetwork.showSnack(
//                    false,
//                    this,
//                    parent_layout,
//                    getString(R.string.error_enter_subcat)
//
//                )
//                valid = false
//                break
//            }
        }



        return valid

    }

    fun setApi(url: String, url_id: Int) {
        if (cusine_id == 0) {
            ConnectionNetwork.showSnack(
                false,
                this,
                parent_layout,
                getString(R.string.please_select_cusine)
            )
        } else {
            val json = Gson().toJson(list)
            var data = HashMap<String, Any>()
            var fileMap = HashMap<String, File>()
            var headerMAp = HashMap<String, String>()
            headerMAp.put("Authorization", CommonUtils.getPrefValue(this, TOKEN))
            headerMAp.put("Accept", "application/json")
            data.put("name", ed_item_name.text.toString())
            data.put("description", ed_item_desc.text.toString())
            data.put("price", ed_item_price.text.toString())
            data.put("offer_price", "0")
            data.put("pure_veg", itemType)
            data.put("approx_prep_time", ed_est_time.text.toString())
            data.put("item_categories", json)


            if (url.equals("edit-item")) {
                data.put("item_id", item_id)
                data.put("cuisine_id", cusine_id)
            } else {
                data.put("cuisine_id", cusine_id)
            }

            if (img_url.isEmpty()) {
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
                parent_layout, url_id
            )

        }
    }

    override fun onCategoryUpdated(pos: Int, categoryModel: DishModel.Data.ItemCategory) {
        list[pos] = categoryModel
    }

    override fun onAdOnAdded(catPosition: Int) {
        list[catPosition].itemSubCategory.add(
            DishModel.Data.ItemCategory.ItemSubCategory("", "", "", 0, 0, "", "", "")
        )
        rv_subCat.adapter!!.notifyItemChanged(catPosition)
    }

    override fun onAddOnUpdated(
        subCatPosition: Int,
        addOnPosition: Int,
        itemSubCategory: DishModel.Data.ItemCategory.ItemSubCategory
    ) {
        list[subCatPosition].itemSubCategory[addOnPosition] = itemSubCategory
    }

    override fun onItemRemove(catPosition: Int) {
        list.removeAt(catPosition)
        rv_subCat.adapter?.notifyDataSetChanged()
    }

    private fun setDeleteItemApi() {
        var headerMAp = HashMap<String, String>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(this, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")

        ConnectionNetwork.getData(
            NetworkConstants.delete_item + item_id,
            headerMAp,
            this,
            parent_layout,
            delete_item_num
        )

    }

    private fun getCousines() {
        var headerMAp = HashMap<String, String>()
        headerMAp.put(
            "Authorization", CommonUtils.getPrefValue(this, PrefConstants.TOKEN)
        )
        headerMAp.put("Accept", "application/json")

        ConnectionNetwork.getData(
            NetworkConstants.get_cuisine,
            headerMAp,
            this,
            parent_layout,
            BroadcastConstants.get_cuisine_num
        )

    }

    override fun click(i: Int) {

        for (item in catList) {
            item.isChecked = item.name.equals(catList.get(i).name)
            Log.e("data", catList.get(i).name)
            category = catList.get(i).name
            cusine_id = catList.get(i).id
        }
        chooseCatAdapter.notifyDataSetChanged()

    }
}
