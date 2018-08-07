package cn.berfy.sdk.asingle.demo.viewpager.view

import android.os.Bundle
import cn.berfy.sdk.asingle.R
import cn.berfy.sdk.mvpbase.base.CommonActivity
import cn.berfy.sdk.mvpbase.iview.IBaseView
import cn.berfy.sdk.mvpbase.prensenter.BasePresenter
import cn.berfy.sdk.mvpbase.view.RippleView


class StatusBarDemoActivity : CommonActivity<IBaseView, BasePresenter<IBaseView>>(), RippleView.OnRippleStateListener {

    override fun getContentViewId(): Int {
        return R.layout.activity_demo_statusbar
    }

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun initView() {
        darkStatusBar(true)
    }

    override fun initPresenter(): BasePresenter<IBaseView>? {
        return null
    }

    override fun startRipple(view: RippleView?) {
    }

    override fun finishRipple(view: RippleView?) {
    }

    override fun cancel(view: RippleView?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}