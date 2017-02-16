package ua.alexandre.masterpe;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by alex9 on 23/04/2016.
 */
public class ChooseVJ extends Activity {
    private ListView mListView;
    private VjListAdapter mAdapter;
    private ArrayList<BluetoothDevice> mDeviceList;
    ProgressDialog mProgressDlg;
    BluetoothAdapter mBluetoothAdapter;
    private IntentFilter filter;
    private String clientAddress;
    //private Button button_ok;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_vj);

        clientAddress = getIntent().getExtras().getString("message");

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //Progress Dialog
        mProgressDlg = new ProgressDialog(this);
        mProgressDlg.setMessage("Scanning...");
        mProgressDlg.setCancelable(false);
        mProgressDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mBluetoothAdapter.cancelDiscovery();
            }
        });

        filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        //Layout views
        //button_ok = (Button) findViewById(R.id.button_ok);

        //buttons listeners
       /* button_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                for(String mac :mAdapter.getDevices()){
                    Intent i = new Intent(getBaseContext(),BluetoothService.class);
                    i.putExtra("command","connect"+ Me.SC +mac);
                    startService(i);
                }
                finish();
            }
        });*/

        //mDeviceList	= getIntent().getExtras().getParcelableArrayList("vj.list");

        //Setting up adapter
        mAdapter = new VjListAdapter(this);
        mAdapter.setData(mDeviceList);
        mAdapter.setListener(new VjListAdapter.onConnectButton() {
            @Override
            public void onItemClick(int position) {
                String deviceAdd = mDeviceList.get(position).getAddress();
                BluetoothService.sendTo(clientAddress,"turnOnVj"+Me.SC+deviceAdd);
                BluetoothService.clients.get(clientAddress).getSensor("Vital Jacket").tv = deviceAdd;
                finish();
            }
        });

        //Setting up ListView
        mListView = (ListView) findViewById(R.id.listView_vj);
        mListView.setAdapter(mAdapter);

        registerReceiver(mReceiver,filter);
        mBluetoothAdapter.startDiscovery();
    }
    public void refresh(){
        mAdapter.setData(mDeviceList);
        mListView.setAdapter(mAdapter);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                mDeviceList = new ArrayList<BluetoothDevice>();
                mProgressDlg.show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                mProgressDlg.dismiss();
                unregisterReceiver(this);
                refresh();

            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                int c = device.getBluetoothClass().getMajorDeviceClass();
                if(!(c == BluetoothClass.Device.Major.COMPUTER
                        || c == BluetoothClass.Device.Major.PHONE)){
                    if(!mDeviceList.contains(device)){
                        mDeviceList.add(device);
                        Toast.makeText(getBaseContext(),"Found device " + device.getName(),Toast.LENGTH_LONG).show();
                        refresh();
                    }
                }
            }
        }
    };
}
