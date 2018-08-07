package cn.berfy.sdk.mvpsdk.demo.permission.view

import android.widget.TextView
import cn.berfy.sdk.mvpbase.iview.IBaseView
import cn.berfy.sdk.mvpbase.view.RippleView

interface IPermissionView : IBaseView {

    fun getBtnPermissionAll(): RippleView

    fun getTvPermissionCamera(): TextView

    fun getTvPermissionSD(): TextView

    fun getTvPermissionGps(): TextView

    fun getTvPermissionCall(): TextView

}