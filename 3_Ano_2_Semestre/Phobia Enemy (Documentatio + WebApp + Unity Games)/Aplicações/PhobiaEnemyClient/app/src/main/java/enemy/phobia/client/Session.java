package enemy.phobia.client;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by alex9 on 16/05/2016.
 */
public class Session implements java.io.Serializable{
    long id_local = 0;
    int id;
    int id_manager;
    boolean hasVideo = false;
    boolean isSessionOnline = false;

    HashMap<Integer,HashMap<Long,Integer>> values;

    public Session(long start,int mana){
        this.id_local = start;
        id_manager = mana;
        values = new HashMap<>();

    }
}
