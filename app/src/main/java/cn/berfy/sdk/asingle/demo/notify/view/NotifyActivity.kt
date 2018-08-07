package cn.berfy.sdk.asingle.demo.notify.view

import android.os.Bundle
import android.widget.EditText
import android.widget.Spinner
import cn.berfy.sdk.asingle.R
import cn.berfy.sdk.asingle.demo.notify.presenter.NotifyPresenter
import cn.berfy.sdk.mvpbase.base.CommonActivity
import cn.berfy.sdk.mvpbase.view.RippleView
import kotlinx.android.synthetic.main.activity_demo_notify.*

class NotifyActivity : CommonActivity<INotifyView, NotifyPresenter>(), INotifyView {

    override fun getContentViewId(): Int {
        return R.layout.activity_demo_notify
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initView() {
        showTitleBar()
        titleBar.setTitle("通知管理")
        mPresenter.init()
    }

    override fun initPresenter(): NotifyPresenter {
        return NotifyPresenter();
    }

    override fun showLoadingView(msg: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hiddenLoadingView(msg: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getEditTitle(): EditText {
        return edit_notify_title
    }

    override fun getEditContent(): EditText {
        return edit_notify_content
    }

    override fun getBtnSend(): RippleView {
        return btn_send
    }

    override fun getSprTag(): Spinner {
        return spr_tag
    }

    override fun getEditTag(): EditText {
        return edit_notify_id_or_tag
    }

    override fun getBtnClearTag(): RippleView {
        return btn_clear_tag
    }

    override fun getBtnClear(): RippleView {
        return btn_clear
    }
}