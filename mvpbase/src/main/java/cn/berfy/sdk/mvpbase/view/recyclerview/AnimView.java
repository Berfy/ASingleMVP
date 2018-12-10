package cn.berfy.sdk.mvpbase.view.recyclerview;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import cn.berfy.sdk.mvpbase.R;
import cn.berfy.sdk.mvpbase.config.Constant;
import cn.berfy.sdk.mvpbase.util.DeviceUtils;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.StringUtils;

/**
 * Created by Berfy on 2018/1/31.
 * 下拉刷新动画
 */
public class AnimView extends LinearLayout {

    private final String TAG = "AnimView";
    private Context mContext;
    private View mView;
    private boolean mIsStartRunning;
    private boolean mIsStopRunning;
    private int mAnimTime = 50;//单程动画时间，越大动画移动越慢
    private int mAnimIntervalTime = 100;//间隔时间，越大两个View动画间隔越大
    private int mAnimStopTime = 150;//动画底部停留时间
    private int mSpeed = 8;//越小速度越快
    private float mStartY;//View图标Y坐标点
    private float mMaxMoveY;//View单程移动最大距离

    public AnimView(Context context) {
        super(context);
        init(context);
    }

    public AnimView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AnimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mView = View.inflate(mContext, R.layout.view_refresh_anim, null);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mView, layoutParams);
        mStartY = DeviceUtils.dpToPx(mContext, 10);
        mMaxMoveY = DeviceUtils.dpToPx(mContext, 3.5f);
    }

    public void startAnim() {
        if (!mIsStartRunning) {
            mIsStopRunning = false;
            mIsStartRunning = true;
            Constant.EXECUTOR_ANIM.execute(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 4; i++) {
                        final int n = i;
                        if (!mIsStopRunning) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    startAnim(n, mView.findViewById(StringUtils.getRes(mContext,
                                            "iv_anim" + (n + 1), "id")));
                                }
                            });
                        }
                        try {
                            Thread.sleep(mAnimIntervalTime);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    public void stopAnim(int delayTime) {
        mIsStartRunning = false;
        mIsStopRunning = true;
        Constant.EXECUTOR_ANIM.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(delayTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        reset();
                    }
                });
            }
        });
    }

    private void startAnim(int pos, View view) {
        LogF.d(TAG, "动画开始" + pos + "  " + System.currentTimeMillis());
        if (!mIsStopRunning) {
            Constant.EXECUTOR_ANIM.execute(new Runnable() {
                @Override
                public void run() {
                    int state = 0;//0上 1下 2停留
                    float i = mStartY;
                    int time = 0;
                    while (!mIsStopRunning) {
                        try {
                            Thread.sleep(mSpeed);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (state == 0) {
                            i -= (mMaxMoveY * mSpeed * 0.1) / (mAnimTime * 0.1);
                            if (i < mStartY - mMaxMoveY) {
                                i = mStartY - mMaxMoveY;
                                state = 1;
                            }
                            time = 0;
                        } else if (state == 1) {
                            i += (float) (mMaxMoveY * mSpeed * 0.1) / (mAnimTime * 0.1);
                            if (i > mStartY + mMaxMoveY) {
                                i = mStartY + mMaxMoveY;
                                state = 2;
                            }
                            time = 0;
                        } else {//停留
                            if (time > mAnimStopTime) {
                                state = 0;
                            }
                            time += mSpeed;
                        }
                        final float y = i;
                        if (!mIsStopRunning) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    view.setY(y);
                                }
                            });
                        }
                    }
                }
            });
//            TranslateAnimation animationTo = new TranslateAnimation(0, 0,
//                    0, -DeviceUtils.dpToPx(mContext, 10));
//            animationTo.setDuration(mAnimTime);
//            animationTo.setFillAfter(true);
//            animationTo.setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//                    TranslateAnimation animationBack = new TranslateAnimation(0, 0,
//                            -DeviceUtils.dpToPx(mContext, 10), 0);
//                    animationBack.setDuration(mAnimTime);
//                    animationBack.setFillAfter(true);
//                    animationBack.setAnimationListener(new Animation.AnimationListener() {
//                        @Override
//                        public void onAnimationStart(Animation animation) {
//
//                        }
//
//                        @Override
//                        public void onAnimationEnd(Animation animation) {
//                            if (!mIsStopRunning) {
//                                startAnim(view);
//                            }
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(Animation animation) {
//
//                        }
//                    });
//                    view.startAnimation(animationBack);
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//
//                }
//            });
//            view.startAnimation(animationTo);
        }
    }

    private void reset() {
        for (int i = 0; i < 4; i++) {
            mView.findViewById(StringUtils.getRes(mContext,
                    "iv_anim" + (i + 1), "id")).setY(mStartY);
        }
    }

    private Handler mHandler = new Handler();

}


