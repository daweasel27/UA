package enemy.phobia.client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.*;
import com.dropbox.client2.session.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import enemy.phobia.client.JSON.JsonAuthObjectRequest;
import enemy.phobia.client.JSON.RequestQueueSingleton;
import enemy.phobia.client.JSON.TokenAuthentication;

/**
 * Created by alex9 on 21/04/2016.
 */
public class Login extends Activity {
    Button button_login;
    EditText editText_username;
    EditText editText_password;
    public static String token;
    SharedPreferences settings;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        try {
            Me.readListSessions(getBaseContext());
        } catch (Exception e) {
            e.printStackTrace();
            Me.sessionslist = new ListSessions();
        }
        try {
            Me.writeListSessions(getBaseContext());
        } catch (Exception e) {

        }

        Log.e("Sessions to Upload",Integer.toString(Me.sessionslist.list.size()));
        if(Me.isNetworkAvailable(getBaseContext())){
            logInDropBox();
            if(Me.sessionslist.list.size() != 0){
                Toast.makeText(getBaseContext(),"Uploading "+Integer.toString(Me.sessionslist.list.size())+ " sessions",Toast.LENGTH_LONG).show();
                class threadUpload extends Thread{
                    Context c;
                    public threadUpload(Context c){
                        this.c = c;
                    }
                    @Override
                    public void run(){
                        Looper.prepare();
                        try {
                            Me.uploadSessions(c);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                threadUpload tu = new threadUpload(getBaseContext());
                tu.start();
            }
        }

        settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        //layout views
        button_login = (Button) findViewById(R.id.button_login);
        editText_username = (EditText) findViewById(R.id.editText_username);
        editText_password = (EditText) findViewById(R.id.editText_password);


        //buttons listeners
        button_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.e("Joao: ", enemy.phobia.client.JSON.Configuration.apiPrefix);
                doLogin(editText_username.getText().toString(), editText_password.getText().toString());
            }
        });

        String user = settings.getString("user","");
        if(!user.equals(""))
            startActivity(new Intent(getBaseContext(),SkipLogin.class));


    }
    private void doLogin(String uu,String pass){
        JSONObject loginObj = new JSONObject();
        try {
            loginObj.put("username", uu);
            loginObj.put("password", pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonAuthObjectRequest req = new JsonAuthObjectRequest(
                Request.Method.POST,
                "/api/token-auth/",
                loginObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("VITOR", response.toString());
                            Login.token = response.get("token").toString();
                            Log.e("Joao: ", token);
                            TokenAuthentication.setToken(response.get("token").toString());
                            askForId();
                            //Intent i=new Intent(LoginActivity.this, MainActivity.class);
                            //startActivity(i);
                        } catch (JSONException e) {
                            Log.e("VITOR", e.toString());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("vitor", error.toString());
                    }
                }
        );

        RequestQueueSingleton.getInstance(getBaseContext()).addToRequestQueue(req);
    }
    protected void askForId(){
        JsonAuthObjectRequest req = new JsonAuthObjectRequest(
                Request.Method.GET,
                "/api/user-info/",
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int id = response.getInt("id");
                            String user = response.get("username").toString();
                            ArrayList<Sensor> sensors = new ArrayList<Sensor>();
                            JSONArray array =  response.getJSONArray("sensors");
                            for (int i = 0; i < array.length(); i++){
                                JSONObject obj = array.getJSONObject(i);
                                sensors.add(new Sensor(obj.getString("label"),obj.getInt("id")));
                            }
                            if(Camera.getNumberOfCameras() != 0) {
                                sensors.add(new Sensor("Video", 0));
                           }
                            if(!response.getBoolean("is_manager")){
                                Log.e("credenciais", response.toString());
                                Me.login(user, id, sensors);
                                loggedIn();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("vitor", error.toString());
                    }
                }
        );

        RequestQueueSingleton.getInstance(getBaseContext()).addToRequestQueue(req);
    }
    public void loggedIn(){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("user", Me.getUsername());
        editor.putInt("id", Me.getId());
        editor.commit();
        try {
            Login.writeSensors(getBaseContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent i = new Intent(getBaseContext(),Menu.class);
        startActivity(i);
    }
    public static void writeSensors(Context c) throws IOException {
        FileOutputStream fos = c.openFileOutput("sensors", Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(Me.getSensors());
        os.close();
        fos.close();
        Log.e("Write", "Sensors");
    }
    public static ArrayList<Sensor> readSensors(Context c)throws IOException, ClassNotFoundException{
        FileInputStream fis = c.openFileInput("sensors");
        ObjectInputStream is = new ObjectInputStream(fis);
        ArrayList<Sensor>  s = (ArrayList<Sensor>) is.readObject();
        is.close();
        fis.close();
        Log.e("Read", "Session");
        return s;
    }
    public void logInDropBox(){
        AndroidAuthSession session;
        AppKeyPair pair = new AppKeyPair(DropboxActivity.ACCESS_KEY, DropboxActivity.ACCESS_SECRET);

        SharedPreferences prefs = getSharedPreferences(DropboxActivity.DROPBOX_NAME, 0);
        String key = prefs.getString(DropboxActivity.ACCESS_KEY, null);
        String secret = prefs.getString(DropboxActivity.ACCESS_SECRET, null);

        if (key != null && secret != null) {
            AccessTokenPair token = new AccessTokenPair(key, secret);
            session = new AndroidAuthSession(pair, Session.AccessType.APP_FOLDER, token);
            DropboxActivity.isLoggedIn = true;
        } else {
            session = new AndroidAuthSession(pair, Session.AccessType.APP_FOLDER);
            //Toast.makeText(getBaseContext(),"You are not logged in with Dropbox",Toast.LENGTH_LONG);
            DropboxActivity.isLoggedIn = false;
        }
        DropboxActivity.dropbox = new DropboxAPI<>(session);
    }
}
