package ua.alexandre.masterpe;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by alex9 on 18/04/2016.
 */
public class Client implements Parcelable ,java.io.Serializable{
    public int id;
    public String username;
    public String address;
    public boolean vj;
    public boolean video;
    public ArrayList<Sensor> sensors;
    public BluetoothService.ConnectedThread r;


    public Client(BluetoothService.ConnectedThread r,String add){
        this.id = 0;
        this.username = "Waiting Information";
        this.address = add;
        this.vj = false;
        this.video = false;
        this.sensors = new ArrayList<>();
        this.r = r;
    }

    public Sensor getSensor(String m){
        for(Sensor s: sensors){
            if(s.name.equals(m)){
                return s;
            }
        }
        return null;
    }
    protected Client(Parcel in) {
        id = in.readInt();
        username = in.readString();
        address = in.readString();
        vj = in.readByte() != 0;
        video = in.readByte() != 0;
    }

    public static final Creator<Client> CREATOR = new Creator<Client>() {
        @Override
        public Client createFromParcel(Parcel in) {
            return new Client(in);
        }

        @Override
        public Client[] newArray(int size) {
            return new Client[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(username);
        dest.writeString(address);
        dest.writeByte((byte) (vj ? 1 : 0));
        dest.writeByte((byte) (video ? 1 : 0));
    }
}
