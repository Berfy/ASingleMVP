package cn.berfy.sdk.asingle.demo.viewpager.view

import android.os.Bundle
import cn.berfy.sdk.asingle.R
import cn.berfy.sdk.asingle.demo.viewpager.presenter.ViewPagerDemoPresenter
import cn.berfy.sdk.mvpbase.base.CommonActivity
import cn.berfy.sdk.mvpbase.view.viewpager.SpringIndicator
import cn.berfy.sdk.mvpbase.view.viewpager.viewpager.ScrollerViewPager
import kotlinx.android.synthetic.main.activity_demo_viewpager.*

class ViewPagerDemoActivity : CommonActivity<IViewPagerDemoView, ViewPagerDemoPresenter>(), IViewPagerDemoView {

    override fun getContentViewId(): Int {
        return R.layout.activity_demo_viewpager
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initView() {
        mPresenter.init(supportFragmentManager);
    }

    override fun initPresenter(): ViewPagerDemoPresenter {
        return ViewPagerDemoPresenter();
    }

    override fun showLoadingView(msg: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hiddenLoadingView(msg: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getViewPager(): ScrollerViewPager {
        return viewPager
    }

    override fun getSpringIndicator(): SpringIndicator {
        return indicator
    }
}