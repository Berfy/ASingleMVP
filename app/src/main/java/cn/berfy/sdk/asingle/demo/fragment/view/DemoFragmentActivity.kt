package cn.berfy.sdk.asingle.demo.fragment.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.TextView
import cn.berfy.sdk.asingle.R
import cn.berfy.sdk.asingle.demo.fragment.presenter.DemoFragmentPresenter
import cn.berfy.sdk.mvpbase.base.CommonActivity
import kotlinx.android.synthetic.main.activity_fragment_demo.*

class DemoFragmentActivity : CommonActivity<IDemoFragmentView, DemoFragmentPresenter>(), IDemoFragmentView {

    override fun getContentViewId(): Int {
        return R.layout.activity_fragment_demo
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initView() {
        showTitleBar()
        titleBar.setTitle("Fragment")
        presenter.init()
    }

    override fun initPresenter(): DemoFragmentPresenter {
        return DemoFragmentPresenter();
    }

    override fun hiddenLoadingView(msg: String?) {

    }

    override fun showLoadingView(msg: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addNewFragment(fragmentLayoutId: Int, fragment: Fragment?) {
        addFragment(fragmentLayoutId, fragment)
    }

    override fun getBtnAdd1(): TextView {
        return tv_add1
    }

    override fun getBtnAdd2(): TextView {
        return tv_add2
    }
}
