package com.rvtechnologies.grigorahq.categories

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import co.csadev.kwikpicker.KwikPicker
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.rvtechnologies.grigorahq.MyApplication
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.categories.adapter.CategoriesAdapter
import com.rvtechnologies.grigorahq.navigation.NavigationActivity
import com.rvtechnologies.grigorahq.network.ConnectionNetwork
import com.rvtechnologies.grigorahq.network.EventBroadcaster
import com.rvtechnologies.grigorahq.network.NetworkConstants
import com.rvtechnologies.grigorahq.network.NetworkConstants.Companion.change_item_availabilty_status
import com.rvtechnologies.grigorahq.network.NetworkConstants.Companion.mark_unmark_featured
import com.rvtechnologies.grigorahq.services.models.DeleteCategoryModel
import com.rvtechnologies.grigorahq.services.models.DishModel
import com.rvtechnologies.grigorahq.services.models.EditCategoryModel
import com.rvtechnologies.grigorahq.services.models.ItemAvailabilityModel
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.change_item_availabilty_status_num
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.delete_category_num
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.edit_category_num
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.mark_unmark_featured_num
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.restaurant_items_list_num
import com.rvtechnologies.grigorahq.utils.CommonUtils
import com.rvtechnologies.grigorahq.utils.IOnRecyclerItemClick
import com.rvtechnologies.grigorahq.utils.PrefConstants
import kotlinx.android.synthetic.main.activity_categories.*
import kotlinx.android.synthetic.main.content_categories.*
import java.io.File
import java.util.regex.Pattern

class Categories : Fragment(), EventBroadcaster, IOnRecyclerItemClick {
    override fun onLongClick(item: Any) {

    }

    override fun onSwitchAvail(item: Any, status: Int) {
        if (item is DishModel.Data) {
            setItemAvail(item.id, status)
        }
    }

    override fun onFeatured(item: Any, status: Int) {
        if(item is DishModel.Data){
            setFeaturedDish(item.id,status)
        }
    }

    private fun setFeaturedDish(id: Int, status: Int) {
        var headerMAp = HashMap<String, Any?>()
        var dataMap = HashMap<String, Any?>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(activity, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")
        dataMap.put("item_id", id)
        dataMap.put("status", status)

        ConnectionNetwork.postFormData(
            mark_unmark_featured,
            headerMAp,
            dataMap,
            "",
            activity!!,
            parent_layout_cat,
            mark_unmark_featured_num
        )
    }


    var cat_id: Int = 0
    var img_url: String = ""
    var img_url_dialog: String = ""
    var cat_name: String = ""
    lateinit var itemView: View
    lateinit var dialog: AlertDialog
    lateinit var mAdapter: CategoriesAdapter


    override fun onClick(item: Any, adapterPosition: Int) {
        if (item is DishModel.Data) {

            startActivity(Intent(activity, AddCategory::class.java).putExtra("edit_item", item))
        }
    }

    val mList: ArrayList<DishModel.Data> = ArrayList()


    override fun broadcast(code: Int, data: Any?) {
        if (code == restaurant_items_list_num) {
            try {
                var pojo = Gson().fromJson(data.toString(), DishModel::class.java)

                if (pojo.status) {
                    if (mList.size > 0) {
                        mList.clear()
                    }
                    if (pojo.data.size > 0) {
                        mList.addAll(pojo.data)
                        ll_empty.visibility = GONE
                        ed_search.visibility = VISIBLE
                        rvCategories.visibility = VISIBLE
                        iv_add.visibility = VISIBLE
                        mAdapter = CategoriesAdapter(mList, this)
                        rvCategories.adapter = mAdapter
                    } else {
                        ll_empty.visibility = VISIBLE
                        ed_search.visibility = GONE

                    }
                } else {
                    CommonUtils.showToast(activity, resources.getString(R.string.went_wrong))
                    ed_search.visibility = GONE


                }
            } catch (e: Exception) {
                ll_empty.visibility = VISIBLE
                rvCategories.visibility = GONE
                CommonUtils.showToast(activity, resources.getString(R.string.not_found))
                ed_search.visibility = GONE
            }


        } else if (code == delete_category_num) {
            var pojo = Gson().fromJson(data.toString(), DeleteCategoryModel::class.java)
            if (pojo.status) {
                ConnectionNetwork.showSnack(false, activity!!, parent_layout_cat, pojo.message)
                setApi(NetworkConstants.restaurant_items_list, restaurant_items_list_num)
            } else {
                CommonUtils.showToast(activity, getString(R.string.went_wrong))
            }
        } else if (code == edit_category_num) {
            var pojo = Gson().fromJson(data.toString(), EditCategoryModel::class.java)
            if (pojo.status) {
                ConnectionNetwork.showSnack(
                    false,
                    activity!!,
                    parent_layout_cat,
                    pojo.message
                )
                dialog.dismiss()
                setApi(
                    NetworkConstants.restaurant_items_list, restaurant_items_list_num
                )

            } else {
                CommonUtils.showToast(activity, getString(R.string.went_wrong))
            }
        } else if (code == change_item_availabilty_status_num) {
            var pojo = Gson().fromJson(data.toString(), ItemAvailabilityModel::class.java)
            if (pojo.status) {
                setApi(
                    NetworkConstants.restaurant_items_list, restaurant_items_list_num
                )
            }
        }
            else if (code == mark_unmark_featured_num) {
            var pojo = Gson().fromJson(data.toString(), ItemAvailabilityModel::class.java)
            if (pojo.status) {
//                setApi(
//                    NetworkConstants.restaurant_items_list, restaurant_items_list_num
//                )
            }

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvCategories.layoutManager = LinearLayoutManager(activity!!)
        mAdapter = CategoriesAdapter(mList, this)
        menu_dish.setOnClickListener {
            NavigationActivity.drawer_layout =
                (activity as Activity).findViewById(R.id.drawerlayout)
            if (NavigationActivity.drawer_layout!!.isDrawerVisible(GravityCompat.START)) {
                NavigationActivity.drawer_layout!!.closeDrawer(GravityCompat.START)
            } else {
                NavigationActivity.drawer_layout!!.openDrawer(GravityCompat.START)
            }
        }

        ed_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                rv_brands.visibility = GONE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        iv_add.setOnClickListener(View.OnClickListener {

            startActivity(Intent(activity, AddCategory::class.java))
        })


        setApi(
            NetworkConstants.restaurant_items_list, restaurant_items_list_num
        )


    }


    private fun setEditCategoryApi(edCat: TextInputEditText) {
        var data = HashMap<String, Any>()
        var fileMap = HashMap<String, File>()
        var headerMAp = HashMap<String, String>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(activity, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")

        data.put("category_id", cat_id)
        data.put("name", edCat.text.toString())



        if (img_url.isNullOrEmpty()) {
            data.put("image", File(img_url).absoluteFile)
        } else {
            fileMap.put("image", File(img_url).absoluteFile)
        }

        var list = ArrayList<String>()
        list.add("2")
        ConnectionNetwork.getData(
            NetworkConstants.edit_category,
            headerMAp,
            ConnectionNetwork.convertRequestBodyfromMap(activity!!, data),
            ConnectionNetwork.converRequestBodyFromMapImage(fileMap, list),
            activity!!,
            parent_layout_cat,
            edit_category_num
        )
    }

    private fun setItemAvail(id: Int, status: Int) {
        var headerMAp = HashMap<String, Any?>()
        var dataMap = HashMap<String, Any?>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(activity, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")
        dataMap.put("item_id", id)
        dataMap.put("status", status)

        ConnectionNetwork.postFormData(
            change_item_availabilty_status,
            headerMAp,
            dataMap,
            "",
            activity!!,
            parent_layout_cat,
            change_item_availabilty_status_num
        )
    }

    override fun onPause() {
        super.onPause()
        MyApplication.instance!!.deRegisterListener(this)

    }

    override fun onResume() {
        super.onResume()
        MyApplication.instance!!.registerListener(this)
        setApi(
            NetworkConstants.restaurant_items_list, restaurant_items_list_num
        )
    }

    fun setApi(url: String, broadcast_num: Int) {

        var headerMAp = HashMap<String, Any?>()
        var dataMap = HashMap<String, Any?>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(activity, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")
        dataMap.put("restaurant_id", CommonUtils.getPrefValue(activity, PrefConstants.ID))

        ConnectionNetwork.postFormData(
            url,
            headerMAp,
            dataMap,
            "",
            activity!!,
            parent_layout_cat,
            broadcast_num
        )
    }

    fun imageSelection(dialogImg: ImageView) {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                val bottomSheetDialogFragment = KwikPicker.Builder(
                    activity!!,
                    imageProvider = { dialogImg, uri ->
                        Glide.with(activity!!)
                            .load(uri)
                            .into(dialogImg)
                    },
                    onImageSelectedListener = { uri: Uri ->
                        dialogImg.post {
                            Glide.with(activity!!)
                                .load(uri)
                                .into(dialogImg)

                            img_url = uri.path!!

                        }
                    },
                    peekHeight = 1200
                )
                    .create(activity!!)
                bottomSheetDialogFragment.show(fragmentManager!!)
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(
                    activity!!,
                    "Permission Denied\n" + deniedPermissions.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        TedPermission.with(activity!!)
            .setPermissionListener(permissionListener)
            .setDeniedMessage("If you reject permission,you can not use activity service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .check()


    }

    private fun filter(text: String) {
        //new array list that will hold the filtered data
        val filterdNames = ArrayList<DishModel.Data>()

        //looping through existing elements
        for (s in mList) {
            //if the existing elements contains the search input
            if (Pattern.compile(
                    Pattern.quote(text),
                    Pattern.CASE_INSENSITIVE
                ).matcher(s.name).find()
            ) {
                //adding the element to filtered list
                filterdNames.add(s)

            }

        }


        //calling a method of the adapter class and passing the filtered list

        mAdapter.filterList(filterdNames)
    }


    companion object {
        @JvmStatic
        fun newInstance(): Categories {
            return Categories().apply {
                arguments = Bundle().apply {
                }
            }
        }
    }
}
