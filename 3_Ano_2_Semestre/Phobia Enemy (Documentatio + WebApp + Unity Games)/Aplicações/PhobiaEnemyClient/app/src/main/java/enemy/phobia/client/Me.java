package enemy.phobia.client;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import enemy.phobia.client.JSON.Send.SendValues;

/**
 * Created by alex9 on 21/04/2016.
 */
public class Me {
    private static String username = "";
    private static int id = 0;
    private static ArrayList<Sensor> sensors = new ArrayList<>();
    private static boolean loggedIn = false;
    public static boolean isSending = false;
    public static String SC = "#";
    public static int managerId;
    public static int sessionId = 0;
    public static long timeStampToAdd = 0;
    public static ListSessions sessionslist;
    public static int idid = 0;
    public static boolean isSessionOnline = false;
    public static boolean isUploading = false;
    public static boolean clickSending = false;



    public static void login(String u, int i, ArrayList<Sensor> s){
        username = u;
        id = i;
        sensors = s;
        loggedIn = true;
        Log.e("Me","Logged in as "+ username);
    }
    public static void logout() {
        username = "";
        id = 0;
        sensors = new ArrayList<>();
        loggedIn = false;
        Log.e("Me","Logged out");
    }

    public static boolean isLoggedIn(){
        return loggedIn;
    }

    public static ArrayList<Sensor> getSensors(){return sensors;}
    public static Sensor getSensor(String m){
        for(Sensor s : sensors){
            if( s.name.equals(m)){
                return s;
            }
        }
        return null;
    }
    public static String getUsername(){return username;}
    public static int getId(){return id;}

    public static void writeSession(Context c,Session s) throws IOException {
        FileOutputStream fos = c.openFileOutput(Long.toString(s.id_local), Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(s);
        os.close();
        fos.close();
        Log.e("Write", "Session");
        Me.sessionslist.list.add(Long.toString(s.id_local));
        Me.writeListSessions(c);
    }
    public static Session readSession(Context c,String name)throws IOException, ClassNotFoundException{
        FileInputStream fis = c.openFileInput(name);
        ObjectInputStream is = new ObjectInputStream(fis);
        Session s = (Session) is.readObject();
        is.close();
        fis.close();
        Log.e("Read", "Session");
        return s;
    }

    public static void writeListSessions(Context c) throws IOException {
        FileOutputStream fos = c.openFileOutput("ListSessions", Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(Me.sessionslist);
        os.close();
        fos.close();
        Log.e("Write", "ListSession");
    }

    public static void readListSessions(Context c)throws IOException, ClassNotFoundException{
        FileInputStream fis = c.openFileInput("ListSessions");
        ObjectInputStream is = new ObjectInputStream(fis);
        ListSessions ls = (ListSessions) is.readObject();
        is.close();
        fis.close();
        Log.e("Read", "ListSession");
        sessionslist = ls;
        Log.e("SESSIONS", sessionslist.list.toString());
    }
    public static void uploadSessions(Context c) throws IOException, ClassNotFoundException {
        Log.e("Data","Uploading Sessions");
        while(isUploading);
        isUploading = true;
        for(Object o: sessionslist.list.toArray()){
            String s = (String)o;
            Session session = Me.readSession(c,(String)o);
            Me.idid = -2;
            Log.e("ASKING",Long.toString(session.id_local));
            SendValues.askId(session.id_local,c);
            while( Me.idid == -2){
            }
            if(Me.idid  == -1){
                Log.e("Upload", "Cant upload session");
                continue;
            }
            session.id = Me.idid;
            Log.e("ID ==== ",Integer.toString(Me.idid));
            if(session.hasVideo)
                Me.uploadVideo(c,"VID_" + Long.toString(session.id_local) + ".mp4",  session.id);
            for(int t:session.values.keySet()){
                for(long ts: session.values.get(t).keySet()) {
                    SendValues.send(ts,Me.sessionId,t,session.values.get(t).get(ts),c);
                }
            }
            File file = new File(s);
            file.delete();
            Me.sessionslist.list.remove(o);
        }
        Me.writeListSessions(c);
        isUploading = false;
    }
    public static boolean isNetworkAvailable(Context c) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void uploadVideo(Context c,String name,int id){
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");
            UploadFileToDropbox upload = new UploadFileToDropbox(c, DropboxActivity.dropbox,
                    Video_app.FILE_DIR, new File(mediaStorageDir.getPath() + File.separator +name),id);
            upload.execute();
    }


}
