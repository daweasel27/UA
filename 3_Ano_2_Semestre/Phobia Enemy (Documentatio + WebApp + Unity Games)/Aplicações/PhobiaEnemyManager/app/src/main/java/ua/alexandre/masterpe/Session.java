package ua.alexandre.masterpe;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by alex9 on 13/05/2016.
 */
public class Session implements java.io.Serializable{
    long id_local;
    int id_manager;
    ArrayList<Integer> members;
    HashMap<Long,String> events;
    boolean isSessionOnline;

    public Session(long start, ArrayList<Integer> m,int manager){
        this.id_local = start;
        events = new HashMap<>();
        members = m;
        id_manager = manager;
        isSessionOnline = false;

    }
}
