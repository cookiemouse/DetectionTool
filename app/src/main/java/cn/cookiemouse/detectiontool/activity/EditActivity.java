package cn.cookiemouse.detectiontool.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.cookiemouse.detectiontool.R;
import cn.cookiemouse.detectiontool.adapter.ParameterAdapter;
import cn.cookiemouse.detectiontool.base.BaseActivity;
import cn.cookiemouse.detectiontool.data.ParameterData;

public class EditActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "EditActivity";

    private EditText mEditTextName, mEditTextAddress;
    private ListView mListView;
    private Button mButtonSave;

    private List<ParameterData> mParameterDataList;
    private ParameterAdapter mParameterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        init();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_activity_edit_save: {
                break;
            }
        }
    }

    private void init() {
        initView();

        initData();

        initEvent();
    }

    private void initView() {
        this.setTitle("编辑");
        this.setLeftVisibility(true);

        mListView = findViewById(R.id.lv_activity_edit_parameter);
    }

    private void initData() {
        mParameterDataList = new ArrayList<>();
        mParameterDataList.add(new ParameterData());

        mParameterAdapter = new ParameterAdapter(this, mParameterDataList);
        mListView.setAdapter(mParameterAdapter);
    }

    private void initEvent() {
    }
}
