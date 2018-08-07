package cn.berfy.sdk.http.callback;

import cn.berfy.sdk.http.model.NetError;
import cn.berfy.sdk.http.model.NetResponse;

/**
 * Created by Berfy on 2017/12/15.
 * http接口回调
 */

public abstract interface RequestCallBack<T> {

    void onStart();

    void onFinish(NetResponse<T> response);

    void onError(NetError error);
}
