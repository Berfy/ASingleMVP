package cn.berfy.sdk.asingle.demo.notify.presenter

import android.text.Editable
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.EditText
import cn.berfy.sdk.asingle.demo.notify.view.INotifyView
import cn.berfy.sdk.mvpbase.prensenter.BasePresenter
import cn.berfy.sdk.mvpbase.util.NotifycationUtil
import cn.berfy.sdk.mvpbase.util.ToastUtil
import cn.berfy.sdk.mvpbase.view.RippleView

class NotifyPresenter : BasePresenter<INotifyView>(), RippleView.OnRippleStateListener, AdapterView.OnItemSelectedListener {

    fun init() {
        view.getBtnSend().setOnRippleStateListener(this)
        view.getBtnClear().setOnRippleStateListener(this)
        view.getBtnClearTag().setOnRippleStateListener(this)
        mView.getSprTag().setOnItemSelectedListener(this)
    }

    override fun startRipple(view: RippleView?) {
    }

    override fun finishRipple(view: RippleView?) {
        when (view) {
            mView.getBtnSend() -> {
                var title = mView.getEditTitle().text.toString().trim()
                var content = mView.getEditContent().text.toString().trim()
                if (TextUtils.isEmpty(title)) {
                    ToastUtil.getInstances().showShort("请填写标题")
                    return
                }
                if (TextUtils.isEmpty(content)) {
                    ToastUtil.getInstances().showShort("请填写内容")
                    return
                }
                NotifycationUtil.getInstance().title = title
                NotifycationUtil.getInstance().content = content
                NotifycationUtil.getInstance().notify("tag")
            }
            mView.getBtnClearTag() -> {
                var tagOrId = mView.getEditTag().text.toString().trim()
                if (TextUtils.isEmpty(tagOrId)) {
                    ToastUtil.getInstances().showShort("请填写id或者tag")
                    return
                }
                var type = mView.getSprTag().selectedItem.toString()
                if (type.contains("Id")) {
                    NotifycationUtil.getInstance().clearNotify(tagOrId.toInt())
                } else {
                    NotifycationUtil.getInstance().clearNotify(tagOrId)
                }
            }
            mView.getBtnClear() -> {
                NotifycationUtil.getInstance().clearAll()
            }
        }
    }

    override fun cancel(view: RippleView?) {
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (position == 0) {
            mView.getEditTag().inputType = EditorInfo.TYPE_CLASS_NUMBER;
        } else {
            mView.getEditTag().inputType = EditorInfo.TYPE_CLASS_TEXT;
        }
    }
}