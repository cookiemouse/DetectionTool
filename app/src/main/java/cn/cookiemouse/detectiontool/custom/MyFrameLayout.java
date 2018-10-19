package cn.cookiemouse.detectiontool.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class MyFrameLayout extends FrameLayout {

    private static final String TAG = "MyFrameLayout";

    private static final int CLOSE = 0;
    private static final int OPEN = 1;
    private static final int DRAG = 3;

    //  水平可滑到范围
    private int mHorizontalRange;

    private int mViewWidth;
    private int mViewHeight;
    private int mDistanceLeft;

    private Context mContext;
    private ViewDragHelper mViewDragHelper;

    private int mDragStatus = CLOSE;

    private ViewGroup mViewGroupContent;
    private ViewGroup mViewGroupMenu;

    private OnDragListener mOnDragListener;

    public MyFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public MyFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        mViewDragHelper = ViewDragHelper.create(this, mCallback);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mViewGroupMenu = (ViewGroup) getChildAt(0);
        mViewGroupContent = (ViewGroup) getChildAt(1);
        mViewGroupMenu.setClickable(true);
        mViewGroupContent.setClickable(true);
        mViewGroupContent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: -->");
//                open();
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = mViewGroupMenu.getMeasuredWidth();
        mViewHeight = mViewGroupMenu.getMeasuredHeight();
        mHorizontalRange = (int) (mViewWidth * 0.6);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_DOWN:
                mViewDragHelper.cancel(); // 相当于调用 processTouchEvent收到ACTION_CANCEL
                break;
        }

        /**
         * 检查是否可以拦截touch事件
         * 如果onInterceptTouchEvent可以return true 则这里return true
         */
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
//        super.computeScroll();
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        //  返回水平滑动的边界范围
        @Override
        public int getViewHorizontalDragRange(View child) {
//            return super.getViewHorizontalDragRange(child);
            return mHorizontalRange;
        }

        @Override
        public int getViewVerticalDragRange(@NonNull View child) {
//            return super.getViewVerticalDragRange(child);
            return 1000;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == mViewGroupContent) {
                if (left > 0) {
                    left = 0;
                }
                if (left < -mHorizontalRange) {
                    left = -mHorizontalRange;
                }
            }
            return left;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            if (top > mViewHeight) {
//                return super.clampViewPositionVertical(child, top, dy);
            }
            return 0;
        }

        // view在拖动过程坐标发生变化时会调用此方法，包括两个时间段：手动拖动和自动滚动
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
//            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if (changedView == mViewGroupContent) {
                mDistanceLeft = left;
            } else {
                mDistanceLeft = mDistanceLeft + left;
            }
            if (mDistanceLeft > 0) {
                mDistanceLeft = 0;
            } else if (Math.abs(mDistanceLeft) > mHorizontalRange) {
                mDistanceLeft = -mHorizontalRange;
            }
            mViewGroupMenu.layout(0, 0, mViewWidth, mViewHeight);
            mViewGroupContent.layout(mDistanceLeft, 0, mDistanceLeft + mViewWidth,
                    mViewHeight);
            dispatchDragEvent(mDistanceLeft);
        }

        //  当ACTION_UP事件后调用其方法
        //  当releasedChild被释放的时候，xvel和yvel是x和y方向的加速度
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (xvel < 0) {
                // 加速度向
                open();
            } else if (xvel > 0) {
                // 加速度向左
                close();
            } else if (releasedChild == mViewGroupContent
                    && mDistanceLeft < -mHorizontalRange * 0.5) {
                // 如果释放时，手指在内容区且内容区离左边的距离是range * 0.3
                Log.i(TAG, "onViewReleased: mDistanceLeft-->" + mDistanceLeft);
                open();
            } else if (releasedChild == mViewGroupMenu
                    && mDistanceLeft < -mHorizontalRange * 0.1) {
                // 如果释放时，手指在菜单区且内容区离左边的距离是range * 0.7
                open();
            } else {
                close();
            }
        }
    };

    private void dispatchDragEvent(int mainLeft) {
        float percent = Math.abs(mainLeft) / (float) mHorizontalRange;

        int lastStatus = mDragStatus;
        if (mOnDragListener == null)
            return;
        mOnDragListener.onDrag(percent);
        if (lastStatus != getStatus() && mDragStatus == CLOSE) {
            mOnDragListener.onClose();
        } else if (lastStatus != getStatus() && mDragStatus == OPEN) {
            mOnDragListener.onOpen();
        }
    }

    //  获取状态
    public int getStatus() {
        if (mDistanceLeft == 0) {
            mDragStatus = CLOSE;
        } else if (mDistanceLeft == mHorizontalRange) {
            mDragStatus = OPEN;
        } else {
            mDragStatus = DRAG;
        }

        return mDragStatus;
    }

    public interface OnDragListener {
        void onOpen();

        void onClose();

        void onDrag(float percent);
    }

    private void open() {
        Log.i(TAG, "open: ");
        if (mViewDragHelper.smoothSlideViewTo(mViewGroupContent,
                -mHorizontalRange, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private void close() {
        if (mViewDragHelper.smoothSlideViewTo(mViewGroupContent, 0, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
}
