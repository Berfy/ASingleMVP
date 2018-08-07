package cn.berfy.sdk.asingle.demo.notify.view

import android.widget.EditText
import android.widget.Spinner
import cn.berfy.sdk.mvpbase.iview.IBaseView
import cn.berfy.sdk.mvpbase.view.RippleView

interface INotifyView : IBaseView {

    fun getEditTitle(): EditText

    fun getEditContent(): EditText

    fun getBtnSend(): RippleView

    fun getSprTag(): Spinner

    fun getEditTag(): EditText

    fun getBtnClearTag(): RippleView

    fun getBtnClear(): RippleView
}