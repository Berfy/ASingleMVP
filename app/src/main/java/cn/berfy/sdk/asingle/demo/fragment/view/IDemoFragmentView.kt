package cn.berfy.sdk.asingle.demo.fragment.view

import android.support.v4.app.Fragment
import android.widget.TextView
import cn.berfy.sdk.mvpbase.iview.IBaseView

interface IDemoFragmentView : IBaseView {

    fun getBtnAdd1(): TextView
    fun getBtnAdd2(): TextView
    fun addNewFragment(fragmentLayoutId: Int, fragment: Fragment?);
}