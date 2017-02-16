package enemy.phobia.client;

import java.util.ArrayList;

/**
 * Created by alex9 on 16/05/2016.
 */
public class ListSessions implements java.io.Serializable {
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
