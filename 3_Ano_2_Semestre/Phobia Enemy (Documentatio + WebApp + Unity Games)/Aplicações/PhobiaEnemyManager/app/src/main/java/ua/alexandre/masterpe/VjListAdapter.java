package ua.alexandre.masterpe;

import android.bluetooth.BluetoothDevice;
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
public class VjListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<BluetoothDevice> mData;
    private onConnectButton mListener;

    public VjListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<BluetoothDevice> data) {
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
            convertView			=  mInflater.inflate(R.layout.list_item_vj, null);

            holder 				= new ViewHolder();

            holder.textView_vj		= (TextView) convertView.findViewById(R.id.textView_vj);
            holder.textView_mac 	= (TextView) convertView.findViewById(R.id.textView_mac);
            holder.button_connect   = (Button) convertView.findViewById(R.id.button_connect);
            holder.checkBox_sensor = (CheckBox) convertView.findViewById(R.id.checkBox_sensor);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BluetoothDevice device	= mData.get(position);

        //holder.textView_vj		= (TextView) convertView.findViewById(R.id.textView_vj);
        //holder.textView_mac 	= (TextView) convertView.findViewById(R.id.textView_mac);

       // / holder.checkBox_sensor.setChecked(sensor.isConnected);
        holder.textView_vj.setText(device.getName());
        holder.textView_mac.setText(device.getAddress());

        holder.button_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(position);
                }
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView textView_vj;
        Button button_connect;
        TextView textView_mac;
        CheckBox checkBox_sensor;
    }

    public interface onConnectButton {
        public abstract void onItemClick(int position);
    }
}
