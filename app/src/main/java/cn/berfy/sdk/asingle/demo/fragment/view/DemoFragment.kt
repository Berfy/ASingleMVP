package cn.berfy.sdk.asingle.demo.fragment.view

import android.os.Bundle
import android.widget.TextView
import cn.berfy.sdk.asingle.R
import cn.berfy.sdk.asingle.demo.fragment.presenter.DemoFragPresenter
import cn.berfy.sdk.mvpbase.base.CommonFragment
import kotlinx.android.synthetic.main.fragment_demo.*

class DemoFragment : CommonFragment<IDemoFragView, DemoFragPresenter>(), IDemoFragView {

    override fun getContentViewId(): Int {
        return R.layout.fragment_demo
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initView() {
        presenter.init()
    }

    override fun initPresenter(): DemoFragPresenter? {
        return DemoFragPresenter()
    }

    override fun showLoadingView(msg: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hiddenLoadingView(msg: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeFrag() {
        removeParentFragment(this)
    }

    override fun getBtnRemove(): TextView {
        return tv_remove
    }
}