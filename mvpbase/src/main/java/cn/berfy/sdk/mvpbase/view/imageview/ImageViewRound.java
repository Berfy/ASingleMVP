package cn.berfy.sdk.mvpbase.view.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.target.SquaringDrawable;

import cn.berfy.sdk.mvpbase.R;
import cn.berfy.sdk.mvpbase.util.CommonUtil;

/**
 * 圆角图片
 */
public class ImageViewRound extends AppCompatImageView {

    private int roundWidth = 5;
    private int roundHeight = 5;

    public ImageViewRound(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public ImageViewRound(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ImageViewRound(Context context) {
        super(context);
        init(context, null);
    }

    public void setRound(int round) {
        this.roundWidth = round;
        this.roundHeight = round;
    }

    private void init(Context context, AttributeSet attrs) {

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageViewRound);
            roundWidth = a.getDimensionPixelSize(R.styleable.ImageViewRound_roundWidth, roundWidth);
            roundHeight = a.getDimensionPixelSize(R.styleable.ImageViewRound_roundHeight, roundHeight);
            a.recycle();
        } else {
            float density = context.getResources().getDisplayMetrics().density;
            roundWidth = (int) (roundWidth * density);
            roundHeight = (int) (roundHeight * density);
        }

        /*paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        paint2 = new Paint();
        paint2.setXfermode(null);*/
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        // 将Bitmap转为圆形drawable
        RoundImageDrawable drawable = new RoundImageDrawable(bm, roundWidth, roundHeight);
        super.setImageDrawable(drawable);
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        if (drawable == null) return;
        RoundImageDrawable rd = null;
        Bitmap bitmap = null;
        if (drawable instanceof GlideBitmapDrawable) {
            bitmap = ((GlideBitmapDrawable) drawable).getBitmap();

        } else if (drawable instanceof BitmapDrawable)
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        else if (drawable instanceof TransitionDrawable) {
            Drawable dra = ((TransitionDrawable) drawable).getDrawable(1);
            if (dra instanceof GlideBitmapDrawable) {
                bitmap = ((GlideBitmapDrawable) dra).getBitmap();
            } else if (dra instanceof SquaringDrawable) {
                bitmap = ((GlideBitmapDrawable) dra.getCurrent()).getBitmap();
            }
        } else if (drawable instanceof SquaringDrawable) {
            bitmap = ((GlideBitmapDrawable) drawable.getCurrent()).getBitmap();
        }
        if (bitmap == null) {
            Log.i("RoundAngleImageView===>", drawable.getClass().getName() + "");
            super.setImageDrawable(drawable);
            return;
        }
        rd = new RoundImageDrawable(bitmap, roundWidth, roundHeight);

        super.setImageDrawable(rd);
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        Drawable drawable = CommonUtil.getDrawable(getContext(), resId);
        setImageDrawable(drawable);
    }

    public static class RoundImageDrawable extends Drawable {

        private Paint mPaint;
        private Bitmap mBitmap;
        private int roundWidth = 5;
        private int roundHeight = 5;

        private RectF rectF;

        public RoundImageDrawable(Bitmap bitmap, int rx, int ry) {

            mBitmap = bitmap;
            BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,
                    Shader.TileMode.CLAMP);
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setShader(bitmapShader);
            roundHeight = ry;
            roundWidth = rx;
            // Log.i("RoundImageDrawable===>",roundWidth+","+roundHeight);
        }

        @Override
        public void setBounds(int left, int top, int right, int bottom) {
            super.setBounds(left, top, right, bottom);
            rectF = new RectF(left, top, right, bottom);
        }

        @Override
        public void draw(@NonNull Canvas canvas) {
            // Log.i("RoundImageDrawable===>",roundWidth+","+roundHeight);
            canvas.drawRoundRect(rectF, roundWidth, roundHeight, mPaint);
        }

        @Override
        public int getIntrinsicWidth() {
            return mBitmap.getWidth();
        }

        @Override
        public int getIntrinsicHeight() {
            return mBitmap.getHeight();
        }

        @Override
        public void setAlpha(int alpha) {
            mPaint.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
            mPaint.setColorFilter(cf);
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }

    }
}
