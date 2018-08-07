package cn.berfy.sdk.asingle.demo.viewpager.view

import android.os.Bundle
import cn.berfy.sdk.asingle.R
import cn.berfy.sdk.mvpbase.base.CommonFragment
import cn.berfy.sdk.mvpbase.iview.IBaseView
import cn.berfy.sdk.mvpbase.prensenter.BasePresenter

class VPDemoFragment : CommonFragment<IBaseView, BasePresenter<IBaseView>>() {

    override fun getContentViewId(): Int {
        return R.layout.fragment_demo_viewpager
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initView() {
    }

    override fun initPresenter(): BasePresenter<IBaseView>? {
        return null;
    }
}