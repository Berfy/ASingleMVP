package cn.berfy.sdk.asingle.demo.permission.view

import android.os.Bundle
import android.widget.TextView
import cn.berfy.sdk.asingle.R
import cn.berfy.sdk.asingle.demo.permission.presenter.PermissionPresenter
import cn.berfy.sdk.mvpbase.base.CommonActivity
import cn.berfy.sdk.mvpbase.view.RippleView
import cn.berfy.sdk.mvpsdk.demo.permission.view.IPermissionView
import kotlinx.android.synthetic.main.activity_get_permission.*

class PermissionActivity : CommonActivity<IPermissionView, PermissionPresenter>(), IPermissionView {

    override fun getContentViewId(): Int {
        return R.layout.activity_get_permission
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initView() {
        showTitleBar()
        titleBar.setTitle("权限获取")
        presenter.init()
    }

    override fun initPresenter(): PermissionPresenter {
        return PermissionPresenter()
    }

    override fun hiddenLoadingView(msg: String?) {

    }

    override fun showLoadingView(msg: String?) {

    }

    override fun getBtnPermissionAll(): RippleView {
        return btn_all_permissions
    }

    override fun getTvPermissionCamera(): TextView {
        return tv_permission_camera
    }

    override fun getTvPermissionSD(): TextView {
        return tv_permission_sd
    }

    override fun getTvPermissionGps(): TextView {
        return tv_permission_gps
    }

    override fun getTvPermissionCall(): TextView {
        return tv_permission_call
    }
}