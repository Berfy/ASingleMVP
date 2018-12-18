package cn.berfy.sdk.asingle.main.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import cn.berfy.sdk.asingle.R
import cn.berfy.sdk.asingle.demo.dialog.view.DialogDemoActivity
import cn.berfy.sdk.asingle.demo.fragment.view.DemoFragmentActivity
import cn.berfy.sdk.asingle.demo.notify.view.NotifyActivity
import cn.berfy.sdk.asingle.demo.permission.view.PermissionActivity
import cn.berfy.sdk.asingle.demo.record.view.AudioRecordActivity
import cn.berfy.sdk.asingle.demo.viewpager.view.StatusBarDemoActivity
import cn.berfy.sdk.asingle.demo.viewpager.view.ViewPagerDemoActivity
import cn.berfy.sdk.asingle.main.presenter.MainMVPDemoPresenter
import cn.berfy.sdk.mvpbase.base.CommonActivity
import cn.berfy.sdk.mvpbase.base.CommonFragment
import cn.berfy.sdk.mvpbase.config.CacheConstant
import cn.berfy.sdk.mvpbase.util.CommonUtil
import cn.berfy.sdk.mvpbase.util.FileUtils
import cn.berfy.sdk.mvpbase.util.map.MapActivity
import cn.berfy.sdk.mvpbase.view.RippleView
import kotlinx.android.synthetic.main.fragment_main_mvp.*

class MainMVPDemoFragment : CommonFragment<IMainMVPDemoView, MainMVPDemoPresenter>(), RippleView.OnRippleStateListener, View.OnClickListener {

    private var mIsShowTitleBar = true

    override fun getContentViewId(): Int {
        return R.layout.fragment_main_mvp
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initView() {
        btn_titlebar_show.setOnRippleStateListener(this)
        btn_viewpager.setOnClickListener(this)
        btn_statusbar.setOnClickListener(this)
        btn_notify.setOnClickListener(this)
        btn_fragment.setOnClickListener(this)
        btn_permission.setOnClickListener(this)
        btn_dialog.setOnClickListener(this)
        btn_audio_record.setOnClickListener(this)
        btn_map_haiwai.setOnClickListener(this)
        checkPermission(CommonActivity.CheckPermListener {
            FileUtils.createDir(CacheConstant.VOICE_FILE_DIR)
        }, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
    }

    override fun initPresenter(): MainMVPDemoPresenter? {
        return null;
    }

    override fun startRipple(view: RippleView?) {
        when (view) {
            btn_titlebar_show -> {
                mIsShowTitleBar = !mIsShowTitleBar;
            }
            btn_viewpager -> {

            }
        }
    }

    override fun finishRipple(view: RippleView?) {
        when (view) {
            btn_titlebar_show -> {
                if (mIsShowTitleBar) {
                    titleBar.visibility = View.VISIBLE
                    btn_titlebar_show.text = "隐藏标题栏"
                    btn_titlebar_show.setBackgroundResource(R.drawable.button_normal)
                    btn_titlebar_show.setRippleColor(CommonUtil.getColor(mContext, R.color.colorAccent), 1f)
                } else {
                    titleBar.visibility = View.GONE
                    btn_titlebar_show.text = "显示标题栏"
                    btn_titlebar_show.setBackgroundResource(R.drawable.button_press)
                    btn_titlebar_show.setRippleColor(CommonUtil.getColor(mContext, R.color.color_titlebar_light_theme), 1f)
                }
            }
        }
    }

    override fun cancel(view: RippleView?) {
    }

    override fun onClick(v: View?) {
        when (v) {
            btn_viewpager -> {
                startActivity(Intent(mContext, ViewPagerDemoActivity::class.java))
            }
            btn_statusbar -> {
                startActivity(Intent(mContext, StatusBarDemoActivity::class.java))
            }
            btn_notify -> {
                startActivity(Intent(mContext, NotifyActivity::class.java))
            }
            btn_fragment -> {
                startActivity(Intent(mContext, DemoFragmentActivity::class.java))
            }
            btn_permission -> {
                startActivity(Intent(mContext, PermissionActivity::class.java))
            }
            btn_dialog -> {
                startActivity(Intent(mContext, DialogDemoActivity::class.java))
            }
            btn_audio_record -> {
                startActivity(Intent(mContext, AudioRecordActivity::class.java))
            }
            btn_map_haiwai -> {
                MapActivity.intoThisActivity(mContext,"pk.eyJ1IjoiYmVyZnkiLCJhIjoiY2pwbWNjaGQ3MG5jbzQ5cGZwaTBiZ2dvcSJ9.pbs0OWZNh1aTvspFoaHmRg");
            }
        }
    }
}