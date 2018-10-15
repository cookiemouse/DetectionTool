package cn.cookiemouse.detectiontool;

import android.os.Bundle;

import cn.cookiemouse.detectiontool.base.BaseActivity;
import cn.cookiemouse.detectiontool.interfaces.OnBaseListener;

public class DetectionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection);

        init();
    }

    private void init() {
        this.setRightVisibility(true);
        this.setRightResource(R.drawable.ic_add);

        initView();

        initEvent();
    }

    private void initView() {
    }

    private void initEvent() {
        this.setOnBaseListener(new OnBaseListener() {
            @Override
            public void onLeftClick() {
            }

            @Override
            public void onRightClick() {
                // TODO: 18-10-12 跳转到编辑页面
            }
        });
    }
}
