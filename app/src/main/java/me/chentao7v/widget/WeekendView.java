package me.chentao7v.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;

/**
 * @author chentao
 */
public class WeekendView extends View {

    private static String TAG = "WeekendView";

    private boolean isDebug = false;

    private Paint mPaint;

    private Paint mHelpPaint;

    /**
     * 滑动控制器
     */
    private OverScroller mScroller;

    /**
     * 速度追踪
     */
    private VelocityTracker mVelocityTracker;

    /**
     * 滑动的左右边界
     */
    private int mScrollLeftBorder, mScrollRightBorder;

    /**
     * 滑动的最小与最大速度
     */
    private int mMinVelocity, mMaxVelocity;

    /**
     * 控件的宽度/一半宽度/高度
     */
    private int mWidth, mHalfWidth, mHeight;

    /**
     * 圆的最小/最大半径
     */
    private int mMinRadius, mMaxRadius;

    /**
     * 控件的最小高度
     */
    private int mMinHeight;

    /**
     * 左右辅助线的初始位置
     */
    private int mLeftHelperLineInit, mRightHelperLineInit;

    /**
     * 滑动时左/右/中辅助线的位置
     */
    private int mLeftLine, mRightLine, mMiddleLine;

    /**
     * 两个圆的间距
     */
    private int mSpace;

    /**
     * 7个点圆心的x坐标
     */
    private int[] mInitXCoor = new int[7];

    private float mDownX;

    /**
     * 文字绘制时 y 轴的偏移量
     */
    private float mTextOffset;

    /**
     * 当前选中的position
     */
    private int mCurrentPosition;

    public WeekendView(Context context) {
        this(context, null);
    }

    public WeekendView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.parseColor("#ff0071"));

        mHelpPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHelpPaint.setStrokeWidth(1);
        mHelpPaint.setColor(Color.RED);
        mHelpPaint.setStyle(Paint.Style.FILL);
        mHelpPaint.setTextSize(60);
        mHelpPaint.setTextAlign(Paint.Align.CENTER);

        mScroller = new OverScroller(context);

        mMinVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
        mMaxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        //		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //
        //		if (widthMode != MeasureSpec.AT_MOST) {
        //			MeasureSpec.makeMeasureSpec(ViewGroup.LayoutParams.MATCH_PARENT,)
        //		}

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;

        mHalfWidth = (int) (mWidth * 0.5f + 0.5f);

        mMaxRadius = (int) (mWidth * 0.18f);
        mMinRadius = (int) (mMaxRadius * 0.65f);

        mSpace = (int) (mMinRadius * 0.20f);

        mLeftHelperLineInit = (int) (mHalfWidth * 0.5f);
        mRightHelperLineInit = mLeftHelperLineInit + mHalfWidth;

        int increment = 2 * mMaxRadius + mSpace;

        for (int i = 0; i < 7; i++) {
            if (i == 0) {
                mInitXCoor[i] = mHalfWidth;
            } else {
                mInitXCoor[i] = mInitXCoor[i - 1] + increment;
            }
        }

        // 边界控制
        mScrollLeftBorder = 0;
        mScrollRightBorder = mInitXCoor[6] - mHalfWidth;

        // 文字绘制居中
        Paint.FontMetrics fontMetrics = mHelpPaint.getFontMetrics();
        mTextOffset = (fontMetrics.ascent + fontMetrics.descent) * 0.5f;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // 事件给到VelocityTracker以最终当前的滑动速度
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 取消之前的动画
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mDownX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float currentX = event.getX();
                float dx = currentX - mDownX;
                // 增量移动
                scrollBy((int) -dx, 0);
                mDownX = currentX;
                break;
            case MotionEvent.ACTION_UP:
                // UP时，计算速度
                mVelocityTracker.computeCurrentVelocity(1000, mMaxVelocity);
                float velocityX = mVelocityTracker.getXVelocity();

                if (Math.abs(velocityX) > mMinVelocity) {
                    // 根据速度滑动
                    fling((int) -velocityX);
                } else {
                    // 自动滚动到距离中心最近的圆的圆心
                    autoScrollToCenter();
                }

                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
        }

        return true;
    }

    /**
     * 每次停止滑动后都自动偏移到中心点
     */
    private void autoScrollToCenter() {
        int[] toCenterMinDistance = getToCenterMinDistance();

        int position = toCenterMinDistance[0];
        int distance = toCenterMinDistance[1];

        //		Log.d(TAG, "position : " + position + " , distance : " + distance);

        // 自动回滚，圆心与中心线对其
        startScroll(distance);
    }

    /**
     * 开始滚动
     *
     * @param dx x向滚动的距离
     */
    private void startScroll(int dx) {
        mScroller.startScroll(getScrollX(), 0, dx, 0);
        invalidate();
    }

    /**
     * 选中指定位置的圆
     */
    public void selectPosition(int position) {
        if (position < 0 || position >= mInitXCoor.length) {
            return;
        }

        if (mCurrentPosition == position) {
            return;
        }

        mCurrentPosition = position;

        // 选中某一个,就是将中线移动到圆心位置
        int xI = mInitXCoor[position];

        int distance = xI - mMiddleLine;

        Log.d(TAG, "dx : " + distance);

        startScroll(distance);
    }

    /**
     * 根据UP时的速度进行滑动
     */
    private void fling(int velocityX) {
        mScroller.fling(getScrollX(), 0, velocityX, 0, mScrollLeftBorder, mScrollRightBorder, 0, 0);
        invalidate();
    }

    @Override
    public void scrollTo(int x, int y) {

        // 滑动的边界控制
        if (x < mScrollLeftBorder) {
            x = mScrollLeftBorder;
        }
        if (x > mScrollRightBorder) {
            x = mScrollRightBorder;
        }

        super.scrollTo(x, y);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {

            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();

            if (!mScroller.computeScrollOffset()) {
                // 不能再滑动时, 自动回滚，圆心与中心线对其
                autoScrollToCenter();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();

        // Scroller是屏幕的移动
        // 滑动控制
        int scrollX = getScrollX();

        mLeftLine = mLeftHelperLineInit + scrollX;
        mRightLine = mRightHelperLineInit + scrollX;
        mMiddleLine = mHalfWidth + scrollX;

        // 最绘制区域进行裁剪，提升性能
        canvas.clipRect(scrollX, 0, mWidth + scrollX, mHeight);

        if (isDebug) {
            canvas.drawLine(mLeftLine, 0, mLeftLine, mHeight, mHelpPaint);
            canvas.drawLine(mRightLine, 0, mRightLine, mHeight, mHelpPaint);
            canvas.drawLine(mMiddleLine, 0, mMiddleLine, mHeight, mHelpPaint);
            canvas.drawLine(scrollX, mHeight * 0.5f, mWidth + scrollX, mHeight * 0.5f, mHelpPaint);
        }

        // 整个绘制过程，所有圆的坐标都还是初始错标
        // 通过"移动屏幕与三条辅助线"来达到动态改变圆的半径而"模拟出视觉上是画面移动的感觉"。
        for (int i = 0; i < 7; i++) {
            int currentX = getXCoor(i);
            float currentRadius = getCurrentRadius(i);
            // 文字
            canvas.drawText((i + 1) + "", currentX, mHeight * 0.5f - mTextOffset, mHelpPaint);
            // 圆：变化的只是圆的半径。移动的是手机
            canvas.drawCircle(currentX, mHeight / 2, currentRadius, mPaint);
        }

        canvas.restore();

    }

    /**
     * 获取距离中心最近的点,以及该点距离中心的距离
     *
     * @return 距离中心线最近的点；以及该点距离中心的距离。
     */
    private int[] getToCenterMinDistance() {

        int[] result = new int[2];
        int[] temp = new int[mInitXCoor.length];

        // 先找到距离中间较近的多个点
        for (int i = 0; i < mInitXCoor.length; i++) {

            int xI = mInitXCoor[i];
            // 距离中心线的距离有方向
            if (xI > mLeftLine && xI < mMiddleLine) {
                temp[i] = -(mMiddleLine - xI);
            } else if (xI > mMiddleLine && xI < mRightLine) {
                temp[i] = xI - mMiddleLine;
            }
        }

        // 然后再找到最近的点
        int minDistance = mHalfWidth;
        for (int i = 0; i < temp.length; i++) {
            int tempI = temp[i];
            if (tempI != 0) {
                if (Math.abs(tempI) < minDistance) {
                    minDistance = Math.abs(tempI);
                    result[0] = i;
                    result[1] = tempI;
                }
            }
        }
        return result;
    }

    /**
     * 返回指定位置圆的坐标
     */
    private int getXCoor(int i) {
        return mInitXCoor[i];
    }

    /**
     * 返回指定位置圆的半径
     */
    private float getCurrentRadius(int i) {
        int xI = getXCoor(i);
        // 半径随着辅助线位置的变化而变化
        if (xI <= mLeftLine) {
            return mMinRadius;
        } else if (xI > mLeftLine && xI <= mMiddleLine) {
            return mMinRadius +
                (xI - mLeftLine) * 1.0f / mLeftHelperLineInit * (mMaxRadius - mMinRadius);
        } else if (xI > mMiddleLine && xI <= mRightLine) {
            return mMaxRadius -
                (xI - mMiddleLine) * 1.0f / mLeftHelperLineInit * (mMaxRadius - mMinRadius);
        } else {
            return mMinRadius;
        }
    }
}
