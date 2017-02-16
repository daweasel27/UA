package ua.alexandre.masterpe;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by alex9 on 22/04/2016.
 */
public class ClientManage extends Activity {
    TextView textView_user;
    TextView textView_sensors;
    TextView textView_welcome;
    Button button_disconnect;

    private static ListView mListView;
    private static SensorListAdapter mAdapter;
    private static ArrayList<Sensor> mDeviceList;
    Client client;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_manage);

        //get Client
        String add = getIntent().getExtras().getString("message");
        client = BluetoothService.getClient(add);

        //Layout views
        textView_user = (TextView) findViewById(R.id.textView_user);
        textView_welcome = (TextView) findViewById(R.id.textView_welcome_menu);
        button_disconnect = (Button) findViewById(R.id.button_disconnect);

        textView_welcome.setText("Welcome " + Me.getUsername());
        textView_user.setText(client.username);

        button_disconnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(),BluetoothService.class);
                i.putExtra("command","disconnect"+ Me.SC +client.address);
                startService(i);
                finish();
            }
        });

        mDeviceList	= client.sensors;

        //Setting up adapter
        mAdapter = new SensorListAdapter(this);
        mAdapter.setData(mDeviceList);
        mAdapter.setListener(new SensorListAdapter.onConnectButton() {
            @Override
            public void onItemClick(int position) {
                //Intent i = new Intent(getBaseContext(), Menu.class);
                Sensor s = mDeviceList.get(position);
                if(s.isConnected){
                    if(s.name.equals("Vital Jacket")){
                        BluetoothService.sendTo(client.address,"turnOffVj");
                    }
                    else if(s.name.equals("Video")){
                        BluetoothService.sendTo(client.address,"turnOffVideo");
                    }
                }
                else {
                    if (s.name.equals("Vital Jacket")) {
                        Intent i = new Intent(getBaseContext(), ChooseVJ.class);
                        i.putExtra("message", client.address);
                        startActivity(i);
                    }
                    else if(s.name.equals("Video")){
                        BluetoothService.sendTo(client.address,"turnOnVideo");
                    }
                }
            }
        });

        //Setting up ListView
        mListView = (ListView) findViewById(R.id.listView_sensor);
        mListView.setAdapter(mAdapter);
    }
    public static void refresh(){
        mAdapter.setData(mDeviceList);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void finish() {
        Manager.refreshClientsConnectedList();
        super.finish();
    }
}
