package cn.berfy.sdk.asingle.demo.record.view

import android.widget.Button
import android.widget.TextView
import cn.berfy.sdk.mvpbase.iview.IBaseView
import cn.berfy.sdk.mvpbase.view.RippleView
import cn.berfy.sdk.mvpbase.view.audio.AudioRecordButton

interface IAudioRecordView : IBaseView {
    fun getTvPath(): TextView
    fun getBtnPlay(): Button
    fun getBtnRecord(): Button
    fun getBtnTouchRecord(): AudioRecordButton

}