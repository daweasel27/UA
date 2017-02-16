package ua.alexandre.masterpe;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by alex9 on 21/04/2016.
 */
public class NewClientListAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private List<BluetoothDevice> mData;
    private HashMap<Integer,Boolean> checks = new HashMap<>();
    private OnPairButtonClickListener mListener;

    public NewClientListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setData(List<BluetoothDevice> data) {
        mData = data;
    }

    public void setListener(OnPairButtonClickListener listener) {
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
            convertView			=  mInflater.inflate(R.layout.list_item_device, null);

            holder 				= new ViewHolder();

            holder.textView_name		= (TextView) convertView.findViewById(R.id.textView_vj);
            holder.textView_mac 	= (TextView) convertView.findViewById(R.id.textView_mac);
           // holder.button_connect   = (Button) convertView.findViewById(R.id.btn_connect);
            holder.checkBox_device = (CheckBox) convertView.findViewById(R.id.checkBox_sensor);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BluetoothDevice device	= mData.get(position);
        holder.textView_name.setText(device.getName());
        holder.textView_mac.setText(device.getAddress());

       /* holder.button_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(position);
                }
            }
        });*/

        holder.checkBox_device.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                boolean tmp = checks.get(position);
                checks.remove(position);
                checks.put(position, !tmp);

            }
        });
        if (BluetoothService.clients.containsKey(device.getAddress())){
            holder.checkBox_device.setEnabled(false);
        }
        checks.put(position,holder.checkBox_device.isChecked());

        return convertView;
    }

    static class ViewHolder {
        TextView textView_name;
        TextView textView_mac;
        //TextView button_connect;
        CheckBox checkBox_device;
    }
    public ArrayList<String> getDevices(){
        ArrayList c = new ArrayList();
        for (int i = 0;i < checks.size(); i++) {
            if(checks.get(i)){
                c.add(mData.get(i).getAddress());
            }
        }
        return c;
    }
    public interface OnPairButtonClickListener {
        public abstract void onItemClick(int position);
    }
}
