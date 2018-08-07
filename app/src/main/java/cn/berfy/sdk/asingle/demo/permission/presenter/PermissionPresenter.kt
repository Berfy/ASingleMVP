package cn.berfy.sdk.asingle.demo.permission.presenter

import android.app.Activity
import android.view.View
import cn.berfy.sdk.asingle.R
import cn.berfy.sdk.mvpbase.prensenter.BasePresenter
import cn.berfy.sdk.mvpbase.util.CommonUtil
import cn.berfy.sdk.mvpbase.util.GsonUtil
import cn.berfy.sdk.mvpbase.util.ToastUtil
import cn.berfy.sdk.mvpbase.view.RippleView
import cn.berfy.sdk.mvpsdk.demo.permission.view.IPermissionView
import com.hjq.permissions.OnPermission
import com.hjq.permissions.XXPermissions

class PermissionPresenter : BasePresenter<IPermissionView>(), RippleView.OnRippleStateListener, View.OnClickListener {

    fun init() {
        view.getBtnPermissionAll().setOnRippleStateListener(this)
        view.getTvPermissionSD().setOnClickListener(this)
        view.getTvPermissionCamera().setOnClickListener(this)
        view.getTvPermissionGps().setOnClickListener(this)
        view.getTvPermissionCall().setOnClickListener(this)
        updateStatus()
    }

    fun updateStatus() {
        view.getTvPermissionCamera().setTextColor(
                if (XXPermissions.isHasPermission(mContext, android.Manifest.permission.CAMERA)) {
                    CommonUtil.getColor(mContext, R.color.white_normal)
                } else {
                    CommonUtil.getColor(mContext, R.color.color_black)
                })
        view.getTvPermissionSD().setTextColor(
                if (XXPermissions.isHasPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    CommonUtil.getColor(mContext, R.color.white_normal)
                } else {
                    CommonUtil.getColor(mContext, R.color.color_black)
                })
        view.getTvPermissionGps().setTextColor(
                if (XXPermissions.isHasPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                    CommonUtil.getColor(mContext, R.color.white_normal)
                } else {
                    CommonUtil.getColor(mContext, R.color.color_black)
                })
        view.getTvPermissionCall().setTextColor(
                if (XXPermissions.isHasPermission(mContext, android.Manifest.permission.CALL_PHONE)) {
                    CommonUtil.getColor(mContext, R.color.white_normal)
                } else {
                    CommonUtil.getColor(mContext, R.color.color_black)
                })
    }

    override fun startRipple(view: RippleView?) {
    }

    override fun finishRipple(view: RippleView?) {
        when (view) {
            getView().getBtnPermissionAll() -> {
                var need = arrayOf(android.Manifest.permission.CAMERA,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.CALL_PHONE);
                XXPermissions.with(mContext as Activity)
                        .permission(need)
                        .request(object : OnPermission {
                            override fun noPermission(denied: MutableList<String>?, quick: Boolean) {
                                updateStatus();
                                ToastUtil.getInstances().showShort("你拒绝了${GsonUtil.getInstance().toJson(denied)}权限")
                                XXPermissions.gotoPermissionSettings(mContext)
                            }

                            override fun hasPermission(granted: MutableList<String>?, isAll: Boolean) {
                                updateStatus();
                                var denieds = arrayListOf<String>();
                                if (null != granted)
                                    for (grant in need) {
                                        if (!granted.contains(grant)) {
                                            denieds.add(grant);
                                        }
                                    }
                                if (isAll) {
                                    ToastUtil.getInstances().showShort("你同意了所有权限")
                                } else {
                                    ToastUtil.getInstances().showShort("你同意了" + GsonUtil.getInstance().toJson(granted) + "权限\n"
                                            + "你拒绝了" + GsonUtil.getInstance().toJson(denieds) + "权限")
                                    XXPermissions.gotoPermissionSettings(mContext)
                                }
                            }
                        })
            }
        }
    }

    override fun cancel(view: RippleView?) {
    }

    override fun onClick(v: View?) {
        when (v) {
            view.getTvPermissionCamera() -> {
                XXPermissions.with(mContext as Activity)
                        .permission(android.Manifest.permission.CAMERA)
                        .request(object : OnPermission {
                            override fun noPermission(denied: MutableList<String>?, quick: Boolean) {
                                updateStatus();
                                ToastUtil.getInstances().showShort("你拒绝了${android.Manifest.permission.CAMERA}")
                                XXPermissions.gotoPermissionSettings(mContext)
                            }

                            override fun hasPermission(granted: MutableList<String>?, isAll: Boolean) {
                                updateStatus();
                                ToastUtil.getInstances().showShort("你同意了${android.Manifest.permission.CAMERA}")
                            }
                        })
            }
            view.getTvPermissionSD() -> {
                XXPermissions.with(mContext as Activity)
                        .permission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .request(object : OnPermission {
                            override fun noPermission(denied: MutableList<String>?, quick: Boolean) {
                                updateStatus();
                                ToastUtil.getInstances().showShort("你拒绝了${android.Manifest.permission.WRITE_EXTERNAL_STORAGE}")
                                XXPermissions.gotoPermissionSettings(mContext)
                            }

                            override fun hasPermission(granted: MutableList<String>?, isAll: Boolean) {
                                updateStatus();
                                ToastUtil.getInstances().showShort("你同意了${android.Manifest.permission.WRITE_EXTERNAL_STORAGE}")
                            }
                        })
            }
            view.getTvPermissionGps() -> {
                XXPermissions.with(mContext as Activity)
                        .permission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                        .request(object : OnPermission {
                            override fun noPermission(denied: MutableList<String>?, quick: Boolean) {
                                updateStatus();
                                ToastUtil.getInstances().showShort("你拒绝了${android.Manifest.permission.ACCESS_FINE_LOCATION}")
                                XXPermissions.gotoPermissionSettings(mContext)
                            }

                            override fun hasPermission(granted: MutableList<String>?, isAll: Boolean) {
                                updateStatus();
                                ToastUtil.getInstances().showShort("你同意了${android.Manifest.permission.ACCESS_FINE_LOCATION}")
                            }
                        })
            }
            view.getTvPermissionCall() -> {
                XXPermissions.with(mContext as Activity)
                        .permission(android.Manifest.permission.CALL_PHONE)
                        .request(object : OnPermission {
                            override fun noPermission(denied: MutableList<String>?, quick: Boolean) {
                                updateStatus();
                                ToastUtil.getInstances().showShort("你拒绝了${android.Manifest.permission.CALL_PHONE}")
                                XXPermissions.gotoPermissionSettings(mContext)
                            }

                            override fun hasPermission(granted: MutableList<String>?, isAll: Boolean) {
                                updateStatus();
                                ToastUtil.getInstances().showShort("你同意了${android.Manifest.permission.CALL_PHONE}")
                            }
                        })
            }
        }
    }
}