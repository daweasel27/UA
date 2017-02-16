package ua.alexandre.masterpe;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by alex9 on 22/04/2016.
 */
public class ClientConnectedListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<Client> mData;
    private onConnectButton mListener;

    public ClientConnectedListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<Client> data) {
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
            convertView			=  mInflater.inflate(R.layout.list_client_connected, null);

            holder 				= new ViewHolder();

            holder.textView_client_name		= (TextView) convertView.findViewById(R.id.textView_client_name);
            //holder.textView_mac 	= (TextView) convertView.findViewById(R.id.textView_mac);
            holder.button_manage   = (Button) convertView.findViewById(R.id.button_manage);
            //holder.checkBox_device = (CheckBox) convertView.findViewById(R.id.checkBox_device);
            holder.textView_sensors = (TextView) convertView.findViewById(R.id.textView_sensors);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Client client	= mData.get(position);
        holder.textView_client_name.setText(client.username);
        String ss = "";
        for(Sensor s : client.sensors){
            if(s.isConnected)
                ss += "\n" + s.name;
        }
        holder.textView_sensors.setText(ss);
        Log.e("asgasg",holder.textView_client_name.getText().toString());
        if(holder.textView_client_name.getText().toString().equals("Waiting Information")){
            holder.button_manage.setEnabled(false);
        }
        else{
            holder.button_manage.setEnabled(true);
        }
        //holder.textView_mac.setText(device.getAddress());

       holder.button_manage.setOnClickListener(new View.OnClickListener() {
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
        TextView textView_client_name;
        Button button_manage;
        //TextView button_connect;
        //CheckBox checkBox_device;
        TextView textView_sensors;
    }

    public interface onConnectButton {
        public abstract void onItemClick(int position);
    }
}
