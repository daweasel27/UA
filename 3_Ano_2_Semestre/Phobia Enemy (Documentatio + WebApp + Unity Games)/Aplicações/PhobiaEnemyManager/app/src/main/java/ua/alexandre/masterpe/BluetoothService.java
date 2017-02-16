package ua.alexandre.masterpe;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import ua.alexandre.masterpe.JSON.Send.SendValues;


/**
 * Created by alex9 on 21/04/2016.
 */
public class BluetoothService extends Service{
    //boolean onSession;
    //boolean connectedToManager;
    BluetoothAdapter mBluetoothAdapter;
    Handler handler;
    public static HashMap<String,Client> clients = new HashMap<>();

    @Override
    public void onCreate() {
        Log.e("BluetoothService", "Service created");

        handler = new Handler(BluetoothService.this.getMainLooper());
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        clients = new HashMap<>();

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
                parts = m.split(Me.SC);
                command = parts[0];
                msg = parts[1];
            }catch (Exception e){e.printStackTrace();}
            if(command.equals("connect")) {
                connectTo(msg);
            }
            if(command.equals("disconnect")){
                clients.get(msg).r.interrupt();
                clients.remove(msg);
                myRefreshClientsConnectedList();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for(Client c : clients.values()){
            c.r.interrupt();
        }
        Log.e("BluetoothService", "Service stopped");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void connectTo(String m){
        ConnectedThread ct = new ConnectedThread(m);
        ct.start();
        clients.put(m, new Client(ct,m));
        myRefreshClientsConnectedList();
    }

    public static void sendTo(String add,String m){
        Client a = getClient(add);
        a.r.send(m);
    }
    public static void sendAll(String m){
        for(Client a :  clients.values()){
            a.r.send(m);
        }
    }

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
                Toast.makeText(getBaseContext(), m, Toast.LENGTH_LONG).show();
            }
        });
    }
    public void myRefreshClientsConnectedList(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Manager.refreshClientsConnectedList();
            }
        });
    }
    public void myRefreshClientManage(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                ClientManage.refresh();
            }
        });
    }
    public static ArrayList<Client> getClientList(){
        ArrayList<Client> r = new ArrayList<>();
        for(Client c : clients.values()){
            r.add(c);
        }
        return r;
    }
    public static ArrayList<Integer> getClientIds(){
        ArrayList<Integer> r = new ArrayList<>();
        for(Client c : clients.values()){
            r.add(c.id);
        }
        return r;
    }

    public static Client getClient(String add){
        return clients.get(add);
    }

    public class ConnectedThread extends Thread {
        private BluetoothSocket socket;
        private OutputStreamWriter os;
        private InputStream is;
        private String address;

        public ConnectedThread(String a){
            address = a;
        }

        @Override
        public void run() {
            Log.e("BluetoothService", "Connecting to " + mBluetoothAdapter.getRemoteDevice(address).getName());
            UUID uuid = UUID.fromString("3b7add25-6ebd-4407-a645-66205b76e450");
            try {
                socket = mBluetoothAdapter.getRemoteDevice(address).createRfcommSocketToServiceRecord(uuid);
                socket.connect();
            } catch (IOException e) {
                e.printStackTrace();
                clients.remove(address);
                myRefreshClientsConnectedList();
                myToast("Can't connect to " + mBluetoothAdapter.getRemoteDevice(address).getName());
                return;
            }
            Log.e("BluetoothService", "Connected to " + mBluetoothAdapter.getRemoteDevice(address).getName());
            myToast("Connected to " + mBluetoothAdapter.getRemoteDevice(address).getName());
            try {
                os = new OutputStreamWriter(socket.getOutputStream());
                is = socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
                    clients.remove(address);
                    myRefreshClientsConnectedList();
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
                for (int i = 0; i < m.length; i++) {
                    if (m[i].length() != 0)
                        received(m[i]);
                }
            }
        }

        public void send(String m) {
            Log.e("BluetoothService", "Sending: " + m);
            try {
                os.write(m + "\n\n");
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void received(String m) {
            Log.e("BluetoothService", "Received: " + m);
            String[] parts;
            String command = "";
            String msg = "";
            try {
                parts = m.split(Me.SC);
                command = parts[0];
                msg = parts[1];
            }catch (Exception e){e.printStackTrace();}
            if(command.equals("username")) {
                clients.get(address).username = msg;
                Log.e("USERNAME",clients.get(address).username);
                myRefreshClientsConnectedList();
            }
            else if(command.equals("id")) {
                clients.get(address).id = Integer.parseInt(msg);
                Log.e("ID",Integer.toString(clients.get(address).id));
                myRefreshClientsConnectedList();
            }
            if(command.equals("sensor")){
                String[] p;
                String type = "";
                String value = "";
                try {
                    p = msg.split(";");
                    type = p[0];
                    value = p[1];
                }catch (Exception e){e.printStackTrace();}
                getClient(address).sensors.add(new Sensor(type, Integer.parseInt(value)));
            }
            else if(command.equals("sensorOn")) {
                clients.get(address).getSensor(msg).connected(true);
                Log.e("AAAAAAAA",m);
                //clients.get(address).getSensor(msg).tv = "Disconnected";
                myRefreshClientManage();
            }
            else if(command.equals("sensorOff")){
                clients.get(address).getSensor(msg).connected(false);
                clients.get(address).getSensor(msg).tv = "";
                Log.e("AAAAAAAA",m);
                myRefreshClientManage();
            }
        }


        @Override
        public void interrupt() {
            Log.e("BluetoothService", "ct interrupted");
            try {
                socket.close();
            } catch (IOException e) {
            }
            super.interrupt();
        }
    }
}
