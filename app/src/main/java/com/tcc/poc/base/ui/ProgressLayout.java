package com.tcc.poc.base.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;


import com.tcc.poc.R;

import java.util.ArrayList;
import java.util.List;

public class ProgressLayout extends RelativeLayout {

    public static final int INFINITE = 0;

    public static final int INTERP_LINEAR = 0;
    public static final int INTERP_ACCELERATE = 1;
    public static final int INTERP_DECELERATE = 2;
    public static final int INTERP_ACCELERATE_DECELERATE = 3;

    private static final int DEFAULT_COUNT = 12;
    private static final int DEFAULT_DURATION = 1000;
    private static final int DEFAULT_REPEAT = INFINITE;
    private static final boolean DEFAULT_START_FROM_SCRATCH = true;
    private static final int DEFAULT_INTERPOLATOR = INTERP_DECELERATE;

    private int mCount;
    private int mDuration;
    private int mRepeat;
    private boolean mStartFromScratch;
    private int mColor;
    private int mInterpolator;
    private float mStartScale = 0;
    private float mStartAlpha = 1f;

    private final List<View> mViews = new ArrayList<>();
    private AnimatorSet mAnimatorSet;
    private Paint mPaint;
    private float mRadius;
    private float mCenterX;
    private float mCenterY;
    private boolean mIsStarted;

    /**
     * Simple constructor to use when creating a view from serverType.
     *
     * @param context The Context the view is running in, through which it can access the current
     *                theme, resources, etc.
     */
    public ProgressLayout(Context context) {
        this(context, null, 0);
    }

    /**
     * Constructor that is called when inflating a view from XML.
     *
     * @param context The Context the view is running in, through which it can access the current
     *                theme, resources, etc.
     * @param attrs The attributes of the XML tag that is inflating the view.
     */
    public ProgressLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Perform inflation from XML and apply a class-specific base style from a theme attribute.
     *
     * @param context The Context the view is running in, through which it can access the current
     *                theme, resources, etc.
     * @param attrs The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a reference to a style
     *                     resource that supplies default values for the view. Can be 0 to not look
     *                     for defaults.
     */
    public ProgressLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mCount = DEFAULT_COUNT;
        mDuration = DEFAULT_DURATION;
        mRepeat = DEFAULT_REPEAT;
        mStartFromScratch = DEFAULT_START_FROM_SCRATCH;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mColor = context.getColor(R.color.black);
        }
        mInterpolator = DEFAULT_INTERPOLATOR;

        // create paint
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mColor);

        // create views
        build();
    }

    private Paint createPaint(int color) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);

        return paint;
    }

    /**
     * Start pulse animation.
     */
    public synchronized void start() {
        if (mAnimatorSet == null || mIsStarted) {
            return;
        }

        mAnimatorSet.start();

        if (!mStartFromScratch) {
            ArrayList<Animator> animators = mAnimatorSet.getChildAnimations();
            for (Animator animator : animators) {
                ObjectAnimator objectAnimator = (ObjectAnimator) animator;

                long delay = objectAnimator.getStartDelay();
                objectAnimator.setStartDelay(0);
                objectAnimator.setCurrentPlayTime(mDuration - delay);
            }
        }
    }

    /**
     * Stop pulse animation.
     */
    public synchronized void stop() {
        if (mAnimatorSet == null || !mIsStarted) {
            return;
        }

        mAnimatorSet.end();
    }

    public synchronized boolean isStarted() {
        return (mAnimatorSet != null && mIsStarted);
    }

    /**
     * Get number of pulses.
     *
     * @return Number of pulses
     */
    public int getCount() {
        return mCount;
    }

    /**
     * Get pulse duration.
     *
     * @return Duration of single pulse in milliseconds
     */
    public int getDuration() {
        return mDuration;
    }

    /**
     * Set number of pulses.
     *
     * @param count Number of pulses
     */
    public void setCount(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Count cannot be negative");
        }

        if (count != mCount) {
            mCount = count;
            reset();
            invalidate();
        }
    }

    public void setStartScale(float scale) {
        if (scale != mStartScale) {
            mStartScale = scale;
            reset();
            invalidate();
        }
    }

    public float getStartScale() {
        return mStartScale;
    }

    /**
     * Set single pulse duration.
     *
     * @param millis Pulse duration in milliseconds
     */
    public void setDuration(int millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("Duration cannot be negative");
        }

        if (millis != mDuration) {
            mDuration = millis;
            reset();
            invalidate();
        }
    }

    /**
     * Gets the current color of the pulse effect in integer
     * Defaults to Color.rgb(0, 116, 193);
     * @return an integer representation of color
     */
    public int getColor() {
        return mColor;
    }

    /**
     * Sets the current color of the pulse effect in integer
     * Takes effect immediately
     * Usage: Color.parseColor("<hex-value>") or getResources().getColor(R.color.colorAccent)
     * @param color : an integer representation of color
     */
    public void setColor(int color) {
        if (color != mColor) {
            this.mColor = color;

            if (mPaint != null) {
                mPaint.setColor(color);
            }
        }
    }

    /**
     * Get current interpolator type used for animating.
     *
     * @return Interpolator type as int
     */
    public int getInterpolator() {
        return mInterpolator;
    }

    /**
     * Set current interpolator used for animating.
     *
     * @param type Interpolator type as int
     */
    public void setInterpolator(int type) {
        if (type != mInterpolator) {
            mInterpolator = type;
            reset();
            invalidate();
        }
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();

        mCenterX = width * 0.5f;
        mCenterY = height * 0.5f;
        mRadius = Math.min(width, height) * 0.5f;

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * Remove all views and animators.
     */
    private void clear() {
        // remove animators
        stop();

        // remove old views
        for (View view : mViews) {
            removeView(view);
        }
        mViews.clear();
    }

    /**
     * Build pulse views and animators.
     */
    private void build() {
        // create views and animators
        LayoutParams layoutParams = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);

        int repeatCount = (mRepeat == INFINITE) ? ObjectAnimator.INFINITE : mRepeat;

        List<Animator> animators = new ArrayList<>();
        for (int index = 0; index < mCount; index++) {
            // setup view color
            Paint paint;
            if (index == mCount-1) {
                paint = createPaint(Color.argb(0, 0, 0, 0));
            } else {
                paint = createPaint(mColor);
            }
            float startScale = index*(1 - mStartScale)/mCount;
            float startAlpha = index*(1 - mStartAlpha)/mCount;
            if (startScale == 0) startScale = mStartScale;
            // setup view
            PulseView pulseView = new PulseView(getContext(), paint);
            pulseView.setScaleX(startScale);
//            pulseView.setScaleX(mStartScale);
            pulseView.setScaleY(startScale);
//            pulseView.setScaleY(mStartScale);
            pulseView.setAlpha(startAlpha);
//            pulseView.setAlpha(mStartAlpha);

            addView(pulseView, index, layoutParams);
            mViews.add(pulseView);

            long delay = index * mDuration / mCount;

            // setup animators
            ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(pulseView, "ScaleX", mStartScale, 1f);
            scaleXAnimator.setRepeatCount(repeatCount);
            scaleXAnimator.setRepeatMode(ObjectAnimator.RESTART);
            scaleXAnimator.setStartDelay(delay);
            animators.add(scaleXAnimator);

            ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(pulseView, "ScaleY", mStartScale, 1f);
            scaleYAnimator.setRepeatCount(repeatCount);
            scaleYAnimator.setRepeatMode(ObjectAnimator.RESTART);
            scaleYAnimator.setStartDelay(delay);
            animators.add(scaleYAnimator);

            ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(pulseView, "Alpha", mStartAlpha, 0f);
            alphaAnimator.setRepeatCount(repeatCount);
            alphaAnimator.setRepeatMode(ObjectAnimator.RESTART);
            alphaAnimator.setStartDelay(delay);
            animators.add(alphaAnimator);
        }

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(animators);
        mAnimatorSet.setInterpolator(createInterpolator(mInterpolator));
        mAnimatorSet.setDuration(mDuration);
        mAnimatorSet.addListener(mAnimatorListener);
    }

    /**
     * Reset views and animations.
     */
    private void reset() {
        boolean isStarted = isStarted();

        clear();
        build();

        if (isStarted) {
            start();
        }
    }

    /**
     * Create interpolator from type.
     *
     * @param type Interpolator type as int
     * @return Interpolator object of type
     */
    private static Interpolator createInterpolator(int type) {
        switch (type) {
            case INTERP_ACCELERATE:
                return new AccelerateInterpolator();
            case INTERP_DECELERATE:
                return new DecelerateInterpolator();
            case INTERP_ACCELERATE_DECELERATE:
                return new AccelerateDecelerateInterpolator();
            default:
                return new LinearInterpolator();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (mAnimatorSet != null) {
            mAnimatorSet.cancel();
            mAnimatorSet = null;
        }
    }

    private class PulseView extends View {

        private Paint paint;

        public PulseView(Context context, Paint paint) {
            super(context);
            this.paint = paint;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawCircle(mCenterX, mCenterY, mRadius, paint);
        }

    }

    private final Animator.AnimatorListener mAnimatorListener = new Animator.AnimatorListener() {

        @Override
        public void onAnimationStart(Animator animator) {
            mIsStarted = true;
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            mIsStarted = false;
        }

        @Override
        public void onAnimationCancel(Animator animator) {
            mIsStarted = false;
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
        }

    };

}