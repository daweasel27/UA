package ua.alexandre.masterpe;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import ua.alexandre.masterpe.JSON.Send.SendValues;

/**
 * Created by alex9 on 21/04/2016.
 */
public class Me {
    private static String username = "";
    private static int id = 0;
    private static boolean loggedIn = false;
    public static String SC = "#";
    public static ListSessions sessionslist;
    public static Activity thisActivity = null;


    public static void myToast()
    {
        Toast.makeText(thisActivity, "message", Toast.LENGTH_SHORT).show();
    }
    public static void login(String u, int i){
        username = u;
        id = i;
        loggedIn = true;
        Log.e("Me", "Login as " + username);
    }
    public static void logout() {
        username = "";
        id = 0;
        loggedIn = false;
        Log.e("Me","Logout");
    }

    public static boolean isLoggedIn(){
        return loggedIn;
    }
    public static String getUsername(){return username;}
    public static int getId(){return id;}

    public static void writeSession(Context c,Session s) throws IOException {
        FileOutputStream fos = c.openFileOutput(Long.toString(s.id_local), Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(s);
        os.close();
        fos.close();
        Log.e("Data management", "Session Saved");
        Me.sessionslist.list.add(Long.toString(s.id_local));
        Me.writeListSessions(c);
    }
    public static Session readSession(Context c,String name)throws IOException, ClassNotFoundException{
        FileInputStream fis = c.openFileInput(name);
        ObjectInputStream is = new ObjectInputStream(fis);
        Session s = (Session) is.readObject();
        is.close();
        fis.close();
        Log.e("Data management", "Session Loaded");
        return s;
    }

    public static void writeListSessions(Context c) throws IOException {
        FileOutputStream fos = c.openFileOutput("ListSessions", Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(Me.sessionslist);
        os.close();
        fos.close();
        Log.e("Data management", "Sessions List Saved");
    }

    public static void readListSessions(Context c)throws IOException, ClassNotFoundException{
        FileInputStream fis = c.openFileInput("ListSessions");
        ObjectInputStream is = new ObjectInputStream(fis);
        ListSessions ls = (ListSessions) is.readObject();
        is.close();
        fis.close();
        Log.e("Data management", "Sessions List Loaded");
        sessionslist = ls;
    }
    public static void uploadSessions(Context c) throws IOException, ClassNotFoundException {
        for(Object o: sessionslist.list.toArray()){
            String s = (String)o;
            Session session = Me.readSession(c,(String)o);
            Manager.sessionId = -2;
            SendValues.createSession(session.id_manager,session.members,session.id_local,c);
            while(Manager.sessionId == -2);
            for(long t:session.events.keySet()){
                SendValues.event(Manager.sessionId,session.events.get(t),t,c);
            }
            File file = new File(s);
            file.delete();
        }
        sessionslist = new ListSessions();
        Me.writeListSessions(c);
    }
    public static boolean isNetworkAvailable(Context c) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
