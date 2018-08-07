package cn.berfy.sdk.asingle.demohttp.rxjavademo.presenter;

import android.content.Context;

import cn.berfy.sdk.asingle.demohttp.model.Data;
import cn.berfy.sdk.asingle.demohttp.rxjavademo.DemoHttpApi;
import cn.berfy.sdk.asingle.demohttp.rxjavademo.model.Book;
import cn.berfy.sdk.asingle.demohttp.rxjavademo.view.IDemoHttpView;
import cn.berfy.sdk.mvpbase.prensenter.BasePresenter;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class DemoHttpPresenter extends BasePresenter<IDemoHttpView> {

    private CompositeSubscription mCompositeSubscription;

    public void init() {

    }

    @Override
    public void attach(Context context, IDemoHttpView view) {
        super.attach(context, view);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void detach() {
        super.detach();
        if (mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    public void checkUpdate(String version, Observer<Data<Book>> observer) {
        mCompositeSubscription.add(DemoHttpApi.getInstance().getServer().checkUpdate(version)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        );
    }

    public void getSearchBooks(String name, String tag, int start, int count, Observer<Book> observer) {
        mCompositeSubscription.add(DemoHttpApi.getInstance().getServer().getSearchBooksPost(name, tag, start, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        );
    }
}
