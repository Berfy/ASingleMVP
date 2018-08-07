package cn.berfy.sdk.asingle.demo.fragment.presenter

import android.view.View
import cn.berfy.sdk.asingle.demo.fragment.view.IDemoFragView
import cn.berfy.sdk.mvpbase.prensenter.BasePresenter

class DemoFragPresenter : BasePresenter<IDemoFragView>(), View.OnClickListener {

    fun init() {
        view.getBtnRemove().setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            view.getBtnRemove() -> {
                view.removeFrag()
            }
        }
    }
}