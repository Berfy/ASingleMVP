package cn.berfy.sdk.asingle.demo.record.view

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import cn.berfy.sdk.asingle.R
import cn.berfy.sdk.mvpbase.base.CommonActivity
import cn.berfy.sdk.mvpbase.view.audio.AudioRecordButton
import cn.berfy.sdk.asingle.demo.record.presenter.AudioRecordPresenter
import kotlinx.android.synthetic.main.activity_demo_audio_record.*

class AudioRecordActivity : CommonActivity<IAudioRecordView, AudioRecordPresenter>(), IAudioRecordView {

    override fun getContentViewId(): Int {
        return R.layout.activity_demo_audio_record
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initView() {
        showTitleBar()
        titleBar.setTitle("录音")
        presenter.init()
    }

    override fun initPresenter(): AudioRecordPresenter {
        return AudioRecordPresenter()
    }

    override fun getTvPath(): TextView {
        return tv_record_path
    }

    override fun getBtnPlay(): Button {
        return btn_record_play
    }

    override fun getBtnRecord(): Button {
        return btn_record_record
    }

    override fun getBtnTouchRecord(): AudioRecordButton {
        return btn_record_touchrecord
    }

    override fun hiddenLoadingView(msg: String?) {
    }

    override fun showLoadingView(msg: String?) {
    }
}