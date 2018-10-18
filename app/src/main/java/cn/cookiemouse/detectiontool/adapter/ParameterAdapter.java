package cn.cookiemouse.detectiontool.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
        final int position = i;

        ViewHolder viewHolder = null;
        ParameterData data = mParameterDataList.get(position);

        if (null == view) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_parameter, viewGroup, false);

            viewHolder.etKey = view.findViewById(R.id.et_layout_item_parameter_key);
            viewHolder.etValue = view.findViewById(R.id.et_layout_item_parameter_value);
            viewHolder.ivAddOrReduce = view.findViewById(R.id.iv_layout_item_parameter);

            view.setTag(viewHolder);

            viewHolder.etKey.addTextChangedListener(new KeyTextChangedListener(viewHolder));
            viewHolder.etValue.addTextChangedListener(new ValueTextChangedListener(viewHolder));
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.etKey.setTag(position);
        viewHolder.etValue.setTag(position);

        viewHolder.etKey.setText(data.getKey());
        viewHolder.etValue.setText(data.getValue());

        final int size = ParameterAdapter.this.getCount();
        if (position == size - 1) {
            viewHolder.ivAddOrReduce.setImageResource(R.drawable.ic_item_add);
        } else {
            viewHolder.ivAddOrReduce.setImageResource(R.drawable.ic_item_reduce);
        }

        viewHolder.ivAddOrReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int last = size - 1;
                if (position == last) {
                    mParameterDataList.add(new ParameterData());
                } else {
                    mParameterDataList.remove(position);
                }
                ParameterAdapter.this.notifyDataSetChanged();
            }
        });

        return view;
    }

    public class KeyTextChangedListener implements TextWatcher {

        private ViewHolder holder;

        KeyTextChangedListener(ViewHolder holder) {
            this.holder = holder;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (null != holder) {
                int position = (int) holder.etKey.getTag();
                ParameterData data = mParameterDataList.get(position);
                data.setKey(editable.toString());
            }
        }
    }

    public class ValueTextChangedListener implements TextWatcher {

        private ViewHolder holder;

        ValueTextChangedListener(ViewHolder holder) {
            this.holder = holder;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (null != holder) {
                int position = (int) holder.etKey.getTag();
                ParameterData data = mParameterDataList.get(position);
                data.setValue(editable.toString());
            }
        }
    }

    class ViewHolder {
        EditText etKey, etValue;
        ImageView ivAddOrReduce;
    }
}
