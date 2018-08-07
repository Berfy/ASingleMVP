package cn.berfy.sdk.asingle.demo.dialog.view

import android.widget.EditText
import cn.berfy.sdk.mvpbase.iview.IBaseView
import cn.berfy.sdk.mvpbase.view.RippleView

interface IDialogView : IBaseView {

    fun getEditTitle(): EditText
    fun getEditContent(): EditText
    fun getBtnTip(): RippleView
    fun getBtnSure(): RippleView
    fun getBtnCustom(): RippleView
}