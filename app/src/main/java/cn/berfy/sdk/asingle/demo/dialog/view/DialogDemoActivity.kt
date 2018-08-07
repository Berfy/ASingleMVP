package cn.berfy.sdk.asingle.demo.dialog.view

import android.os.Bundle
import android.widget.EditText
import cn.berfy.sdk.asingle.R
import cn.berfy.sdk.asingle.demo.dialog.presenter.DialogPresenter
import cn.berfy.sdk.mvpbase.base.CommonActivity
import cn.berfy.sdk.mvpbase.view.RippleView
import kotlinx.android.synthetic.main.activity_demo_dialog.*

class DialogDemoActivity : CommonActivity<IDialogView, DialogPresenter>(), IDialogView {

    override fun getContentViewId(): Int {
        return R.layout.activity_demo_dialog
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initView() {
        showTitleBar()
        titleBar.setTitle("Dialog")
        presenter.init()
    }

    override fun initPresenter(): DialogPresenter {
        return DialogPresenter()
    }

    override fun getEditTitle(): EditText {
        return edit_title
    }

    override fun getEditContent(): EditText {
        return edit_content
    }

    override fun getBtnTip(): RippleView {
        return btn_tip_dialog
    }

    override fun getBtnSure(): RippleView {
        return btn_sure_dialog
    }

    override fun getBtnCustom(): RippleView {
        return btn_custom_dialog
    }

    override fun hiddenLoadingView(msg: String?) {
    }

    override fun showLoadingView(msg: String?) {
    }
}