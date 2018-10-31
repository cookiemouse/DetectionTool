package cn.cookiemouse.detectiontool;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.cookiemouse.detectiontool.activity.EditActivity;
import cn.cookiemouse.detectiontool.adapter.DetectionAdapter;
import cn.cookiemouse.detectiontool.base.BaseActivity;
import cn.cookiemouse.detectiontool.data.Data;
import cn.cookiemouse.detectiontool.data.DetectionData;
import cn.cookiemouse.detectiontool.data.ParameterData;
import cn.cookiemouse.detectiontool.interfaces.OnBaseListener;
import cn.cookiemouse.detectiontool.interfaces.OnDetectionItemListener;
import cn.cookiemouse.detectiontool.utils.DatabaseU;
import cn.cookiemouse.detectiontool.utils.NetworkU;
import cn.cookiemouse.detectiontool.utils.RegularU;
import cn.cookiemouse.detectiontool.utils.TimerU;
import cn.cookiemouse.detectiontool.utils.ToastU;
import cn.cookiemouse.dialogutils.LoadingDialog;
import cn.cookiemouse.dialogutils.MessageDialog;
import cn.cookiemouse.dialogutils.ViewDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DetectionActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "DetectionActivity";

    private static final long FLUSH_DELAY = 400;

    private ImageView mImageViewRun;
    private ListView mListView;
    private List<DetectionData> mDetectionDataList;
    private DetectionAdapter mDetectionAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private TimerU mTimerUFlush;
    private TimerU mTimerUExit;
    //  双击退出
    private boolean mExitAble = false;
    private ToastU mToastU;

    //  数据库操作
    private DatabaseU mDatabaseU;

    //  网络请求
    private NetworkU mNetworkU;

    //  LoadingDialog
    private LoadingDialog mLoadingDialog;

    private MessageDialog mMessageDialog;

    //  长控显示内容
    private boolean mIsLongClick = false;
    private ViewDialog mViewDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //  加载数据库数据
        loadDetectionToList();
    }

    @Override
    public void onBackPressed() {
        if (mExitAble) {
            super.onBackPressed();
        } else {
            mToastU.showToast("再次点击退出");
            mExitAble = true;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_activity_detection_run: {
                netRequestAll();
                break;
            }
        }
    }

    private void init() {
        this.setTitle("检测");
        this.setRightVisibility(true);
        this.setRightResource(R.drawable.ic_add);
        mTimerUFlush = new TimerU(FLUSH_DELAY);
        mTimerUExit = new TimerU(2);
        mToastU = new ToastU(this);
        mDatabaseU = new DatabaseU(this);
        mNetworkU = new NetworkU();

        initView();

        initData();

        initEvent();
    }

    private void initView() {
        mImageViewRun = findViewById(R.id.iv_activity_detection_run);
        mListView = findViewById(R.id.lv_activity_detection);
        mSwipeRefreshLayout = findViewById(R.id.srl_activity_detection);
        mSwipeRefreshLayout.setColorSchemeColors(0xaa757575);
    }

    private void initData() {
        mDetectionDataList = new ArrayList<>();
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
                mTimerUFlush.start();
                loadDetectionToList();
            }
        });

        mTimerUFlush.setOnTickListener(new TimerU.OnTickListener() {
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

        mTimerUExit.setOnTickListener(new TimerU.OnTickListener() {
            @Override
            public void onTick(int time) {
            }

            @Override
            public void onEnd() {
                mExitAble = false;
            }

            @Override
            public void onCancel() {
            }
        });

        mDetectionAdapter.setOnDetectionItemListener(new OnDetectionItemListener() {
            @Override
            public void onDelete(int position) {
                showDeleteDialog(position);
            }

            @Override
            public void onEdit(int position) {
                DetectionData data = mDetectionDataList.get(position);
                toEdit(data.getRowid());
            }

            @Override
            public void onItemClick(int position) {
                netRequest(position);
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                // TODO: 18-10-19 显示内容
                mIsLongClick = true;
                netRequest(i);
                return true;
            }
        });

        mImageViewRun.setOnClickListener(this);
    }

    //  跳转到编辑页面
    private void toEdit() {
        Intent intent = new Intent(DetectionActivity.this, EditActivity.class);
        startActivity(intent);
    }

    //  跳转到编辑页面
    private void toEdit(long rowid) {
        Intent intent = new Intent(DetectionActivity.this, EditActivity.class);
        intent.putExtra(Data.DATA_INTENT_ROWID, rowid);
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
    private void loadDetectionToList() {
        mDetectionDataList.clear();
        if (null != mDatabaseU) {
            mDetectionDataList.addAll(mDatabaseU.getDetection());
            mDetectionAdapter.notifyDataSetChanged();
        }
    }

    //  请求网络
    private void netRequest(int position) {
        HashMap<String, String> hashMap = new HashMap<>();

        DetectionData data = mDetectionDataList.get(position);
        final String tagRequest = "" + position;
        long rowid = data.getRowid();
        String url = data.getAddress();
        for (ParameterData parameterData : mDatabaseU.getParameter(rowid)) {
            hashMap.put(parameterData.getKey(), parameterData.getValue());
        }
        if (!RegularU.isEnableAddress(url)) {
            mToastU.showToast("非有效地址！");
            return;
        }
        showLoading();
        mNetworkU.connectUrl(tagRequest, url, hashMap, new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                Log.i(TAG, "onFailure: ");
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mIsLongClick) {
                                showViewDialog(e.toString());
                            }
                            mDetectionDataList.get(Integer.valueOf(tagRequest)).setStatus(Data.STATUS_ERROR);
                            mDetectionAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (Exception exception) {
                    Log.e(TAG, "onFailure: -->", exception);
                } finally {
                    dismissLoading();
                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    final String str = response.body().string();
                    Log.i(TAG, "onResponse: -->" + str);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "run: tag=position-->" + tagRequest);
                            if (mIsLongClick) {
                                showViewDialog(str);
                            }
                            if (response.code() < 400) {
                                mDetectionDataList.get(Integer.valueOf(tagRequest)).setStatus(Data.STATUS_OK);
                            } else {
                                mDetectionDataList.get(Integer.valueOf(tagRequest)).setStatus(Data.STATUS_ERROR);
                            }
                            mDetectionAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (NullPointerException e) {
                    Log.e(TAG, "onResponse: 空指针-->", e);
                } catch (ArrayIndexOutOfBoundsException e) {
                    Log.e(TAG, "onResponse: 越界-->", e);
                } finally {
                    dismissLoading();
                }
            }
        });
    }

    //  请求网络
    private void netRequestAll() {
        int size = mDetectionDataList.size();
        for (int i = 0; i < size; i++) {
            netRequest(i);
        }
    }

    //  显示Loading动画
    private void showLoading() {
        dismissLoading();
        mLoadingDialog = LoadingDialog.with(this)
                .setDimAmount(0.1f)
                .setCancelable(true)
                .show();
    }

    //  取消Loading动画
    private void dismissLoading() {
        if (null != mLoadingDialog) {
            mLoadingDialog.dismiss();
        }
    }

    //  显示Delete Dialog
    private void showDeleteDialog(final int position) {
        //  退出页面时提醒保存
        mMessageDialog = MessageDialog.with(this)
                .setMessage("是否删除？")
                .setNegativeClickListener("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //  do nothing
                        mMessageDialog.dismiss();
                    }
                })
                .setPositiveClickListener("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DetectionData data = mDetectionDataList.get(position);
                        long rowid = data.getRowid();
                        mDatabaseU.deleteDetection(data);
                        mDatabaseU.deleteParameter(rowid);
                        loadDetectionToList();
                        mMessageDialog.dismiss();
                    }
                })
                .show();
    }

    //  显示长控内容
    private void showViewDialog(String msg) {
        View viewLongClick = LayoutInflater.from(this).inflate(R.layout.layout_message_view, null);
        TextView textView = viewLongClick.findViewById(R.id.tv_layout_message_view);
        textView.setText(msg);
        mIsLongClick = false;
        mViewDialog = ViewDialog.with(this)
                .setView(viewLongClick)
                .setCancelable(true)
                .show();
    }
}
