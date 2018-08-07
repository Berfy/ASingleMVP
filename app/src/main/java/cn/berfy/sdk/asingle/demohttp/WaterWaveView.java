package cn.berfy.sdk.asingle.demohttp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;

import cn.berfy.sdk.asingle.R;


public class WaterWaveView extends View {

    private Context mContext;
    private final Paint mPaint1 = new Paint();//远处
    private final Paint mPaint2 = new Paint();//近处
    private boolean mIsRun;
    private final int FPS = 80;// fps
    private float mAngle1 = 0;//远处远处波浪角度1
    private float mAngle2 = 180;//近处波浪角度2
    private float mWaterSin = 30;// 睡眠波浪振幅 数值越大波浪越大
    private float mLevel = 0.4f;// 水面高度
    private float mWaterSpeed = 0.0002f;// 水滴速度
    private float mSpeed1 = 1.5f;// 远处水浪速度 越大越快
    private float mSpeed2 = 3f;// 近处水浪速度 越大越快
    private Thread mRefreshThread1;
    private Thread mRefreshThread2;
    private Thread mRefreshThread3;
    private Thread mAddThread;
    private PaintFlagsDrawFilter mDrawFilter;
    private final int mMaxWaterCount = 50;
    private Random mRandom = new Random();
    private List<Water> mWaters = new ArrayList<>();
    private List<Water> mAddWaters = new ArrayList<>();
    private boolean mIsScrollToDown = true;
    private int mWidth, mHeight;

    public WaterWaveView(Context paramContext) {
        super(paramContext);
        mContext = paramContext;
        init();
    }

    public WaterWaveView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        mContext = paramContext;
        init();
    }

    private void init() {
        mDrawFilter = new PaintFlagsDrawFilter(Paint.ANTI_ALIAS_FLAG,
                Paint.DITHER_FLAG);
        mPaint1.setAlpha(150);
        mPaint1.setAntiAlias(true);
        mPaint1.setColor(Color.rgb(89, 176, 200));
        mPaint2.setAlpha(150);
        mPaint2.setAntiAlias(true);
        mPaint2.setColor(Color.rgb(89, 196, 231));
    }

    public void startWave() {
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        if (null != mWaters) {
            mWaters.clear();
        }
        if (!mIsRun) {
            mIsRun = true;
            mRefreshThread1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    while (mIsRun) {
                        mAngle1 += mSpeed1;
                        mAngle2 += mSpeed2;
                        if (mAngle1 == 360) {
                            mAngle1 = 0;
                        }
                        if (mAngle2 == 360) {
                            mAngle2 = 0;
                        }
                        try {
                            if (mWidth > 0 && mHeight > 0) {
                                for (int i = 0; i < mWaters.size() / 3; i++) {
                                    Water water = mWaters.get(i);
                                    float trueHeight = mHeight * (1 - mLevel);
                                    if (mIsScrollToDown) {
                                        water.size += 0.0005;
                                    } else {
                                        water.size += 0.0005;
                                    }
                                    if (mIsScrollToDown && water.y > trueHeight + 50) {
                                        water.x = mRandom.nextInt(mWidth);
                                        water.y = mRandom.nextInt(mHeight / 4);
                                        water.size = 0.01f;
                                        water.rawTime = System.currentTimeMillis();
                                    } else if (!mIsScrollToDown && water.y < -50) {
                                        water.x = mRandom.nextInt(mWidth);
                                        water.y = trueHeight + 50 + mRandom.nextInt((int) (mHeight * mLevel));
                                        water.size = 0.01f;
                                        water.rawTime = System.currentTimeMillis();
                                    }
                                    double moveTime = (System.currentTimeMillis() * 0.1 - water.rawTime * 0.1);
                                    if (mIsScrollToDown) {
                                        water.y += (float) ((moveTime + moveTime * 3) / 200);
                                    } else {
                                        water.y -= (float) ((moveTime + moveTime * 3) / 200);
                                    }
//                                    LogF.d("水滴刷新", "===高度" + mHeight + "   " + water.x + "," + water.y + "  " + water.size);
                                }
                            }
                            mHandler.sendEmptyMessage(0);
                            try {
                                Thread.sleep(1000 / FPS);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mHandler.sendEmptyMessage(0);
                            try {
                                Thread.sleep(1L);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            });
            mRefreshThread1.start();

            mRefreshThread2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    while (mIsRun) {
                        try {
                            if (mWidth > 0 && mHeight > 0) {
                                for (int i = mWaters.size() / 3; i < mWaters.size() / 3 * 2; i++) {
                                    Water water = mWaters.get(i);
                                    float trueHeight = mHeight * (1 - mLevel);
                                    if (mIsScrollToDown) {
                                        water.size += 0.0004;
                                    } else {
                                        water.size += 0.0004;
                                    }
                                    if (mIsScrollToDown && water.y > trueHeight + 50) {
                                        water.x = mRandom.nextInt(mWidth);
                                        water.y = mRandom.nextInt(mHeight / 4);
                                        water.size = 0.01f;
                                        water.rawTime = System.currentTimeMillis();
                                    } else if (!mIsScrollToDown && water.y < -50) {
                                        water.x = mRandom.nextInt(mWidth);
                                        water.y = trueHeight + 50 + mRandom.nextInt((int) (mHeight * mLevel));
                                        water.size = 0.01f;
                                        water.rawTime = System.currentTimeMillis();
                                    }
                                    double moveTime = (System.currentTimeMillis() * 0.1 - water.rawTime * 0.1);
                                    if (mIsScrollToDown) {
                                        water.y += (float) ((moveTime + moveTime * 3) / 600);
                                    } else {
                                        water.y -= (float) ((moveTime + moveTime * 3) / 600);
                                    }
//                                    LogF.d("水滴刷新", "===高度" + mHeight + "   " + water.x + "," + water.y + "  " + water.size);
                                }
                            }
                            mHandler.sendEmptyMessage(0);
                            try {
                                Thread.sleep(1000 / FPS);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mHandler.sendEmptyMessage(0);
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            });
            mRefreshThread2.start();

            mRefreshThread3 = new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    while (mIsRun) {
                        try {
                            if (mWidth > 0 && mHeight > 0) {
                                for (int i = mWaters.size() / 3 * 2; i < mWaters.size(); i++) {
                                    Water water = mWaters.get(i);
                                    float trueHeight = mHeight * (1 - mLevel);
                                    if (mIsScrollToDown) {
                                        water.size += 0.0002;
                                    } else {
                                        water.size += 0.0002;
                                    }
                                    if (mIsScrollToDown && water.y > trueHeight + 50) {
                                        water.x = mRandom.nextInt(mWidth);
                                        water.y = mRandom.nextInt(mHeight / 4);
                                        water.size = 0.01f;
                                        water.rawTime = System.currentTimeMillis();
                                    } else if (!mIsScrollToDown && water.y < -50) {
                                        water.x = mRandom.nextInt(mWidth);
                                        water.y = trueHeight + 50 + mRandom.nextInt((int) (mHeight * mLevel));
                                        water.size = 0.01f;
                                        water.rawTime = System.currentTimeMillis();
                                    }
                                    double moveTime = (System.currentTimeMillis() * 0.1 - water.rawTime * 0.1);
                                    if (mIsScrollToDown) {
                                        water.y += (float) ((moveTime + moveTime * 3) / 800);
                                    } else {
                                        water.y -= (float) ((moveTime + moveTime * 3) / 800);
                                    }
//                                    LogF.d("水滴刷新", "===高度" + mHeight + "   " + water.x + "," + water.y + "  " + water.size);
                                }
                            }
                            mHandler.sendEmptyMessage(0);
                            try {
                                Thread.sleep(1000 / FPS);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mHandler.sendEmptyMessage(0);
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            });
            mRefreshThread3.start();
            mAddThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    while (mIsRun && mWaters.size() < mMaxWaterCount) {
                        if (mWidth > 0 && mHeight > 0) {
                            if (mWaters.size() < mMaxWaterCount) {
                                Water water = new Water();
                                water.water = BitmapFactory.decodeResource(mContext.getResources(),
                                        R.mipmap.water);
                                water.x = mRandom.nextInt(mWidth);
                                water.y = mRandom.nextInt(mHeight / 4);
                                water.rawTime = System.currentTimeMillis() - (long) (3000 * ((water.y * 0.1) / (mHeight * 0.1)));
                                mAddWaters.add(water);
//                                LogF.d("水滴增加", " " + water.x + "," + water.y + "  " + water.size);
//                                    mHandler.sendEmptyMessage(0);
//                                    postInvalidate();
                            }
                        }
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            mAddThread.start();
        }
//        mHandler.sendEmptyMessageDelayed(1, 10000);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    invalidate();
                    break;
                case 1:
//                    mIsScrollToDown = !mIsScrollToDown;
//                    mHandler.sendEmptyMessageDelayed(1, 10000);
                    break;
            }
        }
    };

    @Override
    public void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        canvas.setDrawFilter(mDrawFilter);
        canvas.drawColor(Color.WHITE);
        mWidth = getWidth();
        mHeight = getHeight();
        try {
            if (null != mWaters) {
                if (mAddWaters.size() > 0) {
                    mWaters.addAll(mAddWaters);
                    mAddWaters.clear();
                }
                for (Water water : mWaters) {
                    Matrix matrix = new Matrix();
                    matrix.postScale(water.size, water.size);
                    matrix.postTranslate(water.x, water.y);
                    canvas.drawBitmap(water.water, matrix, mPaint1);
//                    LogF.d("水滴", "==="
//                            + water.x + "," + water.y + "  " + water.size);
                }
            }
        } catch (ConcurrentModificationException e) {
            e.printStackTrace();
        }

        double lineX1 = 0;
        double lineY1 = 0;
        double lineX2 = 0;
        double lineY2 = 0;
        for (int i = 0; i < mWidth; i++) {
            lineX1 = i;
            lineX2 = i;
            if (mIsRun) {
                lineY1 = mWaterSin * Math.sin((i / 4 + mAngle1) * Math.PI / 180) + 50;//远处
                lineY2 = mWaterSin * Math.sin((i / 4 + mAngle2) * Math.PI / 180) + 50;//近处
            } else {
                lineY1 = 50;
                lineY2 = 50;
            }
            canvas.drawLine((int) lineX1,
                    (int) (lineY1 + mHeight * (1 - mLevel)), (int) lineX1,
                    mHeight, mPaint1);
            canvas.drawLine((int) lineX2,
                    (int) (lineY2 + mHeight * (1 - mLevel)), (int) lineX2,
                    mHeight, mPaint2);
        }
    }

    public void stop() {
        setLayerType(View.LAYER_TYPE_NONE, null);
        for (Water water : mWaters) {
            water.water.recycle();
        }
        mWaters.clear();
        for (Water water : mAddWaters) {
            water.water.recycle();
        }
        mAddWaters.clear();
        mIsRun = false;
        if (null != mRefreshThread1) {
            mRefreshThread1.interrupt();
            mRefreshThread1 = null;
        }
        if (null != mRefreshThread2) {
            mRefreshThread2.interrupt();
            mRefreshThread2 = null;
        }
        if (null != mRefreshThread3) {
            mRefreshThread3.interrupt();
            mRefreshThread3 = null;
        }
        if (null != mAddThread) {
            mAddThread.interrupt();
            mAddThread = null;
        }
        System.gc();
    }

    public boolean isRunning() {
        return mIsRun;
    }

    public static class Water {

        public Bitmap water;
        public float x;
        public float y;
        public float size = 0.01f;// 水滴尺寸 0-1
        public long rawTime;
    }


}