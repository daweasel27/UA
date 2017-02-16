package enemy.phobia.client;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.hardware.*;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import enemy.phobia.client.JSON.Send.SendValues;


public class BluetoothService extends Service {
    static AcceptThread at;
    boolean onSession;
    boolean connectedToManager;
    BluetoothAdapter mBluetoothAdapter;
    Handler handler;
    public static Session session;
    public static long localId = 0;
    @Override
    public void onCreate() {
        Log.e("BluetoothService", "Service created");
        handler = new Handler(BluetoothService.this.getMainLooper());
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Me.isSessionOnline = false;
        //Check if device supports bluetooth
        /*if(mBluetoothAdapter == null){
            myToast("This device doesn't support Bluetooth");
            myStartActivity(new Intent(getBaseContext(),ServingManager.class));
            this.stopSelf();
        }

        //Bluetooth Visible
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
        myStartActivity(discoverableIntent);
        */
        onSession = false;
        connectedToManager = false;
        at = new AcceptThread();
        at.start();
        super.onCreate();

    }
    public static void sendToManager(String m){
        at.ct.send(m);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String m = "";
        try {
            m = intent.getExtras().getString("command");
        }catch (Exception e){}

        if(m != ""){
            Log.e("BluetoothService", "Command: " + m);
            String[] parts;
            String command = "";
            String msg = "";
            try {
                parts = m.split(";");
                command = parts[0];
                msg = parts[1];
            }catch (Exception e){e.printStackTrace();}
            if(command.equals("send")) {
                at.ct.send(msg);
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        at.interrupt();
        super.onDestroy();
        Log.e("BluetoothService", "Service stopped");
    }

    @Override
    public IBinder onBind(Intent intent) {return null;}

    public void myStartActivity(final Intent i){
        handler.post(new Runnable() {
            @Override
            public void run() {
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
    }
    public void myToast(final String m){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(),m,Toast.LENGTH_LONG).show();
            }
        });
    }


    public class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;
        public ConnectedThread ct;
        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            try {
                UUID uuid = UUID.fromString("3b7add25-6ebd-4407-a645-66205b76e450");
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("BLTServer", uuid);
            } catch (IOException e) { }
            mmServerSocket = tmp;
            Log.e("BluetoothService","at created");
        }

        public void run() {
            BluetoothSocket socket;
            while (!isInterrupted()) {
                socket = null;
                Log.e("BluetoothService", "Listenning");
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    //Log.e("BuetoothService","Once");
                    //interrupt();
                    return;
                }
                Log.e("BluetoothService", "Manager Connected");
                myToast("Manager Connected");
                if (socket != null) {
                    if(!onSession) {
                        ct = new ConnectedThread(socket);
                        ct.start();
                        connectedToManager = true;
                    }
                }
            }
        }

        @Override
        public void interrupt() {
            Log.e("BluetoothService","at interrupted");
            if(ct != null)
                ct.interrupt();
            try {
                mmServerSocket.close();
            } catch (IOException e) { }
            super.interrupt();
        }
    }

    public class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream is;
        private final OutputStreamWriter os;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStreamWriter tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = new OutputStreamWriter(socket.getOutputStream());;
            } catch (IOException e) { }
            is = tmpIn;
            os = tmpOut;
            Log.e("BluetoothService", "ct created");
            send("username" + Me.SC + Me.getUsername());
            send("id"+Me.SC+Integer.toString(Me.getId()));
            ArrayList<Sensor> s = Me.getSensors();
            for(Sensor a: s){
                if(!a.name.equals("Click"))
                    send("sensor"+Me.SC+a.name+";" + Integer.toString(a.id));
            }
        }

        public void run() {
            int bufferSize = 1024;
            int bytesRead = -1;
            byte[] buffer = new byte[bufferSize];
            while (!this.isInterrupted()) {
                final StringBuilder sb = new StringBuilder();
                try {
                    bytesRead = is.read(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                    interrupt();
                    myToast("Manager Disconnected");
                    connectedToManager = false;
                    received("turnOffVideo");
                    received("turnOffVj");
                    return;
                }
                if (bytesRead != -1) {
                    String result = "";
                    while ((bytesRead == bufferSize) && (buffer[bufferSize - 1] != 0)) {
                        result = result + new String(buffer, 0, bytesRead - 1);
                        try {
                            bytesRead = is.read(buffer);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    result = result + new String(buffer, 0, bytesRead - 1);
                    sb.append(result);
                }
                String[] m = sb.toString().split("\n");
                for(int i = 0; i< m.length;i++){
                    if(m[i].length() != 0)
                                received(m[i]);
                }
            }
        }
        public void send(String m) {
            Log.e("BluetoothService","Sending: "+m);
            try {
                os.write(m + "\n\n");
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void received(String m){
            Log.e("BluetoothService", "Received: " + m);
            String[] parts;
            String command = "";
            String msg = "";
            try {
                parts = m.split(Me.SC);
                command = parts[0];
                msg = parts[1];
            }catch (Exception e){e.printStackTrace();}
            if(command.equals("turnOnVj")) {
                if(!Me.getSensor("Vital Jacket").isConnected) {
                    Me.getSensor("Vital Jacket").isConnected = true;
                    Intent i = new Intent(getBaseContext(), VitalJacket_Service.class);
                    i.putExtra("message", "connect" + Me.SC + msg);
                    startService(i);
                    //send("sensorOn" + Me.SC + "Vital Jacket");
                }
            }
            else if(command.equals("turnOffVj")){
                Intent i = new Intent(getBaseContext(), VitalJacket_Service.class);
                //i.putExtra("message", "connect" + Me.SC + msg);
                stopService(i);
                Me.getSensor("Vital Jacket").isConnected = false;
                send("sensorOff"+Me.SC+"Vital Jacket");
            }
            else if(command.equals("turnOnVideo")){
                if(!Me.getSensor("Video").isConnected){
                    Me.getSensor("Video").isConnected = true;
                    Intent i = new Intent(getBaseContext(),Video_app.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }

            }
            else if(command.equals("turnOffVideo")){
                if(Me.getSensor("Video").isConnected){
                    Me.getSensor("Video").isConnected = false;
                    Intent intent = new Intent("command");
                    intent.putExtra("command", "turnOff");
                    LocalBroadcastManager.getInstance(BluetoothService.this).sendBroadcast(intent);
                }
            }
            else if(command.equals("start")){
                myToast("Session started");
                Log.e("OLA", Integer.toString(Me.sessionId));
                localId = Long.parseLong(msg);
                Log.e("LOCALID",Long.toString(localId));
                session = new Session(localId,Me.managerId);
                session.isSessionOnline = Me.isSessionOnline;
                for(Sensor s :Me.getSensors()){
                    if(s.isConnected && !s.name.equals("Video")){
                        session.values.put(s.id,new HashMap<Long, Integer>());
                    }
                }
                session.values.put(Me.getSensor("Click").id,new HashMap<Long, Integer>());
                if(Me.getSensor("Video").isConnected){
                    session.hasVideo = true;
                    Intent i = new Intent("command");
                    i.putExtra("command","start");
                    LocalBroadcastManager.getInstance(BluetoothService.this).sendBroadcast(i);
                }
                Me.isSending = true;
            }
            else if(command.equals("stop")){
                Me.isSending = false;
                if(Me.getSensor("Video").isConnected){
                    Intent i = new Intent("command");
                    i.putExtra("command","stop");
                    LocalBroadcastManager.getInstance(BluetoothService.this).sendBroadcast(i);
                }
                myToast("Session ended");
                try {
                    Me.writeSession(getBaseContext(),session);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Me.uploadSessions(getBaseContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Me.isSessionOnline = false;
            }
            else if(command.equals("tsConfig")){
                long ts = System.currentTimeMillis();
                long initTs = Long.parseLong(msg);
                Me.timeStampToAdd = ts-initTs;
            }
            else if(command.equals("sessionID")){
                Me.sessionId = Integer.parseInt(msg);
                if(Me.isNetworkAvailable(getBaseContext()) && (Me.sessionId!=-2)){
                    Me.isSessionOnline = true;
                }
            }
            else if(command.equals("Click")){
                if(Me.isSending){
                    long ts = System.currentTimeMillis()+Me.timeStampToAdd;
                    if(BluetoothService.session.isSessionOnline){
                        Log.e("ONLINE", msg);
                        SendValues.send(ts, Me.sessionId,Me.getSensor("Click").id, Integer.parseInt(msg), getBaseContext());
                    }
                    else{
                        BluetoothService.session.values.get(Me.getSensor("Click").id).put(ts,Integer.parseInt(msg));
                        Log.e("OFFLINE", msg);
                    }
                    Intent i = new Intent(getBaseContext(), SendToUnityService.class);
                    i.putExtra("message", msg);
                    startService(i);

                }
            }
            else if(command.equals("ClickSimulator")){
                if(msg.equals("start")){
                    Me.clickSending = true;
                }
                else if(msg.equals("stop")){
                    Me.clickSending = false;
                }
            }
        }

        @Override
        public void interrupt() {
            Log.e("BluetoothService","ct interrupted");
            try {
                mmSocket.close();
            } catch (IOException e) { }
            super.interrupt();
        }
    }
}
