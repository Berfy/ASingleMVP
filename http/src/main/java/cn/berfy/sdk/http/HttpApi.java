package cn.berfy.sdk.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

import cn.berfy.sdk.http.callback.Callback;
import cn.berfy.sdk.http.callback.DownloadFileCallBack;
import cn.berfy.sdk.http.callback.HttpCallBack;
import cn.berfy.sdk.http.callback.OnStatusListener;
import cn.berfy.sdk.http.callback.SuperOkHttpCallBack;
import cn.berfy.sdk.http.config.Constant;
import cn.berfy.sdk.http.config.ServerStatusCodes;
import cn.berfy.sdk.http.http.interceptor.AddCookiesInterceptor;
import cn.berfy.sdk.http.http.interceptor.Md5SignInterceptor;
import cn.berfy.sdk.http.http.interceptor.ReceivedCookiesInterceptor;
import cn.berfy.sdk.http.http.interceptor.UserAgentInterceptor;
import cn.berfy.sdk.http.http.okhttp.OkHttpUtils;
import cn.berfy.sdk.http.http.okhttp.builder.GetBuilder;
import cn.berfy.sdk.http.http.okhttp.https.HttpsUtils;
import cn.berfy.sdk.http.http.okhttp.log.LoggerInterceptor;
import cn.berfy.sdk.http.http.okhttp.request.RequestCall;
import cn.berfy.sdk.http.http.okhttp.utils.FileUtils;
import cn.berfy.sdk.http.http.okhttp.utils.GsonUtil;
import cn.berfy.sdk.http.http.okhttp.utils.Hmac;
import cn.berfy.sdk.http.http.okhttp.utils.LogF;
import cn.berfy.sdk.http.http.okhttp.utils.NetworkUtil;
import cn.berfy.sdk.http.model.HttpParams;
import cn.berfy.sdk.http.model.NetError;
import cn.berfy.sdk.http.util.Des;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpApi {

    private Context mContext;
    public static HttpApi mHttpApi;
    private OnStatusListener mOnStatusListener;
    private String mBaseUrl = "";
    private long mConnectTimeout = 20;
    private long mReadTimeout = 20;
    private long mWriteTimeout = 60;
    private HttpParams mHeaders;
    private Retrofit mRetrofit = null;

    public static HttpApi init(Context context) {
        if (null == mHttpApi) {
            mHttpApi = new HttpApi(context);
        }
        return mHttpApi;
    }

    synchronized public static HttpApi getInstances() {
        if (null == mHttpApi) {
            synchronized (HttpApi.class) {
                if (null == mHttpApi) {
                    throw new NullPointerException("没有初始化HttpApi");
                }
            }
        }
        return mHttpApi;
    }

    private HttpApi(Context context) {
        mContext = context;
        LogF.d(Constant.HTTPTAG, "接口服务初始化...");
    }

    public HttpApi setHost(String baseUrl) {
        LogF.d(Constant.HTTPTAG, "新的主机名" + baseUrl);
        mBaseUrl = baseUrl;
        return mHttpApi;
    }

    public HttpApi setTimeOut(long connectTimeout, long readTimeout, long writeTimeout) {
        mConnectTimeout = connectTimeout;
        mReadTimeout = readTimeout;
        mWriteTimeout = writeTimeout;
        return mHttpApi;
    }

    public HttpApi setHeader(HttpParams headers) {
        mHeaders = headers;
        return mHttpApi;
    }

    public HttpApi setCacheDir(String dir) {
        LogF.d(Constant.HTTPTAG, "缓存目录" + dir);
        Constant.setCacheDir(dir);
        return mHttpApi;
    }

    public HttpApi setStatusListener(OnStatusListener onStatusListener) {
        mOnStatusListener = onStatusListener;
        return mHttpApi;
    }

    public HttpApi setDebug(boolean isDebug) {
        Constant.DEBUG = isDebug;
        return mHttpApi;
    }

    public HttpApi setLogTAG(String tag) {
        Constant.HTTPTAG = tag;
        return mHttpApi;
    }

    public String getLogTAG() {
        return Constant.HTTPTAG;
    }

    public void finish() {
        LogF.d(Constant.HTTPTAG, "接口服务初始化完毕");
        if (TextUtils.isEmpty(mBaseUrl)) {
            LogF.d(Constant.HTTPTAG, "没有设置主机名");
            return;
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //设置拦截器，以用于自定义Cookies的设置
        builder.addInterceptor(new AddCookiesInterceptor(mContext, mOnStatusListener));
        builder.addInterceptor(new ReceivedCookiesInterceptor(mContext, Constant.HTTPTAG, mOnStatusListener));
        builder.addInterceptor(new Md5SignInterceptor(Constant.HTTPTAG, mOnStatusListener));//接口签名
        builder.addInterceptor(new LoggerInterceptor(Constant.HTTPTAG, true));
//        if (Constant.DEBUG) {
//            // https://drakeet.me/retrofit-2-0-okhttp-3-0-config
//            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
//                @Override
//                public void log(String message) {
//                    LogUtil.d("HttpLoggingInterceptor", message);
//                }
//            });
//            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//            builder.addInterceptor(loggingInterceptor);
//        }
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        //错误重连
        builder.retryOnConnectionFailure(true);
        builder.connectTimeout(mConnectTimeout, TimeUnit.SECONDS);
        builder.readTimeout(mReadTimeout, TimeUnit.SECONDS);
        builder.writeTimeout(mWriteTimeout, TimeUnit.SECONDS);
        String userAgent = new WebView(mContext).getSettings().getUserAgentString();
        builder.addInterceptor(new UserAgentInterceptor(mContext, getValidUA(userAgent), Constant.HTTPTAG));
        OkHttpClient okHttpClient = builder.build();
        OkHttpUtils.initClient(okHttpClient);
//        Glide.get(context).register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(getHttpClient()));

        mRetrofit = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .client(OkHttpUtils.getInstance().getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public OkHttpClient getHttpClient() {
        OkHttpClient.Builder builder1 = new OkHttpClient.Builder();
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory(null, null, null);
        builder1.addInterceptor(new LoggerInterceptor(Constant.HTTPTAG, true));
        builder1.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        return builder1.build();
    }

    public <T> T getServer(final Class<T> service) {
        return mRetrofit.create(service);
    }

    public void get(String url, HttpParams httpParams, HttpCallBack callback) {
        SuperOkHttpCallBack<String> superOkHttpCallBack = new SuperOkHttpCallBack<String>(callback);
        get(mBaseUrl, url, httpParams, superOkHttpCallBack, false);
    }

    public void get(String host, String url, HttpParams httpParams, HttpCallBack callback) {
        SuperOkHttpCallBack<String> superOkHttpCallBack = new SuperOkHttpCallBack<String>(callback);
        get(host, url, httpParams, superOkHttpCallBack, false);
    }

    //（不开放）
    private <T> void superGet(String url, HttpParams httpParams, SuperOkHttpCallBack<T> callback) {
        get(mBaseUrl, url, httpParams, callback, true);
    }

    //get请求（不开放）
    private <T> void get(String host, String url, HttpParams httpParams, SuperOkHttpCallBack<T> callback, boolean needGson) {
        if (TextUtils.isEmpty(host)) {
            if (null != callback) {
                callback.error(ServerStatusCodes.RET_CODE_SYSTEM_ERROR, 0, "没有host");
            }
            return;
        }
        //host有/
        boolean isHasHostIndex = false;
        if (host.substring(host.length() - 1, host.length()).equals("/")) {
            isHasHostIndex = true;
        }
        //去除多余/
        if (isHasHostIndex && !TextUtils.isEmpty(url) && url.substring(0, 1).equals("/")) {
            url = url.substring(1, url.length());
        }
        if (null == httpParams) {
            httpParams = new HttpParams();
        }
        final String finalUrl = host + url;
        GetBuilder builder = OkHttpUtils.get()
                .url(finalUrl);
        //添加header
        Iterator<Map.Entry<String, Object>> headers = httpParams.getHeaders().entrySet().iterator();
        while (headers.hasNext()) {
            Map.Entry<String, Object> entry = headers.next();
            if (!TextUtils.isEmpty(entry.getKey()) && !TextUtils.isEmpty(entry.getValue().toString())) {
                builder.addHeader(entry.getKey().trim(), entry.getValue().toString());
            }
        }
        //添加params参数
        Iterator<Map.Entry<String, Object>> params = httpParams.getParams().entrySet().iterator();
        while (params.hasNext()) {
            Map.Entry<String, Object> entry = params.next();
            if (!TextUtils.isEmpty(entry.getKey()) && !TextUtils.isEmpty(entry.getValue().toString())) {
                builder.addParams(entry.getKey().trim(), entry.getValue().toString());
            }
        }
        if (null != callback) {
            callback.start();
        }
        RequestCall call = builder
                .tag(System.currentTimeMillis())
                .build();
        call.execute(new Callback<Response>() {
            @Override
            public Response parseNetworkResponse(Response response, int id) throws Exception {
                return response;
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                doError(call, finalUrl, e, callback);
            }

            @Override
            public void onResponse(Response response, int id) {
                checkResult(response, callback, needGson);
            }
        });
    }

    //post请求
    public void post(String url, HttpParams httpParams, HttpCallBack callback) {
        SuperOkHttpCallBack<String> superOkHttpCallBack = new SuperOkHttpCallBack<String>(callback);
        switch (httpParams.getContentType()) {
            case POST_TYPE_FORM:
                post(mBaseUrl, url, httpParams, superOkHttpCallBack, false);
                break;
            case POST_TYPE_JSON:
                postJson(mBaseUrl, url, httpParams, superOkHttpCallBack, false);
                break;
        }
    }

    //post请求
    public void post(String host, String url, HttpParams httpParams, HttpCallBack callback) {
        SuperOkHttpCallBack<String> superOkHttpCallBack = new SuperOkHttpCallBack<String>(callback);
        switch (httpParams.getContentType()) {
            case POST_TYPE_FORM:
                post(host, url, httpParams, superOkHttpCallBack, false);
                break;
            case POST_TYPE_JSON:
                postJson(host, url, httpParams, superOkHttpCallBack, false);
                break;
            default:
                post(host, url, httpParams, superOkHttpCallBack, false);
                break;
        }
    }

    //post请求（不开放）
    private <T> void superPost(String url, HttpParams httpParams, SuperOkHttpCallBack<T> callback) {
        switch (httpParams.getContentType()) {
            case POST_TYPE_FORM:
                post(mBaseUrl, url, httpParams, callback, true);
                break;
            case POST_TYPE_JSON:
                postJson(mBaseUrl, url, httpParams, callback, true);
                break;
        }
    }

    //form post请求（不开放）
    private <T> void post(String host, String url, HttpParams httpParams, SuperOkHttpCallBack<T> callback, boolean needGson) {
        if (TextUtils.isEmpty(host)) {
            if (null != callback) {
                callback.error(ServerStatusCodes.RET_CODE_SYSTEM_ERROR, 0, "没有host");
            }
            return;
        }
        //host有/
        boolean isHasHostIndex = false;
        if (host.substring(host.length() - 1, host.length()).equals("/")) {
            isHasHostIndex = true;
        }
        if (isHasHostIndex && !TextUtils.isEmpty(url) && url.substring(0, 1).equals("/")) {
            url = url.substring(1, url.length());
        }
        if (null == httpParams) {
            httpParams = new HttpParams();
        }
        final String finalUrl = host + url;

        Request.Builder requestBuilder = new Request.Builder();
        //添加header
        Iterator<Map.Entry<String, Object>> headers = httpParams.getHeaders().entrySet().iterator();
        while (headers.hasNext()) {
            Map.Entry<String, Object> entry = headers.next();
            if (!TextUtils.isEmpty(entry.getKey()) && !TextUtils.isEmpty(entry.getValue().toString())) {
                requestBuilder.addHeader(entry.getKey().trim(), entry.getValue().toString());
            }
        }

        //添加form参数
        FormBody.Builder builder = new FormBody.Builder();
        Iterator<Map.Entry<String, Object>> params = httpParams.getParams().entrySet().iterator();
        while (params.hasNext()) {
            Map.Entry<String, Object> entry = params.next();
            if (!TextUtils.isEmpty(entry.getKey()) && !TextUtils.isEmpty(entry.getValue().toString())) {
                builder.add(entry.getKey().trim(), entry.getValue().toString());
            }
        }
        if (null != callback) {
            callback.start();
        }
        Request requestPost = requestBuilder
                .url(finalUrl)
                .post(builder.build())
                .tag(System.currentTimeMillis())
                .build();
        OkHttpUtils.getInstance().getOkHttpClient().newCall(requestPost).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                OkHttpUtils.getInstance().getPlatform().execute(new Runnable() {
                    @Override
                    public void run() {
                        doError(call, finalUrl, e, callback);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                OkHttpUtils.getInstance().getPlatform().execute(new Runnable() {
                    @Override
                    public void run() {
                        checkResult(response, callback, needGson);
                    }
                });
            }
        });
    }

    public String getHmac(String text) {
        return Hmac.md5(text);
    }

    //body json post请求
    private <T> void postJson(String url, HttpParams httpParams, SuperOkHttpCallBack<T> callback, boolean needGson) {
        postJson(mBaseUrl, url, httpParams, callback, needGson);
    }

    //body json post请求
    private <T> void postJson(String host, String url, HttpParams httpParams, SuperOkHttpCallBack<T> callback, boolean needGson) {
        if (TextUtils.isEmpty(host)) {
            if (null != callback)
                callback.error(ServerStatusCodes.RET_CODE_SYSTEM_ERROR, 0, "没有host");
            return;
        }
        //host有/
        boolean isHasHostIndex = false;
        if (host.substring(host.length() - 1, host.length()).equals("/")) {
            isHasHostIndex = true;
        }
        if (isHasHostIndex && !TextUtils.isEmpty(url) && url.substring(0, 1).equals("/")) {
            url = url.substring(1, url.length());
        }
        if (null == httpParams) {
            httpParams = new HttpParams();
        }
        String json = "";
        final String finalUrl = host + url;
        //Hashmap序列化json参数
        try {
            json = GsonUtil.getInstance().toJson(httpParams.getParams());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != callback) {
            callback.start();
        }
        //添加Json body参数
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request.Builder requestBuilder = new Request.Builder();
        //添加header
        Iterator<Map.Entry<String, Object>> headers = httpParams.getHeaders().entrySet().iterator();
        while (headers.hasNext()) {
            Map.Entry<String, Object> entry = headers.next();
            if (!TextUtils.isEmpty(entry.getKey()) && !TextUtils.isEmpty(entry.getValue().toString())) {
                requestBuilder.addHeader(entry.getKey().trim(), entry.getValue().toString());
            }
        }
        Request requestPost = requestBuilder
                .url(finalUrl)
                .tag(System.currentTimeMillis())
                .post(requestBody)
                .build();
        OkHttpUtils.getInstance().getOkHttpClient().newCall(requestPost).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                OkHttpUtils.getInstance().getPlatform().execute(new Runnable() {
                    @Override
                    public void run() {
                        doError(call, finalUrl, e, callback);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                OkHttpUtils.getInstance().getPlatform().execute(new Runnable() {
                    @Override
                    public void run() {
                        checkResult(response, callback, needGson);
                    }
                });
            }
        });
    }

    public void postFile(String url, String localPath, HttpCallBack callback) {
        postFile(mBaseUrl, url, localPath, callback);
    }

    public void postFile(String host, String url, String localPath, HttpCallBack callback) {
        if (TextUtils.isEmpty(host)) {
            if (null != callback) {
                NetError netError = new NetError();
                netError.statusCode = ServerStatusCodes.RET_CODE_SYSTEM_ERROR;
                netError.errMsg = "没有host";
                netError.usedTime = 0;
                callback.onError(netError);
            }
            return;
        }
        //host有/
        boolean isHasHostIndex = false;
        if (host.substring(host.length() - 1, host.length()).equals("/")) {
            isHasHostIndex = true;
        }
        if (isHasHostIndex && !TextUtils.isEmpty(url) && url.substring(0, 1).equals("/")) {
            url = url.substring(1, url.length());
        }
        final String finalUrl = host + url;
        SuperOkHttpCallBack<String> superOkHttpCallBack = new SuperOkHttpCallBack<String>(callback);
        RequestCall requestCall = OkHttpUtils.postFile()
                .file(new File(localPath))
                .url(finalUrl)
                .tag(System.currentTimeMillis())
                .build();
        requestCall.execute(new Callback<Response>() {
            @Override
            public Response parseNetworkResponse(Response response, int id) throws Exception {
                return response;
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                doError(call, finalUrl, e, superOkHttpCallBack);
            }

            @Override
            public void onResponse(Response response, int id) {
                checkResult(response, superOkHttpCallBack, false);
            }
        });
    }

    public void downFile(String url, DownloadFileCallBack callBack) {
        String localPath = Constant.FILE_CACHE + File.separator + Hmac.md5(url.getBytes());
        downFile(url, localPath, callBack);
    }

    public void downFile(String url, String localPath, DownloadFileCallBack callBack) {
        final Request request = new Request.Builder().tag(System.currentTimeMillis()).url(url).build();
        final Call call = OkHttpUtils.getInstance().getOkHttpClient().newCall(request);
        if (FileUtils.exists(localPath)) {
            OkHttpUtils.getInstance().getPlatform().execute(new Runnable() {
                @Override
                public void run() {
                    if (null != callBack) {
                        LogF.d(Constant.HTTPTAG, "保存成功 文件已经存在" + localPath);
                        callBack.onSuccess(url, localPath);
                    }
                }
            });
        } else {
            if (null != callBack) {
                callBack.onStart(url);
            }
            call.enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (null != callBack) {
                        callBack.onError(url, e.getMessage());
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    FileOutputStream fos = null;
                    InputStream is = null;
                    try {
                        long total = response.body().contentLength();
                        Log.e(Constant.HTTPTAG, "文件大小 total------>" + total);
                        File file = new File(localPath);
                        byte[] buf = new byte[2048];
                        int len = 0;
                        long current = 0;
                        is = response.body().byteStream();
                        fos = new FileOutputStream(file);
                        while ((len = is.read(buf)) != -1) {
                            current += len;
                            fos.write(buf, 0, len);
                            Log.e(Constant.HTTPTAG, "文件大小 下载中------>" + total + ":" + current);
                        }
                        fos.flush();
                        OkHttpUtils.getInstance().getPlatform().execute(new Runnable() {
                            @Override
                            public void run() {
                                if (null != callBack) {
                                    LogF.d(Constant.HTTPTAG, "保存成功" + file.getAbsolutePath());
                                    callBack.onSuccess(url, file.getAbsolutePath());
                                }
                            }
                        });
                    } catch (IOException e) {
                        LogF.d(Constant.HTTPTAG, "保存失败" + e.getMessage());
                        OkHttpUtils.getInstance().getPlatform().execute(new Runnable() {
                            @Override
                            public void run() {
                                if (null != callBack) {
                                    callBack.onError(url, e.getMessage());
                                }
                            }
                        });
                    } finally {
                        try {
                            if (is != null) {
                                is.close();
                            }
                            if (fos != null) {
                                fos.close();
                            }
                        } catch (IOException e) {
                            LogF.d(Constant.HTTPTAG, "保存失败" + e.getMessage());
                            OkHttpUtils.getInstance().getPlatform().execute(new Runnable() {
                                @Override
                                public void run() {
                                    if (null != callBack) {
                                        callBack.onError(url, e.getMessage());
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    private void doError(Call call, String url, Exception e, SuperOkHttpCallBack callBack) {
        long usedTime = null == call.request().tag() ? 0 : Long.valueOf(call.request().tag().toString());
        String msg = "";
        int statusCode = ServerStatusCodes.RET_CODE_SYSTEM_ERROR;
        if (!NetworkUtil.isNetAvailable(mContext)) {
            msg = "没有网络";
            statusCode = ServerStatusCodes.NO_NET;
        } else {
            if (null != e && null != e.getMessage()) {
                if (e.getMessage().contains("after") && e.getMessage().contains("ms") && e.getMessage().startsWith("failed to connect to")) {
                    msg = "请求超时";
                    statusCode = ServerStatusCodes.RET_CODE_SYSTEM_ERROR;
                } else {
                    msg = "未知错误";
                    statusCode = ServerStatusCodes.RET_CODE_SYSTEM_ERROR;
                }
            } else {
                msg = "未知错误";
                statusCode = ServerStatusCodes.RET_CODE_SYSTEM_ERROR;
                msg += "\n" + e.getMessage() + "\n" + e.getCause();
                LogF.d(Constant.HTTPTAG, "请求失败doError\n" + url + "\n" + e.getMessage() + "\n" + e.getCause());
            }
        }
        if (null != callBack) {
            callBack.error(statusCode, System.currentTimeMillis() - usedTime, msg);
            callBack.errorDetail(statusCode, statusCode);
        }
        if (null != mOnStatusListener) {
            mOnStatusListener.statusCodeError(statusCode, System.currentTimeMillis() - usedTime);
        }
    }

    //拦截错误
    private void checkResult(Response response, SuperOkHttpCallBack callBack, boolean needGson) {
        long usedTime = null == response.request().tag() ? 0 : Long.valueOf(response.request().tag().toString());
        if (null == response) {
            LogF.d(Constant.HTTPTAG, "请求错误response==null");
            if (null != callBack) {
                callBack.error(ServerStatusCodes.RET_CODE_SYSTEM_ERROR, System.currentTimeMillis() - usedTime, "返回值为空");
                callBack.errorDetail(ServerStatusCodes.RET_CODE_SYSTEM_ERROR, ServerStatusCodes.RET_CODE_SYSTEM_ERROR);
            }
            if (null != mOnStatusListener) {
                mOnStatusListener.statusCodeError(ServerStatusCodes.RET_CODE_SYSTEM_ERROR, System.currentTimeMillis() - usedTime);
            }
            return;
        }
        if (null == response.body()) {
            LogF.d(Constant.HTTPTAG, "请求错误null == response.body()");
            if (null != callBack) {
                callBack.error(ServerStatusCodes.RET_CODE_SYSTEM_ERROR, System.currentTimeMillis() - usedTime, "返回值为空");
                callBack.errorDetail(response.code(), ServerStatusCodes.RET_CODE_SYSTEM_ERROR);
            }
            return;
        }
        //只单独拦截了404
//        if (response.code() == 404) {
//            LogF.d(Constant.HTTPTAG, "请求错误404");
//            callBack.errorDetail(response.code(), ServerStatusCodes.RET_CODE_SYSTEM_ERROR);
//            if (null != callBack) {
//                callBack.error("请求错误404");
//            }
//            if (null != mOnStatusListener) {
//                mOnStatusListener.statusCodeError(404);
//            }
//            return;
//        }
        if (response.code() >= 200 && response.code() <= 300) {//状态码
            try {
                String resultJson = response.body().string();
//                JSONObject json = new JSONObject(resultJson);
//                int code = json.optInt("code");
//                String msg = json.optString("msg");
//                String data = json.optString("res");
//                switch (code) {
////                    case ServerStatusCodes.RET_CODE_NOT_LOGIN://未登录，切换服务器，闪退心跳包丢失都会造成与后台session失效
////                        LogF.d(HttpConstants.HTTPTAG, "---检测到您的账号没有登录 未登录，切换服务器，闪退心跳包丢失都会造成与后台session失效---");
////                        if (null != mOnStatusListener) {
////                            mOnStatusListener.toLogin(msg);
////                        }
////                        //标记状态 否则不会退出登录
////                        BaseApplication.setIsSeize(true);
////                        break;
////                    case ServerStatusCodes.RET_CODE_ROBBED://被踢
////                        LogF.d(HttpConstants.HTTPTAG, "---检测到您的账号在另外一台设备上登录---");
////                        //标记状态 否则不会退出登录
////                        BaseApplication.setIsSeize(true);
////                        break;
////                    case ServerStatusCodes.RET_CODE_FROZEN://账号被冻结
////                        LogF.d(HttpConstants.HTTPTAG, "---账号被冻结---");
////                        if (null != mOnStatusListener) {
////                            mOnStatusListener.frozenAccount(msg);
////                        }
////                        break;
////                    case ServerStatusCodes.RET_CODE_FORCED_UPDATE://强制更新
////                        ApkDownloadUrl apkDownloadUrl = GsonUtil.getInstance().toClass(data, ApkDownloadUrl.class);
////                        LogF.d(HttpConstants.HTTPTAG, "---检测到有新的版本，需要强制更新---");
////                        if (null != apkDownloadUrl) {
////                            LogF.d(HttpConstants.HTTPTAG, "---下载地址" + apkDownloadUrl.dowload_url);
////                            if (null != mOnStatusListener) {
////                                mOnStatusListener.forcedUpdate(msg, apkDownloadUrl.dowload_url);
////                            }
////                        }
////                        break;
//                    default:
//                        if (null != callBack) {
//                            callBack.finish(resultJson);
//                        }
//                        break;
//                }
                if (null != callBack) {
                    callBack.finish(response.code(), System.currentTimeMillis() - usedTime, resultJson, needGson);
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogF.d(Constant.HTTPTAG, "请求错误码解析出错" + e.getMessage());
                if (null != callBack) {
//                callBack.error(DataUtil.getString(R.string.data_network, context));
                    callBack.errorDetail(ServerStatusCodes.RET_CODE_SYSTEM_ERROR, ServerStatusCodes.RET_CODE_SYSTEM_ERROR);
                    callBack.error(ServerStatusCodes.RET_CODE_SYSTEM_ERROR, System.currentTimeMillis() - usedTime, e.getMessage());
                }
                if (null != mOnStatusListener) {
                    mOnStatusListener.statusCodeError(ServerStatusCodes.RET_CODE_SYSTEM_ERROR, System.currentTimeMillis() - usedTime);
                }
            }
        } else {
            if (null != mOnStatusListener) {
                mOnStatusListener.statusCodeError(response.code(), System.currentTimeMillis() - usedTime);
            }
        }
    }

    /**
     * @param userAgent
     * @return 去除非法字符
     */
    private String getValidUA(String userAgent) {
        if (TextUtils.isEmpty(userAgent)) {
            return "android";
        }
        String validUA = "android";
        String uaWithoutLine = userAgent.replace("\n", "");
        for (int i = 0, length = uaWithoutLine.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                try {
                    validUA = URLEncoder.encode(uaWithoutLine, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return validUA;
            }
        }
        return uaWithoutLine;
    }

    public String encode3Des(String str) {
        return Des.encode(get3DesKey(), str);
    }

    public String decode3Des(String desStr) {
        return Des.decode(get3DesKey(), desStr);
    }

    public String encodeMD5(boolean isUpper, String str) {
        return encodeMd5JNI(isUpper, str);
    }

    public String encodeBase64(String str) {
        return encodeBase64JNI(str);
    }

    public String decodeBase64(String str) {
        return decodeBase64JNI(str);
    }

    //加载so库
    static {
        System.loadLibrary("httpjni");
    }

    //md5加密
    private static native String encodeMd5JNI(boolean isUpper, String text);

    //base64加密
    private static native String encodeBase64JNI(String text);

    //base64解密
    private static native String decodeBase64JNI(String base64);

    //DES加密
//    public static native int encode3DES(byte[] aes, byte[] key, byte[] py);

    //DES解密
//    public static native int decode3DES(byte[] key, byte[] py, byte[] result);

    //获取DES key
    private static native String getDesKey();

    //获取3DES key
    private static native String get3DesKey();

    //获取DES idcard
    private static native String getDesCipher();
}
