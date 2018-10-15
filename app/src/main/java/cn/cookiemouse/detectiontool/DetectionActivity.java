package cn.cookiemouse.detectiontool;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.cookiemouse.detectiontool.activity.EditActivity;
import cn.cookiemouse.detectiontool.adapter.DetectionAdapter;
import cn.cookiemouse.detectiontool.base.BaseActivity;
import cn.cookiemouse.detectiontool.data.DetectionData;
import cn.cookiemouse.detectiontool.interfaces.OnBaseListener;

public class DetectionActivity extends BaseActivity {

    private static final String TAG = "DetectionActivity";

    private ListView mListView;
    private List<DetectionData> mDetectionDataList;
    private DetectionAdapter mDetectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection);

        init();
    }

    private void init() {
        this.setTitle("检测");
        this.setRightVisibility(true);
        this.setRightResource(R.drawable.ic_add);

        initView();

        initData();

        initEvent();
    }

    private void initView() {
        mListView = findViewById(R.id.lv_activity_detection);
    }

    private void initData() {
        mDetectionDataList = new ArrayList<>();
        mDetectionDataList.add(new DetectionData("一", ""));
        mDetectionDataList.add(new DetectionData("二", ""));
        mDetectionDataList.add(new DetectionData("三", ""));
        mDetectionDataList.add(new DetectionData("四", ""));
        mDetectionDataList.add(new DetectionData("五", ""));
        mDetectionAdapter = new DetectionAdapter(this, mDetectionDataList);
        mListView.setAdapter(mDetectionAdapter);
    }

    private void initEvent() {
        this.setOnBaseListener(new OnBaseListener() {
            @Override
            public void onLeftClick() {
            }

            @Override
            public void onRightClick() {
                // TODO: 18-10-12 跳转到编辑页面
                toEdit();
            }
        });
    }

    //  跳转到编辑页面
    private void toEdit() {
        Intent intent = new Intent(DetectionActivity.this, EditActivity.class);
        startActivity(intent);
    }
}
