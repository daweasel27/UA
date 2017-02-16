package ua.alexandre.masterpe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by alex9 on 23/04/2016.
 */
public class SensorListAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private ArrayList<Sensor> mData;
    private onConnectButton mListener;

    public SensorListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<Sensor> data) {
        mData = data;
    }

    public void setListener(onConnectButton listener) {
        mListener = listener;
    }

    public int getCount() {
        return (mData == null) ? 0 : mData.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView			=  mInflater.inflate(R.layout.list_item_sensor, null);

            holder 				= new ViewHolder();

            holder.textView_sensor		= (TextView) convertView.findViewById(R.id.textView_vj);
            holder.textView_tv 	= (TextView) convertView.findViewById(R.id.textView_tv);
            holder.button_turnOn   = (Button) convertView.findViewById(R.id.button_turnOn);
            holder.checkBox_sensor = (CheckBox) convertView.findViewById(R.id.checkBox_sensor);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Sensor sensor	= mData.get(position);

        holder.checkBox_sensor.setChecked(sensor.isConnected);
        holder.textView_sensor.setText(sensor.name);
        holder.textView_tv.setText(sensor.tv);

        holder.button_turnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(position);
                }
            }
        });

        if(sensor.isConnected){
            holder.button_turnOn.setText("off");
        }
        else{
            holder.button_turnOn.setText("on");
        }

        return convertView;
    }

    static class ViewHolder {
        TextView textView_sensor;
        Button button_turnOn;
        TextView textView_tv;
        CheckBox checkBox_sensor;
    }

    public interface onConnectButton {
        public abstract void onItemClick(int position);
    }
}
