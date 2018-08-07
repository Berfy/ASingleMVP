package cn.berfy.sdk.asingle.main.presenter

import android.content.Context
import cn.berfy.sdk.asingle.demohttp.model.Data
import cn.berfy.sdk.asingle.demohttp.rxjavademo.DemoHttpApi
import cn.berfy.sdk.asingle.demohttp.rxjavademo.model.Book
import cn.berfy.sdk.asingle.demohttp.rxjavademo.view.IDemoHttpView
import cn.berfy.sdk.asingle.main.view.IMainHTTPDemoView
import cn.berfy.sdk.mvpbase.prensenter.BasePresenter
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

class MainHTTPDemoPresenter : BasePresenter<IMainHTTPDemoView>() {


    private lateinit var mCompositeSubscription: CompositeSubscription

    fun init() {

    }

    override fun attach(context: Context?, view: IMainHTTPDemoView?) {
        super.attach(context, view)
        mCompositeSubscription = CompositeSubscription()
    }

    override fun detach() {
        super.detach()
        if (mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe()
        }
    }

    fun checkUpdate(version: String, observer: Observer<Data<Book>>) {
        mCompositeSubscription.add(DemoHttpApi.getInstance().getServer().checkUpdate(version)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        )
    }

    fun getSearchBooks(name: String, tag: String, start: Int, count: Int, observer: Observer<Book>) {
        mCompositeSubscription.add(DemoHttpApi.getInstance().getServer().getSearchBooksPost(name, tag, start, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        )
    }
}