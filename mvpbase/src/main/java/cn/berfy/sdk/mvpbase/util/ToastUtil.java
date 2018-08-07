package cn.berfy.sdk.mvpbase.util;

import android.content.Context;
import android.widget.Toast;

/**
 * 单例吐司工具类
 * Created by jjf on 2015/9/22.
 */
public class ToastUtil {

    private static ToastUtil mToastUtil;
    private Toast mToast;
    private Context mContext;

    public static void init(Context context) {
        if (null == mToastUtil) {
            mToastUtil = new ToastUtil(context);
        }
    }

    public static ToastUtil getInstances() {
        if (null == mToastUtil) {
            throw new NullPointerException("空指针，未初始化ToastUtil");
        }
        return mToastUtil;
    }

    private ToastUtil(Context context) {
        mContext = context;
        mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
    }

    public void showShort(String text) {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        mToast.show();
    }

    public void showShort(int textResId) {
        showShort(mContext.getString(textResId));
    }

    public void showLong(String text) {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(mContext, text, Toast.LENGTH_LONG);
        mToast.show();
    }

    public void showLong(int textResId) {
        showLong(mContext.getString(textResId));
    }
}
