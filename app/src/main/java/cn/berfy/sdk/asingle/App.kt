package cn.berfy.sdk.asingle

import cn.berfy.sdk.asingle.main.view.MainActivity
import cn.berfy.sdk.http.HttpApi
import cn.berfy.sdk.mvpbase.base.BaseApplication
import cn.berfy.sdk.mvpbase.config.CacheConstant
import cn.berfy.sdk.mvpbase.manager.NotifycationManager
import cn.berfy.sdk.mvpbase.util.ToastUtil

class App : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        ToastUtil.init(BaseApplication.getContext())
        CacheConstant.setRootDir("MvpSDK")
        HttpApi.init(applicationContext)
//        CrashException.getInstance().init(BaseApplication.getContext(), MainActivity::class.java)
        NotifycationManager.init(BaseApplication.getContext(), MainActivity::class.java)
    }
}
