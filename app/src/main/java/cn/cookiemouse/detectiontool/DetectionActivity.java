package cn.cookiemouse.detectiontool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.ListView;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.cookiemouse.detectiontool.activity.EditActivity;
import cn.cookiemouse.detectiontool.adapter.DetectionAdapter;
import cn.cookiemouse.detectiontool.base.BaseActivity;
import cn.cookiemouse.detectiontool.data.DetectionData;
import cn.cookiemouse.detectiontool.data.ParameterData;
import cn.cookiemouse.detectiontool.interfaces.OnBaseListener;
import cn.cookiemouse.detectiontool.utils.DatabaseU;
import cn.cookiemouse.detectiontool.utils.TimerU;

public class DetectionActivity extends BaseActivity {

    private static final String TAG = "DetectionActivity";

    private static final long FLUSH_DELAY = 1000;

    private ListView mListView;
    private List<DetectionData> mDetectionDataList;
    private DetectionAdapter mDetectionAdapter;

    //  参数列表
    private HashMap<Integer, List<ParameterData>> mHashMapParameterDataList;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private TimerU mTimerU;

    //  数据库操作
    private DatabaseU mDatabaseU;

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
        mTimerU = new TimerU(FLUSH_DELAY);
        mDatabaseU = new DatabaseU(this);

        initView();

        initData();

        initEvent();
    }

    private void initView() {
        mListView = findViewById(R.id.lv_activity_detection);
        mSwipeRefreshLayout = findViewById(R.id.srl_activity_detection);
        mSwipeRefreshLayout.setColorSchemeColors(0xaa303F9F);
    }

    private void initData() {
        mDetectionDataList = new ArrayList<>();
        mHashMapParameterDataList = new HashMap<>();

        //  加载数据库数据
        loadDetectiontoList();

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

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mTimerU.start();
            }
        });

        mTimerU.setOnTickListener(new TimerU.OnTickListener() {
            @Override
            public void onTick(int time) {
            }

            @Override
            public void onEnd() {
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onCancel() {
            }
        });
    }

    //  跳转到编辑页面
    private void toEdit() {
        Intent intent = new Intent(DetectionActivity.this, EditActivity.class);
        startActivity(intent);
    }

    //  申请运行时权限
    private void requestRuntimePermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.WRITE_EXTERNAL_STORAGE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        Log.i(TAG, "onAction: -->" + data.toString());
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                    }
                })
                .start();
    }

    //  获取Detection
    private void loadDetectiontoList() {
        if (null != mDatabaseU) {
            mDetectionDataList.addAll(mDatabaseU.getDetection());
        }
    }
}
