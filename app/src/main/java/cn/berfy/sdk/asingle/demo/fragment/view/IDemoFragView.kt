package cn.berfy.sdk.asingle.demo.fragment.view

import android.support.v4.app.Fragment
import android.widget.TextView
import cn.berfy.sdk.mvpbase.iview.IBaseView

interface IDemoFragView : IBaseView {

    fun getBtnRemove(): TextView
    fun removeFrag();
}