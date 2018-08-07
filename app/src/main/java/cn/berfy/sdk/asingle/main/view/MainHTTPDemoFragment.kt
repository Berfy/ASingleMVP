package cn.berfy.sdk.asingle.main.view

import android.Manifest
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import cn.berfy.sdk.asingle.R
import cn.berfy.sdk.asingle.demohttp.AWebView
import cn.berfy.sdk.asingle.demohttp.WaterWaveView
import cn.berfy.sdk.asingle.demohttp.model.Data
import cn.berfy.sdk.asingle.demohttp.rxjavademo.model.Book
import cn.berfy.sdk.asingle.demohttp.util.Base64
import cn.berfy.sdk.asingle.demohttp.util.MD5
import cn.berfy.sdk.asingle.main.presenter.MainHTTPDemoPresenter
import cn.berfy.sdk.http.HttpApi
import cn.berfy.sdk.http.callback.HttpCallBack
import cn.berfy.sdk.http.callback.OnStatusListener
import cn.berfy.sdk.http.http.okhttp.utils.GsonUtil
import cn.berfy.sdk.http.model.HttpParams
import cn.berfy.sdk.http.model.NetError
import cn.berfy.sdk.http.model.NetResponse
import cn.berfy.sdk.mvpbase.base.CommonFragment
import cn.berfy.sdk.mvpbase.util.DisplayUtil
import cn.berfy.sdk.mvpbase.util.ToastUtil
import com.hjq.permissions.OnPermission
import com.hjq.permissions.XXPermissions
import kotlinx.android.synthetic.main.fragment_main_http.*

class MainHTTPDemoFragment : CommonFragment<IMainHTTPDemoView, MainHTTPDemoPresenter>(), IMainHTTPDemoView, View.OnClickListener {

    private lateinit var mWebView1: AWebView
    private lateinit var mWebView2: AWebView
    private lateinit var mWaterWaveView: WaterWaveView
    private lateinit var mBtnAnim: Button
    private lateinit var mBtnMd5Java: Button
    private lateinit var mBtnMd5HmacJava: Button
    private lateinit var mBtnMd5Base64: Button
    private lateinit var mBtnMd5Base64Java: Button
    private lateinit var mBtnHttpGET: Button
    private lateinit var mBtnHttpPOST: Button
    private lateinit var mBtnHttpRxjavaGET: Button
    private lateinit var mBtnHttpRxjavaPOST: Button

    override fun getContentViewId(): Int {
        return R.layout.fragment_main_http
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initView() {
        mWaterWaveView = findViewById(R.id.waterWaveView)
        val layoutParams = mWaterWaveView.layoutParams as RelativeLayout.LayoutParams
        layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT
        layoutParams.height = DisplayUtil.getDisplayHeight(mContext)
        mWaterWaveView.setLayoutParams(layoutParams)
        mWaterWaveView.startWave()
        mWebView1 = findViewById(R.id.webView1)
        mWebView2 = findViewById(R.id.webView2)
        mBtnMd5Java = findViewById(R.id.btn_md5_java)
        mBtnMd5HmacJava = findViewById(R.id.btn_md5_hmac)
        mBtnMd5Base64 = findViewById(R.id.btn_md5_base64)
        mBtnMd5Base64Java = findViewById(R.id.btn_md5_base64_java)
        mBtnHttpGET = findViewById(R.id.btn_http_get)
        mBtnHttpPOST = findViewById(R.id.btn_http_post)
        mBtnHttpRxjavaGET = findViewById(R.id.btn_http_rxjava_get)
        mBtnHttpRxjavaPOST = findViewById(R.id.btn_http_rxjava_post)
        mBtnAnim = findViewById(R.id.btn_anim)
        mBtnAnim.setOnClickListener(this)
        btn_md5_c.setOnClickListener(this)
        mBtnMd5Java.setOnClickListener(this)
        mBtnMd5HmacJava.setOnClickListener(this)
        mBtnMd5Base64.setOnClickListener(this)
        mBtnMd5Base64Java.setOnClickListener(this)
        mBtnHttpGET.setOnClickListener(this)
        mBtnHttpPOST.setOnClickListener(this)
        mBtnHttpRxjavaGET.setOnClickListener(this)
        mBtnHttpRxjavaPOST.setOnClickListener(this)
        btn_3des.setOnClickListener(this)
        HttpApi.getInstances()
                .setHost("http://223.203.221.49:18610/")
                .setStatusListener(object : OnStatusListener {
                    override fun statusCodeError(i: Int, usedTime: Long) {
                        Log.d("httpLog", "测试  请求错误码$i")
                        tv_response.setText("请求错误码" + i + "  耗时:" + usedTime + "ms")
                    }

                    override fun addParams(rawParams: HttpParams): HttpParams? {
                        Log.d(HttpApi.getInstances().logTAG, "测试  请求参数  " + GsonUtil.getInstance().toJson(rawParams.params))
                        Log.d(HttpApi.getInstances().logTAG, "测试  请求Headers  " + GsonUtil.getInstance().toJson(rawParams.headers))
                        rawParams.putParam("sign_ts", System.currentTimeMillis().toString() + "")
                        val headers = rawParams.params.entries.iterator()
                        val sb = StringBuffer()
                        while (headers.hasNext()) {
                            val entry = headers.next()
                            sb.append(entry.key.trim { it <= ' ' }).append("=").append(entry.value.toString())
                        }

                        val httpParams1 = HttpParams()
                        httpParams1.putParam("sign_ts", rawParams.params["ts"])
                        httpParams1.putHeader("sign", HttpApi.getInstances().encodeMD5(false, sb.toString()))
                        return null
                    }

                    override fun receiveSetCookie(s: String) {

                    }

                    override fun addCookies(): HttpParams? {
                        return null
                    }
                })
                .setLogTAG("httpLog")
                .finish()
        mWebView1.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                super.onReceivedSslError(view, handler, error)
                if (null != handler)
                    handler.proceed()  // 接受所有网站的证书
            }

            //是否在webview内加载页面
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                if (null != view && null != request) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        view.loadUrl(request.url.toString())
                    } else {
                        view.loadUrl(request.toString())
                    }
                }
                return false
            }
        }
        mWebView2.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                super.onReceivedSslError(view, handler, error)
                if (null != handler)
                    handler.proceed()  // 接受所有网站的证书
            }

            //是否在webview内加载页面
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                if (null != view && null != request) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        view.loadUrl(request.url.toString())
                    } else {
                        view.loadUrl(request.toString())
                    }
                }
                return false
            }
        }
        XXPermissions.with(activity)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request(object : OnPermission {
                    override fun hasPermission(granted: List<String>, isAll: Boolean) {
                        if (isAll) {

                        }
                    }

                    override fun noPermission(denied: List<String>, quick: Boolean) {

                    }
                })
        mHandler.sendEmptyMessage(0)
        mWebView1?.visibility = View.VISIBLE
        mWebView2?.visibility = View.VISIBLE
    }

    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            mWebView1.loadUrl("http://blog.sina.com.cn/s/blog_472b14140102xxvg.html")
            mWebView2.loadUrl("http://blog.sina.com.cn/s/blog_472b14140102xuw4.html")
            sendEmptyMessageDelayed(0, 1500)
        }
    }

    override fun initPresenter(): MainHTTPDemoPresenter {
        return MainHTTPDemoPresenter()
    }

    override fun hiddenLoadingView(msg: String?) {
    }

    override fun showLoadingView(msg: String?) {
    }

    override fun onClick(v: View) {
        when (v.getId()) {
            R.id.btn_md5_c -> {
                var time = System.currentTimeMillis()
                var md5 = edit_md5.getText().toString().trim()
                tv_md5.setText(HttpApi.getInstances().encodeMD5(true, md5) + "  耗时:" + (System.currentTimeMillis() - time) + "ms")
            }
            R.id.btn_md5_java -> {
                var time = System.currentTimeMillis()
                var md5 = edit_md5.getText().toString().trim()
                tv_md5.setText(MD5.getStringMD5(md5).toUpperCase() + "  耗时:" + (System.currentTimeMillis() - time) + "ms")
            }
            R.id.btn_md5_hmac -> {
                var time = System.currentTimeMillis()
                var md5 = edit_md5.getText().toString().trim()
                tv_md5.setText(HttpApi.getInstances().getHmac(md5).toUpperCase() + "  耗时:" + (System.currentTimeMillis() - time) + "ms")
            }
            R.id.btn_md5_base64 -> {
                var time = System.currentTimeMillis()
                var md5 = edit_md5.getText().toString().trim()
                tv_md5.setText(HttpApi.getInstances().encodeBase64(md5) + "  耗时:" + (System.currentTimeMillis() - time) + "ms")
            }
            R.id.btn_md5_base64_java -> {
                var time = System.currentTimeMillis()
                var md5 = edit_md5.getText().toString().trim()
                try {
                    tv_md5.setText(Base64.encode(md5.toByteArray(charset("UTF-8"))) + "  耗时:" + (System.currentTimeMillis() - time) + "ms")
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            R.id.btn_3des -> {
                var time = System.currentTimeMillis()
                var md5 = edit_md5.getText().toString().trim()
                tv_md5.setText(HttpApi.getInstances().encode3Des(md5) + "  耗时:" + (System.currentTimeMillis() - time) + "ms")
            }
            R.id.btn_http_get -> {
                var httpParams = HttpParams()
                httpParams.putParam("a", 1)
                httpParams.putParam("b", 2)
                httpParams.putHeader("hearfer", 1)
                HttpApi.getInstances().get("", httpParams, object : HttpCallBack {

                    override fun onStart() {
                        Log.d("请求开始", "===")
                    }

                    override fun onFinish(netResponse: NetResponse<String>) {
                        Log.d("返回成功", "statusCode = " + netResponse.statusCode + " 返回值" + netResponse.data + " 耗时:" + netResponse.usedTime + "ms")
                        tv_response.text = netResponse.data + " 耗时:" + netResponse.usedTime + "ms"
                    }

                    override fun onError(netError: NetError) {
                        Log.d("返回错误", "statusCode = " + netError.statusCode + " 错误信息" + netError.errMsg + " 耗时:" + netError.usedTime + "ms")
                        tv_response.text = netError.errMsg + " 耗时:" + netError.usedTime + "ms"
                    }
                })
            }
            R.id.btn_http_post -> {
                var httpParams = HttpParams()
                httpParams.setContentType(HttpParams.POST_TYPE.POST_TYPE_JSON)
                httpParams.putParam("a", 1)
                httpParams.putParam("b", 2)
                httpParams.putHeader("hearfer", 1)
                HttpApi.getInstances().post("", httpParams, object : HttpCallBack {

                    override fun onStart() {
                        Log.d("请求开始", "===")
                    }

                    override fun onFinish(netResponse: NetResponse<String>) {
                        Log.d("返回成功", "statusCode = " + netResponse.statusCode + " 返回值" + netResponse.data + " 耗时:" + netResponse.usedTime + "ms")
                        tv_response.text = netResponse.data + " 耗时:" + netResponse.usedTime + "ms"
                    }

                    override fun onError(netError: NetError) {
                        Log.d("返回错误", "statusCode = " + netError.statusCode + " 错误信息" + netError.errMsg + " 耗时:" + netError.usedTime + "ms")
                        tv_response.text = netError.errMsg + " 耗时:" + netError.usedTime + "ms"
                    }
                })
            }
            R.id.btn_anim -> if (mWaterWaveView.isRunning) {
                mBtnAnim.text = "开始动画"
                mWaterWaveView.stop()
                mWaterWaveView.visibility = View.GONE
            } else {
                mBtnAnim.text = "关闭动画"
                mWaterWaveView.visibility = View.VISIBLE
                mWaterWaveView.startWave()
            }
            R.id.btn_http_rxjava_get -> presenter.checkUpdate("1.4.6", object : rx.Observer<Data<Book>> {
                override fun onCompleted() {}

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    ToastUtil.getInstances().showShort(e.message)
                }

                override fun onNext(book: Data<Book>?) {
                    if (null != book) {
                        ToastUtil.getInstances().showShort("获取成功" + GsonUtil.getInstance().toJson<Any>(book))
                    } else {
                        ToastUtil.getInstances().showShort("获取成功book=null")
                    }
                }
            })
            R.id.btn_http_rxjava_post -> presenter.getSearchBooks("1", "", 1, 10, object : rx.Observer<Book> {
                override fun onCompleted() {}

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    ToastUtil.getInstances().showShort(e.message)
                }

                override fun onNext(book: Book?) {
                    if (null != book) {
                        ToastUtil.getInstances().showShort("获取成功" + GsonUtil.getInstance().toJson<Any>(book))
                    } else {
                        ToastUtil.getInstances().showShort("获取成功book=null")
                    }
                }
            })
        }
    }
}