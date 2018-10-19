package cn.cookiemouse.detectiontool.adapter;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.cookiemouse.detectiontool.R;
import cn.cookiemouse.detectiontool.data.Data;
import cn.cookiemouse.detectiontool.data.DetectionData;
import cn.cookiemouse.detectiontool.interfaces.OnDetectionItemListener;

public class DetectionAdapter extends BaseAdapter {

    private static final String TAG = "DetectionAdapter";

    private Context mContext;
    private List<DetectionData> mDetectionDataList;

    private ViewHolder viewHolder = null;
    private OnDetectionItemListener mOnDetectionItemListener;

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

        final int position = i;

        DetectionData data = mDetectionDataList.get(i);


        if (null == view) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_detection, viewGroup, false);

            viewHolder.tvName = view.findViewById(R.id.tv_layout_item_detection_name);
            viewHolder.tvEdit = view.findViewById(R.id.tv_layout_item_detection_edit);
            viewHolder.tvDelete = view.findViewById(R.id.tv_layout_item_detection_delete);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        switch (data.getStatus()) {
            case Data.STATUS_INIT: {
                view.setBackgroundResource(R.drawable.shape_item_detection_bg_init);
                break;
            }
            case Data.STATUS_OK: {
                view.setBackgroundResource(R.drawable.shape_item_detection_bg_ok);
                break;
            }
            case Data.STATUS_ERROR: {
                view.setBackgroundResource(R.drawable.shape_item_detection_bg_error);
                break;
            }
            default: {
                view.setBackgroundResource(R.drawable.shape_item_detection_bg_init);
                Log.i(TAG, "getView: ");
            }
        }

        viewHolder.tvName.setText(data.getName());

        viewHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnDetectionItemListener){
                    throw new NullPointerException("please setOnDetectionItemListener");
                }
                mOnDetectionItemListener.onDelete(position);
            }
        });

        viewHolder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnDetectionItemListener){
                    throw new NullPointerException("please setOnDetectionItemListener");
                }
                mOnDetectionItemListener.onEdit(position);
            }
        });

        viewHolder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: -->" + "itemView");
                if (null == mOnDetectionItemListener){
                    throw new NullPointerException("please setOnDetectionItemListener");
                }
                mOnDetectionItemListener.onItemClick(position);
            }
        });

        return view;
    }

    private class ViewHolder {
        TextView tvName;
        TextView tvDelete, tvEdit;
    }

    public void setOnDetectionItemListener(OnDetectionItemListener listener) {
        this.mOnDetectionItemListener = listener;
    }
}
