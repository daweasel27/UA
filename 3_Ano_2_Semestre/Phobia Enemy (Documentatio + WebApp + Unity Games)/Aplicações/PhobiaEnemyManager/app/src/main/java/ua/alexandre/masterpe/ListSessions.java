package ua.alexandre.masterpe;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by alex9 on 13/05/2016.
 */
public class ListSessions implements Serializable{
    ArrayList<String> list;
    public ListSessions(){
        list = new ArrayList<>();
    }
    @Override
    public String toString(){
       String re = "";
        for (Object a:list.toArray() ) {
            re = re + ","+(String)a;
        }
        return re;
    }
}
