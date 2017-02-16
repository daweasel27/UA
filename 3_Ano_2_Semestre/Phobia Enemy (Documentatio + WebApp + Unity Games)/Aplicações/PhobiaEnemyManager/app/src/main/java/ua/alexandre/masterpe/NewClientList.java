package ua.alexandre.masterpe;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by alex9 on 21/04/2016.
 */
public class NewClientList extends Activity{
    private ListView mListView;
    private NewClientListAdapter mAdapter;
    private ArrayList<BluetoothDevice> mDeviceList;
    private Button button_ok;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_client_list);

        //Layout views
        button_ok = (Button) findViewById(R.id.button_ok);

        //buttons listeners
        button_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                for(String mac :mAdapter.getDevices()){
                    Intent i = new Intent(getBaseContext(),BluetoothService.class);
                    i.putExtra("command","connect"+ Me.SC +mac);
                    startService(i);
                }
                finish();
            }
        });

        mDeviceList	= getIntent().getExtras().getParcelableArrayList("device.list");

        //Setting up adapter
        mAdapter = new NewClientListAdapter(this);
        mAdapter.setData(mDeviceList);
       /* mAdapter.setListener(new NewClientListAdapter.OnPairButtonClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent i = new Intent(getBaseContext(), Menu.class);
            }
        });*/

        //Setting up ListView
        mListView = (ListView) findViewById(R.id.listView_devices);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
