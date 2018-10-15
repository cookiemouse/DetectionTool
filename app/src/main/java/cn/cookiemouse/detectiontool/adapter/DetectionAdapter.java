package cn.cookiemouse.detectiontool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.cookiemouse.detectiontool.R;
import cn.cookiemouse.detectiontool.data.DetectionData;

public class DetectionAdapter extends BaseAdapter {

    private Context mContext;
    private List<DetectionData> mDetectionDataList;

    public DetectionAdapter(Context context, List<DetectionData> mDetectionDataList) {
        this.mContext = context;
        this.mDetectionDataList = mDetectionDataList;
    }

    @Override
    public int getCount() {
        return mDetectionDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return mDetectionDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        DetectionData data = mDetectionDataList.get(i);
        ViewHolder viewHolder = null;

        if (null == view) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_detection, viewGroup, false);

            viewHolder.tvName = view.findViewById(R.id.tv_layout_item_detection_name);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvName.setText(data.getName());

        return view;
    }

    private class ViewHolder {
        TextView tvName;
    }
}
