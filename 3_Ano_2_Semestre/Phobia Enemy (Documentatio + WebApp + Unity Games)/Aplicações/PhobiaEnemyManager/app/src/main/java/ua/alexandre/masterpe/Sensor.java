package ua.alexandre.masterpe;

import android.os.Handler;

/**
 * Created by alex9 on 23/04/2016.
 */
public class Sensor {
    int id;
    String name;
    boolean isConnected;
    String tv;

    public Sensor(String n, int i){
        this.id = i;
        this.name = n;
        isConnected = false;
        this.tv = "";
    }
    public void connected(boolean a){
        isConnected = a;
    }
}
