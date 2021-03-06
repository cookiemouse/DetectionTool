package cn.cookiemouse.detectiontool.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.cookiemouse.detectiontool.R;
import cn.cookiemouse.detectiontool.adapter.ParameterAdapter;
import cn.cookiemouse.detectiontool.base.BaseActivity;
import cn.cookiemouse.detectiontool.data.Data;
import cn.cookiemouse.detectiontool.data.DetectionData;
import cn.cookiemouse.detectiontool.data.ParameterData;
import cn.cookiemouse.detectiontool.utils.DatabaseU;
import cn.cookiemouse.detectiontool.utils.RegularU;
import cn.cookiemouse.detectiontool.utils.ToastU;
import cn.cookiemouse.dialogutils.MessageDialog;

public class EditActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "EditActivity";

    private EditText mEditTextName, mEditTextAddress;
    private ListView mListView;
    private Button mButtonSave;

    private List<ParameterData> mParameterDataList;
    private ParameterAdapter mParameterAdapter;

    private ToastU mToastU;
    private MessageDialog mMessageDialog;

    //  数据库操作
    private DatabaseU mDatabaseU;
    //  传rowid过来
    private long mRowDetectionId = Data.DATA_INIT_ROWID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        init();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(EditActivity.this.getCurrentFocus().getWindowToken()
                    , InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (NullPointerException e) {
            Log.e(TAG, "onBackPressed: ", e);
        } finally {
            showSaveMessage();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_activity_edit_save: {
                String name = mEditTextName.getText().toString();
                String address = mEditTextAddress.getText().toString();
                if (RegularU.isEmpty(name)) {
                    mToastU.showToast("请输入名称");
                    return;
                }
                if (RegularU.isEmpty(address)) {
                    mToastU.showToast("请输入地址");
                    return;
                }
                Log.i(TAG, "onClick: mRowDetectionId-->" + mRowDetectionId);
                if (Data.DATA_INIT_ROWID == mRowDetectionId) {
                    mRowDetectionId = mDatabaseU.addDetection(new DetectionData(name, address));
                } else {
                    mDatabaseU.addDetection(new DetectionData(mRowDetectionId, name, address));
                }
                //  填写mRowDetectionId，添加
                for (ParameterData parameterData : mParameterDataList) {
                    parameterData.setRowidDetection(mRowDetectionId);
                }

                //  保存参数
                //  先清空该目录下的参数，再保存
                mDatabaseU.deleteParameter(mRowDetectionId);
                mDatabaseU.addParameter(mParameterDataList);

                EditActivity.this.finish();
                break;
            }
        }
    }

    private void init() {
        mToastU = new ToastU(this);
        mDatabaseU = new DatabaseU(this);
        Intent intent = getIntent();
        mRowDetectionId = intent.getLongExtra(Data.DATA_INTENT_ROWID, Data.DATA_INIT_ROWID);
        Log.i(TAG, "init: mRowDetectionId-->" + mRowDetectionId);

        initView();

        initData();

        initEvent();
    }

    private void initView() {
        this.setTitle("编辑");
        this.setLeftVisibility(true);

        mEditTextName = findViewById(R.id.et_activity_edit_name);
        mEditTextAddress = findViewById(R.id.et_activity_edit_address);
        mListView = findViewById(R.id.lv_activity_edit_parameter);
        mButtonSave = findViewById(R.id.btn_activity_edit_save);
    }

    private void initData() {
        mParameterDataList = new ArrayList<>();
        loadData();

        mParameterAdapter = new ParameterAdapter(this, mParameterDataList);
        mListView.setAdapter(mParameterAdapter);

    }

    private void initEvent() {
        mButtonSave.setOnClickListener(this);
    }

    //  loadData
    private void loadData() {
        loadDetection();
        loadParameter();
    }

    //  loadDetection
    private void loadDetection() {
        DetectionData detectionData = mDatabaseU.getDetection(mRowDetectionId);
        if (null != detectionData) {
            mEditTextName.setText(detectionData.getName());
            mEditTextAddress.setText(detectionData.getAddress());
        }
    }

    //  loadParameter
    private void loadParameter() {
        mParameterDataList.clear();
        mParameterDataList.addAll(mDatabaseU.getParameter(mRowDetectionId));
        Log.i(TAG, "loadParameter: size-->" + mParameterDataList.size());
        if (mParameterDataList.size() < 1) {
            mParameterDataList.add(new ParameterData());
        }
    }

    //  退出页面时提醒保存
    private void showSaveMessage() {
        String name = mEditTextName.getText().toString();
        String address = mEditTextAddress.getText().toString();
        if (!RegularU.isEmpty(name) || !RegularU.isEmpty(address)) {
            mMessageDialog = MessageDialog.with(this)
                    .setMessage("有数据未保存，是否退出？")
                    .setNegativeClickListener("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //  do nothing
                        }
                    })
                    .setPositiveClickListener("退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mMessageDialog.dismiss();
                            EditActivity.this.finish();
                        }
                    })
                    .show();
        } else {
            EditActivity.this.finish();
        }
    }
}
