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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import ua.alexandre.masterpe.JSON.Send.SendValues;

/**
 * Created by alex9 on 21/04/2016.
 */
public class Manager extends Activity {
    private TextView textView_welcome;
    private BluetoothAdapter mBluetoothAdapter;
    private Button button_add_client;
    private IntentFilter filter;
    private ProgressDialog mProgressDlg;
    private ArrayList<BluetoothDevice> mDeviceList;
    private Button button_control;
    private Button button_event;
    private Button button_click;
    private long tsInit;
    private static ListView listView_clients_connected;
    private static ClientConnectedListAdapter mAdapter;
    public static Session session;
    public static int sessionId = -2;
    private RadioButton radioButton_sa,radioButton_sd;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager);

        //Layout views
        textView_welcome = (TextView) findViewById(R.id.textView_welcome_menu);
        textView_welcome.setText("Welcome " + Me.getUsername());
        button_add_client = (Button) findViewById(R.id.button_add_client);
        listView_clients_connected = (ListView) findViewById(R.id.listView_clients_connected);
        button_control = (Button) findViewById(R.id.button_control);
        button_event = (Button) findViewById(R.id.button_event);
        button_click = (Button) findViewById(R.id.button_click);
        radioButton_sa = (RadioButton) findViewById(R.id.radioButton_sa);
        radioButton_sd = (RadioButton) findViewById(R.id.radioButton_sd);

        button_control.setText("create session");
        button_event.setText("event");
        button_event.setEnabled(false);
        button_click.setEnabled(false);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //Check if device supports bluetooth
        if(mBluetoothAdapter == null){
            myToast("This device doesn't support Bluetooth");
            startActivity(new Intent(getBaseContext(), Menu.class));
        }

        //Turn on Bluetooth
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }

        //Bluetooth Visible
        if(mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
            startActivity(discoverableIntent);
        }

        //Start Bluetooth Service
        Intent i = new Intent(getBaseContext(),BluetoothService.class);
        startService(i);

        //Initialization
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        filter = new IntentFilter();
        mProgressDlg = new ProgressDialog(this);

        //Progress Dialog
        mProgressDlg.setMessage("Scanning...");
        mProgressDlg.setCancelable(false);
        mProgressDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mBluetoothAdapter.cancelDiscovery();
            }
        });

        //buttons listeners
        button_add_client.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                registerReceiver(mReceiver, filter);
                mBluetoothAdapter.startDiscovery();
            }
        });

        button_control.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
              if(button_control.getText().equals("create session")){
                  button_control.setText("start");
                  button_add_client.setEnabled(false);
                  ArrayList<Integer> cl = BluetoothService.getClientIds();
                  tsInit = System.currentTimeMillis();
                  session = new Session(tsInit,cl,Me.getId());
                  Log.e("TSINIT1",Long.toString(tsInit));
                  if(Me.isNetworkAvailable(getBaseContext())){
                      SendValues.createSession(Me.getId(),cl,tsInit,getBaseContext());
                      session.isSessionOnline = true;
                  }
              }
              else if(button_control.getText().equals("start")){
                  button_control.setText("stop");
                  button_event.setEnabled(true);
                  button_click.setEnabled(true);
                  BluetoothService.sendAll("tsConfig" + Me.SC + Long.toString(System.currentTimeMillis()));
                  if(session.isSessionOnline && (sessionId > 0));
                        BluetoothService.sendAll("sessionID"+Me.SC+sessionId);
                  Log.e("TSINIT2",Long.toString(tsInit));
                  BluetoothService.sendAll("start" + Me.SC + Long.toString(tsInit));

              }
              else if(button_control.getText().equals("stop")){
                  BluetoothService.sendAll("stop"+Me.SC+"stop");
                  button_click.setEnabled(false);
                  if(!session.isSessionOnline){
                      try {
                          Me.writeSession(getBaseContext(), session);
                      } catch (IOException e) {
                          e.printStackTrace();
                      }
                      if(Me.isNetworkAvailable(getBaseContext())){
                          try {
                              Me.uploadSessions(getBaseContext());
                          } catch (Exception e) {
                              e.printStackTrace();
                          }
                      }
                  }


                  session = null;
                  button_control.setText("create session");
                  button_event.setEnabled(false);
                  button_add_client.setEnabled(true);
              }
            }
        });

        button_event.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Log.e("ola", "event" + Me.SC + editText_event.getText());
                String e = "";
                if(radioButton_sa.isChecked()){
                    e = radioButton_sa.getText().toString();
                }
                else if(radioButton_sd.isChecked()){
                    e = radioButton_sd.getText().toString();
                }
                if(e.equals("")){
                    Toast.makeText(getBaseContext(),"Please select a event",Toast.LENGTH_LONG).show();
                }
                else{
                    if(session.isSessionOnline && (sessionId > 0)){
                        SendValues.event(sessionId,e,System.currentTimeMillis(),getBaseContext());
                    }
                    else{
                        session.events.put(System.currentTimeMillis(),e);
                    }
                }
            }
        });
        button_click.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
               Intent i = new Intent(getBaseContext(),Click.class);
                   startActivity(i);
            }
        });

        //filter configs
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        listView_clients_connected = (ListView) findViewById(R.id.listView_clients_connected);
        mAdapter = new ClientConnectedListAdapter(this);
        mAdapter.setData(new ArrayList<Client>());
        mAdapter.setListener(new ClientConnectedListAdapter.onConnectButton() {
            @Override
            public void onItemClick(int position) {
                Client c = BluetoothService.getClientList().get(position);
                Intent i = new Intent(getBaseContext(), ClientManage.class);
                i.putExtra("message", c.address);
                startActivity(i);
            }
        });
        listView_clients_connected.setAdapter(mAdapter);
        radioButton_sd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                radioButton_sa.setChecked(false);
            }
        });
        radioButton_sa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                radioButton_sd.setChecked(false);
            }
        });
    }

    public static void refreshClientsConnectedList(){
        Log.e("REFRESH", "LOL");
        ArrayList<Client> a = BluetoothService.getClientList();
        for(Client c: a){
            Log.e("Client",c.username);
        }

        mAdapter.setData(BluetoothService.getClientList());
        listView_clients_connected.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshClientsConnectedList();
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
                Intent newIntent = new Intent(getBaseContext(), NewClientList.class);
                newIntent.putParcelableArrayListExtra("device.list", mDeviceList);
                startActivity(newIntent);

            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                int c = device.getBluetoothClass().getMajorDeviceClass();
                if(c == BluetoothClass.Device.Major.COMPUTER
                    || c == BluetoothClass.Device.Major.PHONE){
                    if(!mDeviceList.contains(device)){
                        myToast("Found device " + device.getName());
                        mDeviceList.add(device);
                    }
                }

            }
        }
    };

    @Override
    public void finish() {
        Intent i = new Intent(getBaseContext(),BluetoothService.class);
        stopService(i);
        super.finish();
    }

    public void myToast(String m){
        Toast.makeText(getBaseContext(),m,Toast.LENGTH_LONG).show();
    }
}
