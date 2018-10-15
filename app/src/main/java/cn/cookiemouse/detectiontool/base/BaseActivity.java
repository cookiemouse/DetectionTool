package cn.cookiemouse.detectiontool.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.cookiemouse.detectiontool.R;
import cn.cookiemouse.detectiontool.interfaces.OnBaseListener;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    private FrameLayout mFrameLayout;

    private ImageView mImageViewLeft, mImageViewRight;
    private TextView mTextViewTitle;

    private View mViewStatus;

    private OnBaseListener mOnBaseListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);

        setStatusTransparent();

        try {
            getSupportActionBar().hide();
        } catch (NullPointerException e) {
            Log.e(TAG, "onCreate: ", e);
        }

        init();
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = LayoutInflater.from(this).inflate(layoutResID, null);
        mFrameLayout.addView(view);
//        super.setContentView(layoutResID);
    }

    @Override
    public void setContentView(View view) {
        mFrameLayout.addView(view);
//        super.setContentView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mFrameLayout.addView(view, params);
//        super.setContentView(view, params);
    }

    private void init() {
        initView();

        initEvent();
    }

    private void initView() {
        mFrameLayout = findViewById(R.id.fl_activity_base_content);
        mTextViewTitle = findViewById(R.id.tv_activity_base_title);
        mImageViewLeft = findViewById(R.id.iv_activity_base_left);
        mImageViewRight = findViewById(R.id.iv_activity_base_right);

        mViewStatus = findViewById(R.id.view_activity_base_status);

        int height = getStatusHeight();
        Log.i(TAG, "onCreate: height-->" + height);
        if (height > 0) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            mViewStatus.setLayoutParams(layoutParams);
        }
    }

    private void initEvent() {
        mImageViewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnBaseListener) {
                    BaseActivity.this.finish();
                    return;
                }
                mOnBaseListener.onLeftClick();
            }
        });
        mImageViewRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnBaseListener) {
                    return;
                }
                mOnBaseListener.onRightClick();
            }
        });
    }

    public void setOnBaseListener(OnBaseListener listener) {
        this.mOnBaseListener = listener;
    }

    public void setLeftVisibility(boolean visibility) {
        if (visibility) {
            mImageViewLeft.setVisibility(View.VISIBLE);
        } else {
            mImageViewLeft.setVisibility(View.GONE);
        }
    }

    public void setLeftResource(int resourceId) {
        mImageViewLeft.setImageResource(resourceId);
    }

    public void setRightVisibility(boolean visibility) {
        if (visibility) {
            mImageViewRight.setVisibility(View.VISIBLE);
        } else {
            mImageViewRight.setVisibility(View.GONE);
        }
    }

    public void setRightResource(int resourceId) {
        mImageViewRight.setImageResource(resourceId);
    }

    public void setTitle(int resid) {
        mTextViewTitle.setText(getString(resid));
    }

    public void setTitle(String title) {
        mTextViewTitle.setText(title);
    }

    /**
     * 设备状态栏透明
     */
    public void setStatusTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    private int getStatusHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
