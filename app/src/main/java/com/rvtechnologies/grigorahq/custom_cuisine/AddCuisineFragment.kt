package com.rvtechnologies.grigorahq.custom_cuisine

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import co.csadev.kwikpicker.KwikPicker
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.rvtechnologies.grigorahq.MyApplication
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.network.ConnectionNetwork
import com.rvtechnologies.grigorahq.network.EventBroadcaster
import com.rvtechnologies.grigorahq.network.NetworkConstants.Companion.add_cuisine
import com.rvtechnologies.grigorahq.services.models.AddCusinieModel
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.add_cuisine_num
import com.rvtechnologies.grigorahq.utils.CommonUtils
import com.rvtechnologies.grigorahq.utils.PrefConstants.TOKEN
import kotlinx.android.synthetic.main.fragment_add_cuisine.*
import java.io.File


class AddCuisineFragment : Fragment(), EventBroadcaster {

    private var img_url: String = ""
    private var isClick=true


    override fun broadcast(code: Int, data: Any?) {
        if (code == add_cuisine_num) {
            var pojo = Gson().fromJson(data.toString(), AddCusinieModel::class.java)
            if (pojo.status) {
                ConnectionNetwork.showCommonDialog(
                    activity!!,
                    pojo.message,
                    getString(R.string.cuisine_added),
                    resources.getString(R.string.ok))
                ed_cuisine.isCursorVisible=false
                editText3.isCursorVisible=false
                ed_cuisine.setText("")
                editText3.setText("")

                iv_category.setImageResource(R.drawable.placeholder)
//                ConnectionNetwork.showSnack(
//                    false,
//                    activity!!,
//                    cuisine_parent,
//                    pojo.message
//                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_cuisine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iv_cuisine.setOnClickListener {
            setImg()
        }
        ed_cuisine.setOnClickListener {
            ed_cuisine.isCursorVisible= true
        }
        editText3.setOnClickListener {
            editText3.isCursorVisible= true
        }

        button2.setOnClickListener {
            if (varified() && isClick ) {
                isClick=false
                addCuisine()
            }
        }

    }

    private fun addCuisine() {
        var data = HashMap<String, Any>()
        var fileMap = HashMap<String, File>()
        var headerMap = HashMap<String, String>()
        headerMap.put("Authorization", CommonUtils.getPrefValue(activity, TOKEN))
        headerMap.put("Accept", "application/json")
        data.put("name", ed_cuisine.text.toString())
        if (img_url.isNullOrEmpty()) {
            data.put("image", File(img_url).absoluteFile)
        } else {
            fileMap.put("image", File(img_url).absoluteFile)
        }
        var list = ArrayList<String>()
        list.add("2")
        ConnectionNetwork.getData(
            add_cuisine,
            headerMap,
            ConnectionNetwork.convertRequestBodyfromMap(activity!!, data),
            ConnectionNetwork.converRequestBodyFromMapImage(fileMap, list),
            activity!!,
            cuisine_parent,
            add_cuisine_num
        )

    }

    private fun varified(): Boolean {
        if (ed_cuisine.text.isNullOrEmpty()) {
            ConnectionNetwork.showSnack(
                false,
                activity!!,
                cuisine_parent,
                getString(R.string.please_enter_cuisine_name)
            )
            return false
        } else if (img_url.isNullOrEmpty()) {
            ConnectionNetwork.showSnack(
                false,
                activity!!,
                cuisine_parent,
                getString(R.string.please_select_img)
            )
            return false
        }
        return true

    }

    private fun setImg() {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                val bottomSheetDialogFragment = KwikPicker.Builder(
                    activity!!,
                    imageProvider = { image_view, uri ->
                        Glide.with(activity!!)
                            .load(uri)
                            .into(image_view)
                    },
                    onImageSelectedListener = { uri: Uri ->
                        iv_cuisine.post {

                            Glide.with(activity!!)
                                .load(uri)
                                .into(iv_category)
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
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .check()
    }


    companion object {
        fun newInstance(): AddCuisineFragment {
            return AddCuisineFragment().apply {
                arguments = Bundle().apply {

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
