package cn.cookiemouse.detectiontool.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by cookiemouse on 2017/7/13.
 */

public class TimerU {
    private static final String TAG = "TimerU";

    private static final int DELAY = 1000;

    private static final int FLAG_START = 0x01;
    private static final int FLAG_END = 0x02;
    private static final int FLAG_DELAY = 0x03;

    private static final int MIN = 0;
    private static final int INIT_DELAY = -1;

    private int mIntMax;
    private int mIntNow;

    private long mDelay = INIT_DELAY;

    private boolean isStart = false;

    private TimerUHandler mHandler;

    private OnTickListener mOnTickListener;

    public TimerU(int mIntMax) {
        this.mIntMax = mIntMax;
        this.mIntNow = mIntMax;
        mHandler = new TimerUHandler();
    }

    public TimerU(long delay) {
        this.mDelay = delay;
        mHandler = new TimerUHandler();
    }

    public void start() {
        if (INIT_DELAY != mDelay) {
            isStart = true;
            mHandler.sendEmptyMessageDelayed(FLAG_DELAY, mDelay);
            return;
        }
        if (isStart) {
            return;
        }
        mIntNow = mIntMax;
        mHandler.sendEmptyMessage(FLAG_START);
        isStart = true;
    }

    public void cancle() {
        if (isStart) {
            mHandler.removeMessages(FLAG_DELAY);
            mHandler.removeMessages(FLAG_START);
            mIntNow = mIntMax;
            isStart = false;

            if (null == mOnTickListener) {
                return;
            }
            mOnTickListener.onCancel();
        }
    }

    private class TimerUHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null == mOnTickListener) {
                return;
            }
            switch (msg.what) {
                case FLAG_START: {
                    mOnTickListener.onTick(mIntNow);
                    if (mIntNow > MIN) {
                        sendEmptyMessageDelayed(FLAG_START, DELAY);
                        mIntNow--;
                        break;
                    }
                    sendEmptyMessage(FLAG_END);
                    break;
                }
                case FLAG_END: {
                    mOnTickListener.onEnd();
                    mIntNow = mIntMax;
                    isStart = false;
                    break;
                }
                case FLAG_DELAY: {
                    mOnTickListener.onEnd();
                    isStart = false;
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: ");
                }
            }
        }
    }

    public interface OnTickListener {
        void onTick(int time);

        void onEnd();

        void onCancel();
    }

    public void setOnTickListener(OnTickListener listener) {
        this.mOnTickListener = listener;
    }
}
