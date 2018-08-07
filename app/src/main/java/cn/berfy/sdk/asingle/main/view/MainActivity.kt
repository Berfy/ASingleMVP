package cn.berfy.sdk.asingle.main.view

import android.os.Bundle
import cn.berfy.sdk.asingle.R
import cn.berfy.sdk.asingle.main.presenter.MainPresenter
import cn.berfy.sdk.mvpbase.base.CommonActivity
import cn.berfy.sdk.mvpbase.util.ToastUtil
import cn.berfy.sdk.mvpbase.view.audio.VoicePlayer
import cn.berfy.sdk.mvpbase.view.viewpager.SpringIndicator
import cn.berfy.sdk.mvpbase.view.viewpager.viewpager.ScrollerViewPager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : CommonActivity<IMainView, MainPresenter>(), IMainView {

    override fun getContentViewId(): Int {
        return R.layout.activity_main;
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initView() {
        showTitleBar()
        titleBar.setLeftIcon(true, R.mipmap.ic_launcher, { ToastUtil.getInstances().showShort("我是头像 我不跳转") })
        titleBar.setTitle(getString(R.string.app_name))
        mPresenter.init(supportFragmentManager);
    }

    override fun initPresenter(): MainPresenter {
        return MainPresenter()
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        VoicePlayer.instance().stop()
    }

    override fun getViewPager(): ScrollerViewPager {
        return viewPager
    }

    override fun getSpringIndicator(): SpringIndicator {
        return indicator
    }

    override fun hiddenLoadingView(msg: String?) {
    }

    override fun showLoadingView(msg: String?) {
    }
}
