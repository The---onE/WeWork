package com.xmx.wework.Sync;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xmx.wework.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by The_onE on 2016/3/27.
 */
public class SyncAdapter extends BaseAdapter {
    Context mContext;

    public SyncAdapter(Context context) {
        mContext = context;
    }

    public void updateList() {
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return SyncManager.getInstance().getData().size();
    }

    @Override
    public Object getItem(int i) {
        if (i < SyncManager.getInstance().getData().size()) {
            return SyncManager.getInstance().getData().get(i);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        if (i < SyncManager.getInstance().getData().size()) {
            return SyncManager.getInstance().getData().get(i).mId;
        } else {
            return i;
        }
    }

    static class ViewHolder {
        TextView data;
        TextView time;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_sql, null);
            holder = new ViewHolder();
            holder.data = (TextView) convertView.findViewById(R.id.item_data);
            holder.time = (TextView) convertView.findViewById(R.id.item_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        List<Sync> sqlList = SyncManager.getInstance().getData();
        if (position < sqlList.size()) {
            Sync sql = sqlList.get(position);
            holder.data.setText(sql.mData);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeString = df.format(sql.mTime);
            holder.time.setText(timeString);
        } else {
            holder.data.setText("加载失败");
        }

        return convertView;
    }
}