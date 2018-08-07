package cn.berfy.sdk.asingle.demohttp;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by Berfy on 2017/9/25.
 * 自定义的优化的WebView
 */

public class AWebView extends WebView {

    private final String TAG = "AWebView";

    public AWebView(Context context) {
        super(context);
        init(context);
    }

    public AWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        getSettings().setJavaScriptEnabled(true);//支持JS
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置允许JS打开新窗口
        getSettings().setAllowFileAccess(true);// 设置允许访问文件数据
        getSettings().setSupportZoom(true);//启用缩放
        getSettings().setBuiltInZoomControls(true);//启动缩放工具
        getSettings().setDomStorageEnabled(true);
        getSettings().setDatabaseEnabled(true);
        getSettings().setSavePassword(false);//禁止存储密码
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setUseWideViewPort(true);
        getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        getSettings().setAppCacheEnabled(true);
        getSettings().setGeolocationEnabled(true);
        getSettings().getUserAgentString();
        String ua = getSettings().getUserAgentString();
//        getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);//设置不缓存
        clearCache(true);
        setAcceptThirdPartyCookies();
        versionCheck();
    }

    /**
     * 版本适配
     */
    private void versionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    /**
     * 设置跨域cookie读取
     */
    private void setAcceptThirdPartyCookies() {
        //target 23 default false, so manual set true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true);
        }
    }

    @Override
    public void loadUrl(String url) {
        Log.i(TAG, "加载网址" + url);
        super.loadUrl(url);
    }

    @Override
    public void destroy() {
        getSettings().setBuiltInZoomControls(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // call requies API Level 11 ( Android 3.0 + )
            getSettings().setDisplayZoomControls(false);
        }
        setVisibility(View.GONE);
        pauseTimers();
        ViewGroup view = (ViewGroup) getRootView();
        view.removeAllViews();
        super.destroy();
    }
}
