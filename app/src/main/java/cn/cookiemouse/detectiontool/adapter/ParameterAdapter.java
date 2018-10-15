package cn.cookiemouse.detectiontool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import java.util.List;

import cn.cookiemouse.detectiontool.R;
import cn.cookiemouse.detectiontool.data.ParameterData;

public class ParameterAdapter extends BaseAdapter {

    private static final String TAG = "ParameterAdapter";

    private Context mContext;
    private List<ParameterData> mParameterDataList;

    public ParameterAdapter(Context context, List<ParameterData> mParameterDataList) {
        this.mContext = context;
        this.mParameterDataList = mParameterDataList;
    }

    @Override
    public int getCount() {
        return mParameterDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return mParameterDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ParameterData data = mParameterDataList.get(i);

        ViewHolder viewHolder = null;

        if (null == view) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_parameter, viewGroup, false);

            viewHolder.etKey = view.findViewById(R.id.et_layout_item_parameter_key);
            viewHolder.etValue = view.findViewById(R.id.et_layout_item_parameter_value);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.etKey.setText(data.getKey());
        viewHolder.etValue.setText(data.getValue());

        return view;
    }

    class ViewHolder {
        EditText etKey, etValue;
    }
}
