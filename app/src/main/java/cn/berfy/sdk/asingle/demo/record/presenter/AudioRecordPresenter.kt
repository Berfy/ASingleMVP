package cn.berfy.sdk.asingle.demo.record.presenter

import android.text.TextUtils
import android.view.View
import cn.berfy.sdk.asingle.demo.record.view.IAudioRecordView
import cn.berfy.sdk.mvpbase.config.CacheConstant
import cn.berfy.sdk.mvpbase.prensenter.BasePresenter
import cn.berfy.sdk.mvpbase.util.ToastUtil
import cn.berfy.sdk.mvpbase.view.RippleView
import cn.berfy.sdk.mvpbase.view.audio.AudioRecordButton
import cn.berfy.sdk.mvpbase.view.audio.VoicePlayer
import cn.berfy.sdk.mvpbase.view.audio.record.AudioManager

class AudioRecordPresenter : BasePresenter<IAudioRecordView>() {

    private var mRecordPath: String? = ""
    private var mRecordState: Int = 0//0未录音 1录音中
    private var mPlayState: Int = 0//0未播放 1播放中 2暂停
    private lateinit var mAudioManager: AudioManager

    fun init() {
        mAudioManager = AudioManager(CacheConstant.VOICE_FILE_DIR);
        mAudioManager.setOnAudioStageListener {

        }
        updateButton();
        view.getBtnTouchRecord().setAudioFinishRecorderListener(object : AudioRecordButton.AudioFinishRecorderListener {

            override fun onStart() {
                mRecordState = 1;
            }

            override fun onFinished(seconds: Float, filePath: String?) {
                mRecordPath = filePath;
                view.getTvPath().setText(filePath)
                mRecordState = 0;
            }
        })
        view.getBtnPlay().setOnClickListener(object : View.OnClickListener {

            override fun onClick(v: View?) {
                if (TextUtils.isEmpty(mRecordPath)) {
                    ToastUtil.getInstances().showShort("您还没有录音")
                    return;
                }
                when (mPlayState) {
                    0 -> {
                        VoicePlayer.instance().play(mRecordPath)
                        mPlayState = 1;
                        updateButton()
                    }
                    1 -> {
                        VoicePlayer.instance().pause()
                        mPlayState = 2;
                        updateButton()
                    }
                    2 -> {
                        mPlayState = 1;
                        VoicePlayer.instance().resume()
                        updateButton()
                    }
                }
            }
        })
        view.getBtnRecord().setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                when (mRecordState) {
                    0 -> {
                        mAudioManager.prepareAudio()
                        mRecordState = 1
                        updateButton()
                    }
                    1 -> {
                        mAudioManager.release()
                        mRecordState = 0
                        mRecordPath = mAudioManager.currentFilePath
                        updateButton()
                    }
                }
            }
        })
        VoicePlayer.instance().setComListener(
                object : VoicePlayer.OnCompleteListener {
                    override fun completed() {
                        mPlayState = 0
                        updateButton()
                    }
                })
    }

    private fun updateButton() {
        when (mRecordState) {
            0 -> {
                view.getBtnRecord().setText("开始录音")
            }
            1 -> {
                view.getBtnRecord().setText("停止")
            }
        }
        when (mPlayState) {
            0 -> {
                view.getBtnPlay().setText("播放")
            }
            1 -> {
                view.getBtnPlay().setText("暂停")
            }
            2 -> {
                view.getBtnPlay().setText("继续播放")
            }
        }
        if (!TextUtils.isEmpty(mRecordPath)) {
            view.getTvPath().setText(mRecordPath)
        }
    }


}