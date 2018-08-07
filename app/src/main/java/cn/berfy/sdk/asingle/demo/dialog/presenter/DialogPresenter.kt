package cn.berfy.sdk.asingle.demo.dialog.presenter

import android.app.Activity
import android.content.DialogInterface
import android.text.TextUtils
import android.widget.TextView
import android.widget.Toast
import cn.berfy.sdk.asingle.R
import cn.berfy.sdk.asingle.demo.dialog.view.IDialogView
import cn.berfy.sdk.mvpbase.prensenter.BasePresenter
import cn.berfy.sdk.mvpbase.util.ToastUtil
import cn.berfy.sdk.mvpbase.view.RippleView
import cn.berfy.sdk.mvpbase.view.dialog.CommonDialog
import org.w3c.dom.Text

class DialogPresenter : BasePresenter<IDialogView>(), RippleView.OnRippleStateListener {

    private lateinit var mCommonDialog: CommonDialog;

    fun init() {
        mCommonDialog = CommonDialog(mContext as Activity);
        view.getBtnTip().setOnRippleStateListener(this)
        view.getBtnSure().setOnRippleStateListener(this)
        view.getBtnCustom().setOnRippleStateListener(this)
    }

    override fun startRipple(view: RippleView?) {
    }

    override fun finishRipple(view: RippleView?) {
        when (view) {
            mView.getBtnTip() -> {
                var title = mView.getEditTitle().text.toString().trim();
                var content = mView.getEditContent().text.toString().trim();
                if (TextUtils.isEmpty(title)) {
                    ToastUtil.getInstances().showShort("标题为空")
                    return
                }
                if (TextUtils.isEmpty(content)) {
                    ToastUtil.getInstances().showShort("内容为空")
                    return
                }
                mCommonDialog.setCancelable(true)
                        .showTipDialog(title, content, "确定", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                mCommonDialog.dismiss()
                            }
                        })
            }
            mView.getBtnSure() -> {
                var title = mView.getEditTitle().text.toString().trim();
                var content = mView.getEditContent().text.toString().trim();
                if (TextUtils.isEmpty(title)) {
                    ToastUtil.getInstances().showShort("标题为空")
                    return
                }
                if (TextUtils.isEmpty(content)) {
                    ToastUtil.getInstances().showShort("内容为空")
                    return
                }
                mCommonDialog.setCancelable(true)
                        .showDialog(title, content, "确定", "取消", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                mCommonDialog.dismiss()
                            }
                        }, object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                mCommonDialog.dismiss()
                            }
                        })
            }
            mView.getBtnCustom() -> {
                var title = mView.getEditTitle().text.toString().trim();
                var content = mView.getEditContent().text.toString().trim();
                if (TextUtils.isEmpty(title)) {
                    ToastUtil.getInstances().showShort("标题为空")
                    return
                }
                if (TextUtils.isEmpty(content)) {
                    ToastUtil.getInstances().showShort("内容为空")
                    return
                }
                mCommonDialog.setContentView(R.layout.dialog_demo)
                        .setCancelable(true)
                        .setCanceledOnTouchOutside(true)
                        .show();

                var tv_title:TextView? = mCommonDialog.dialog.findViewById<TextView>(R.id.tv_title)
                tv_title?.text = title

                var tv_content:TextView? = mCommonDialog.dialog.findViewById<TextView>(R.id.tv_content)
                tv_content?.text = content

                var btn_left:RippleView? = mCommonDialog.dialog.findViewById<RippleView>(R.id.btn_left)
                btn_left?.setOnRippleStateListener(this)

                var btn_right:RippleView? = mCommonDialog.dialog.findViewById<RippleView>(R.id.btn_right)
                btn_right?.setOnRippleStateListener(this)
            }
        }
        when (view?.id) {
            R.id.btn_left -> {
                mCommonDialog.dismiss()
            }
            R.id.btn_right -> {
                mCommonDialog.dismiss()
            }
        }
    }

    override fun cancel(view: RippleView?) {
    }
}