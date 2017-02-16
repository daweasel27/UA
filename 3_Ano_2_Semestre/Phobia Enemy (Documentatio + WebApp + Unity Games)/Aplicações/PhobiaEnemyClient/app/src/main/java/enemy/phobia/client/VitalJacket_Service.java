package enemy.phobia.client;

import android.annotation.TargetApi;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import Bio.Library.namespace.BioLib;
import enemy.phobia.client.JSON.Send.SendValues;

import java.io.InputStream;
import java.io.OutputStreamWriter;

/**
 * Created by Filipe Tavares on 18/04/2016.
 */
public class VitalJacket_Service extends Service {

    public static int HR = 1;
    public static long HR1 = 1;
    private BioLib lib = null;
    public boolean isConn = false;
    private long time = 0;
    //private String mConnectedDeviceName = "";
   // public static final String DEVICE_NAME = "device_name";

    public VitalJacket_Service(){
        super();
        Log.e("VJ_SERVICE -> ", "Created");
    }

    private final Handler mHandler = new Handler()
    {

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case BioLib.MESSAGE_READ:
                    //  textDataReceived.setText("RECEIVED: " + msg.arg1);
                    break;

                case BioLib.MESSAGE_DEVICE_NAME:
                    //mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                   // Toast.makeText(getApplicationContext(), "Connected to " + VjListActivity.deviceSelected.getName(), Toast.LENGTH_SHORT).show();
                    break;

                case BioLib.MESSAGE_BLUETOOTH_NOT_SUPPORTED:
                    //Toast.makeText(getApplicationContext(), "Bluetooth NOT supported. Aborting! ", Toast.LENGTH_SHORT).show();
                    isConn = false;
                    break;

                case BioLib.MESSAGE_BLUETOOTH_ENABLED:
                    //Toast.makeText(getApplicationContext(), "Bluetooth is now enabled! ", Toast.LENGTH_SHORT).show();
                    break;

                case BioLib.MESSAGE_BLUETOOTH_NOT_ENABLED:
                    //Toast.makeText(getApplicationContext(), "Bluetooth not enabled! ", Toast.LENGTH_SHORT).show();
                    isConn = false;
                    break;

                case BioLib.REQUEST_ENABLE_BT:
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    //startActivityForResult(enableIntent, BioLib.REQUEST_ENABLE_BT);
                    break;

                case BioLib.STATE_CONNECTING:
                    Toast.makeText(getApplicationContext(), "Connecting to Vital Jacket ", Toast.LENGTH_SHORT).show();
                    break;

                case BioLib.STATE_CONNECTED:
                    Toast.makeText(getApplicationContext(), "Connected to Vital Jacket" , Toast.LENGTH_SHORT).show();
                    BluetoothService.sendToManager("sensorOn"+Me.SC+"Vital Jacket");
                    Me.getSensor("Vital Jacket").isConnected = true;
                    break;

                case BioLib.UNABLE_TO_CONNECT_DEVICE:
                    Toast.makeText(getApplicationContext(), "Unable to connect Vital Jacket! ", Toast.LENGTH_SHORT).show();
                    BluetoothService.sendToManager("sensorOff"+Me.SC+"Vital Jacket");
                    Me.getSensor("Vital Jacket").isConnected = false;
                    break;

                case BioLib.MESSAGE_DISCONNECT_TO_DEVICE:
                    //Toast.makeText(getApplicationContext(), "Device connection was lost", Toast.LENGTH_SHORT).show();
                    isConn = false;
                    break;

                case BioLib.MESSAGE_DATA_UPDATED:
                    BioLib.Output out = (BioLib.Output)msg.obj;
                    //hr.setText("HR: " + out.pulse + " bpm     Nb. Leads: " + lib.GetNumberOfChannels());
                    HR = out.pulse;
                    long x = 0;
                    if(Me.isSending){
                        long ts = System.currentTimeMillis()+Me.timeStampToAdd;
                        if(BluetoothService.session.isSessionOnline){
                            SendValues.send(ts, Me.sessionId, Me.getSensor("Vital Jacket").id,out.pulse, getBaseContext());
                        }
                        else{
                            BluetoothService.session.values.get(Me.getSensor("Vital Jacket").id).put(ts,out.pulse);
                        }

                        long xx = System.currentTimeMillis();
                        if(!Me.clickSending){
                            if(xx >= x + 1000) {
                                x = xx;
                                Intent i = new Intent(VitalJacket_Service.this, SendToUnityService.class);
                                i.putExtra("message", Integer.toString(out.pulse));
                                startService(i);
                            }
                        }
                    }
                    //Log.e("VJ",Integer.toString(HR));
                    break;

                case BioLib.MESSAGE_PEAK_DETECTION:
                    BioLib.QRS qrs1 = (BioLib.QRS)msg.obj;
                    HR1 = qrs1.bpmi;
                    Log.e("x", Long.toString(HR1));
                    //textHR.setText("PEAK: " + qrs.position + "  BPMi: " + qrs.bpmi + " bpm  BPM: " + qrs.bpm + " bpm  R-R: " + qrs.rr + " ms");
                    break;

                case BioLib.MESSAGE_ACC_UPDATED:
                    BioLib.DataACC
                            dataACC = (BioLib.DataACC)msg.obj;
                    //accx.setText(""+dataACC.X);
                    //accy.setText(""+dataACC.Y);
                    //accz.setText(""+dataACC.Z);
                    break;
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String m = "";
        try {
            m = intent.getExtras().getString("message");
        }catch (Exception e){}
        if(m != ""){
            Log.e("SERVICE","COMMAND " + m);
        }
        String[] parts;
        String command = "";
        String msg = "";
        try {
            parts = m.split(Me.SC);
            command = parts[0];
            msg = parts[1];
        }catch (Exception e){e.printStackTrace();}
        if(command.equals("connect")){
            try {
                lib.Connect(msg, 5);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onCreate() {
        Log.e("SERVICE", "Starting...");
        super.onCreate();
        try {
            lib = new BioLib(this, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        try {
            lib.Disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
