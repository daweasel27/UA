package enemy.phobia.client;

/**
 * Created by alex9 on 23/04/2016.
 */
public class Sensor implements java.io.Serializable{
    int id;
    String name;
    boolean isConnected;

    public Sensor(String n, int i){
        this.id = i;
        this.name = n;
        isConnected = false;
    }
}
