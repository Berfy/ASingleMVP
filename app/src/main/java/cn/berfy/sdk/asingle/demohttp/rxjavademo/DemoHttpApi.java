package cn.berfy.sdk.asingle.demohttp.rxjavademo;

import cn.berfy.sdk.http.HttpApi;

/**
 * Created by win764-1 on 2016/12/12.
 */
public class DemoHttpApi {
    private static DemoHttpApi mDemoHttpApi;
    private RetrofitService mRetrofitService;

    synchronized public static DemoHttpApi getInstance() {
        if (null == mDemoHttpApi) {
            synchronized (DemoHttpApi.class) {
                if (null == mDemoHttpApi) {
                    mDemoHttpApi = new DemoHttpApi();
                }
            }
        }
        return mDemoHttpApi;
    }

    public DemoHttpApi() {
        mRetrofitService = HttpApi.getInstances().getServer(RetrofitService.class);
    }

    public RetrofitService getServer() {
        return mRetrofitService;
    }
}
