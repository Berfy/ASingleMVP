package cn.berfy.sdk.mvpbase.manager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.List;

import cn.berfy.sdk.mvpbase.R;
import cn.berfy.sdk.mvpbase.model.Notify;
import cn.berfy.sdk.mvpbase.util.GsonUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import kotlin.reflect.KClass;

/**
 * Created by Berfy on 2017/11/21.
 * 推送通知管理(自定义弹起通知，销毁指定tag或者id的消息)
 * 修改2019.1.8
 */
public class NotifycationManager {

    private final String TAG = "NotifycationManager";
    private static NotifycationManager mInstance;
    private Context mContext;
    private int mIconResId = R.drawable.ic_launcher;
    private String mTitle;
    private String mContent;
    private Intent mIntent;
    private NotificationManager mNotificationManager;
    private List<Notify> mCaches = new ArrayList<>();

    synchronized public static NotifycationManager init(Context context, Class defaultIntentClass) {
        if (null == mInstance) {
            synchronized (NotifycationManager.class) {
                if (null == mInstance) {
                    mInstance = new NotifycationManager(context, defaultIntentClass);
                }
            }
        }
        return mInstance;
    }

    public static NotifycationManager getInstance() {
        if (null == mInstance) {
            throw new NullPointerException("没有初始化NotifycationUtil");
        }
        return mInstance;
    }

    public static NotifycationManager newInstance(Context context, Class defaultIntentClass) {
        return new NotifycationManager(context, defaultIntentClass);
    }

    private NotifycationManager(Context context, Class defaultIntentClass) {
        mContext = context;
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mIntent = new Intent(context, defaultIntentClass);
    }

    public void setIcon(int iconResId) {
        mIconResId = iconResId;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void reset() {
        mTitle = "";
        mContent = "";
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getContent() {
        return mContent;
    }

    public void setIntent(Intent intent) {
        mIntent = intent;
    }

    public void notify(int id) {
        if (TextUtils.isEmpty(mContent)) {
            LogF.d(TAG, "通知==> 没有内容  不予显示" + mContent);
            mContent = "";
        }
        if (TextUtils.isEmpty(mTitle)) {
            mTitle = mContext.getString(R.string.app_name);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, id, mIntent, PendingIntent
                .FLAG_ONE_SHOT);
        LogF.d(TAG, "通知==>" + mTitle);
        Notification msgNotification = makeNotification(null, null, null,
                true, pendingIntent, mTitle, mContent, mContent, mIconResId, true, true);
        mNotificationManager.notify(id, msgNotification);
        addCache(new Notify("", id));
    }

    public void notify(int id, RemoteViews remoteView, RemoteViews remoteViewBig,
                       RemoteViews remoteViewHeadUp, Boolean isAutoCancel) {
        if (TextUtils.isEmpty(mContent)) {
            LogF.d(TAG, "通知==> 没有内容  不予显示" + mContent);
            mContent = "";
        }
        if (TextUtils.isEmpty(mTitle)) {
            mTitle = mContext.getString(R.string.app_name);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, id, mIntent, PendingIntent
                .FLAG_ONE_SHOT);
        LogF.d(TAG, "通知==>" + mTitle);
        Notification msgNotification = makeNotification(remoteView, remoteViewBig, remoteViewHeadUp,
                isAutoCancel, pendingIntent, mTitle, mContent, mContent, mIconResId, true, true);
        mNotificationManager.notify(id, msgNotification);
        addCache(new Notify("", id));
    }

    public void notify(String tag) {
        if (TextUtils.isEmpty(mContent)) {
            return;
        }
        if (TextUtils.isEmpty(mTitle)) {
            mTitle = mContext.getString(R.string.app_name);
        }
        int id = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, id, mIntent, PendingIntent
                .FLAG_ONE_SHOT);
        LogF.d(TAG, "通知==>" + mTitle);
        Notification msgNotification = makeNotification(null, null, null,
                true, pendingIntent, mTitle, mContent, mContent, mIconResId, true, true);
        mNotificationManager.notify(tag + "", id, msgNotification);
        addCache(new Notify(tag, id));
    }

    public void notify(String tag, RemoteViews remoteView, RemoteViews remoteViewBig,
                       RemoteViews remoteViewHeadUp, Boolean isAutoCancel) {
        if (TextUtils.isEmpty(mContent)) {
            return;
        }
        if (TextUtils.isEmpty(mTitle)) {
            mTitle = mContext.getString(R.string.app_name);
        }
        int id = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, id, mIntent, PendingIntent
                .FLAG_ONE_SHOT);
        LogF.d(TAG, "通知==>" + mTitle);
        Notification msgNotification = makeNotification(remoteView, remoteViewBig, remoteViewHeadUp,
                isAutoCancel, pendingIntent, mTitle, mContent, mContent, mIconResId, true, true);
        mNotificationManager.notify(tag + "", id, msgNotification);
        addCache(new Notify(tag, id));
    }

    /**
     * @param remoteView
     */
    private Notification makeNotification(RemoteViews remoteView, RemoteViews remoteViewBig,
                                          RemoteViews remoteViewHeadUp, Boolean isAutoCancel, PendingIntent pendingIntent,
                                          String title, String content, String tickerText,
                                          int iconId, boolean ring, boolean vibrate) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);

        builder.setAutoCancel(isAutoCancel)
                .setContentIntent(pendingIntent)
                .setTicker(tickerText)
                .setSmallIcon(iconId);

        if (null != remoteView) {
            builder.setCustomContentView(remoteView);
        }
        if (null != remoteViewBig) {
            builder.setCustomContentView(remoteViewBig);
        }
        if (null != remoteViewHeadUp) {
            builder.setCustomContentView(remoteViewHeadUp);
        }
        if (!TextUtils.isEmpty(title)) {
            builder.setContentTitle(title);
        }
        if (!TextUtils.isEmpty(content)) {
            builder.setContentText(content);
        }
        int defaults = Notification.DEFAULT_LIGHTS;
        if (vibrate) {
            defaults |= Notification.DEFAULT_VIBRATE;
        }

        if (ring) {
            defaults |= Notification.DEFAULT_SOUND;
        }

        builder.setDefaults(defaults);
        return builder.build();
    }

    private void addCache(Notify notify) {
        LogF.d(TAG, "通知缓存 " + GsonUtil.getInstance().toJson(notify) + "  size=" + mCaches.size());
        boolean isHas = false;
        for (Notify cache : mCaches) {
            if (cache.equals(notify)) {
                isHas = true;
            }
        }
        if (!isHas) {
            mCaches.add(notify);
        }
        LogF.d(TAG, "通知缓存后大小 " + mCaches.size());
    }

    private void removeCache(Notify notify) {
        mCaches.remove(notify);
    }

    public void clearNotify(String tag) {
        LogF.d(TAG, "清除通知缓存 tag=" + tag);
        int size = mCaches.size();
        if (size == 0) {
            return;
        }
        for (int i = size - 1; i >= 0; i--) {
            Notify cache = mCaches.get(i);
            if (tag.equals(cache.getTag())) {
                LogF.d(TAG, "清除通知缓存 有通知  id=" + cache.getId() + " tag=" + tag);
                mNotificationManager.cancel(tag, cache.getId());
                removeCache(cache);
                continue;
            }
        }
    }

    public void clearNotify(int id) {
        LogF.d(TAG, "清除通知缓存 id=" + id);
        int size = mCaches.size();
        if (size == 0) {
            return;
        }
        for (int i = size - 1; i >= 0; i--) {
            Notify cache = mCaches.get(i);
            if (id == cache.getId()) {
                LogF.d(TAG, "清除通知缓存 有通知  id=" + cache.getId() + " tag=" + cache.getTag());
                mNotificationManager.cancel(cache.getId());
                removeCache(cache);
            }
        }
    }

    public void clearAll() {
        LogF.d(TAG, "清除全部通知缓存");
        int size = mCaches.size();
        if (size == 0) {
            return;
        }
        for (int i = size - 1; i >= 0; i--) {
            Notify cache = mCaches.get(i);
            mNotificationManager.cancel(cache.getId());
        }
        mCaches.clear();
        mNotificationManager.cancelAll();
    }
}
