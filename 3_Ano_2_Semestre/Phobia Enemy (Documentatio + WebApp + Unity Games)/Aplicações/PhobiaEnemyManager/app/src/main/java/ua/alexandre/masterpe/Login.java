package ua.alexandre.masterpe;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import ua.alexandre.masterpe.JSON.JsonAuthObjectRequest;
import ua.alexandre.masterpe.JSON.RequestQueueSingleton;
import ua.alexandre.masterpe.JSON.TokenAuthentication;

/**
 * Created by alex9 on 21/04/2016.
 */
public class Login extends Activity{
    Button button_login;
    EditText editText_username;
    EditText editText_password;
    public static String token;
    SharedPreferences settings;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Me.thisActivity = this;
        settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        try {
            Me.readListSessions(getBaseContext());
        } catch (Exception e) {
            e.printStackTrace();
            Me.sessionslist = new ListSessions();
        }
        try {
            Me.writeListSessions(getBaseContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.e("Sessions to upload",Integer.toString(Me.sessionslist.list.size()));
        if(Me.sessionslist.list.size()!=0 && Me.isNetworkAvailable(getBaseContext())){
            Toast.makeText(getBaseContext(),"Uploading "+Integer.toString(Me.sessionslist.list.size())+ " sessions",Toast.LENGTH_LONG).show();
            class threadUpload extends Thread{
                @Override
                public void run(){
                    Looper.prepare();
                    try {
                        Me.uploadSessions(getBaseContext());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            threadUpload tu = new threadUpload();
            tu.start();
        }

        //layout views
        button_login = (Button) findViewById(R.id.button_login);
        editText_username = (EditText) findViewById(R.id.editText_username);
        editText_password = (EditText) findViewById(R.id.editText_password);

        String user = settings.getString("user","");
        if(!user.equals(""))
            startActivity(new Intent(getBaseContext(),SkipLogin.class));

        //buttons listeners
        button_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.e("Login Site", ua.alexandre.masterpe.JSON.Configuration.apiPrefix);
                doLogin(editText_username.getText().toString(),editText_password.getText().toString());
            }
        });
    }
    public void loggedIn(){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("user", Me.getUsername());
        editor.putInt("id", Me.getId());
        editor.commit();
        Toast.makeText(getBaseContext(), "You are connected to website", Toast.LENGTH_LONG).show();
        if(Me.sessionslist.list.size() != 0){
            class threadUpload extends Thread{
                @Override
                public void run(){
                    Looper.prepare();
                    Toast.makeText(getBaseContext(),"Uploading "+ Me.sessionslist.list.size() + " sessions",Toast.LENGTH_LONG).show();
                    try {
                        Me.uploadSessions(getBaseContext());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getBaseContext(), "Sessions uploaded", Toast.LENGTH_LONG).show();
                }
            }
            threadUpload tu = new threadUpload();
            tu.start();
        }

        Intent i = new Intent(getBaseContext(),Menu.class);
        startActivity(i);
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
                            //Log.e("VITOR", response.toString());
                            Login.token = response.get("token").toString();
                            //Log.e("Joao: ", token);
                            TokenAuthentication.setToken(response.get("token").toString());
                            askForId();
                            //Intent i=new Intent(LoginActivity.this, MainActivity.class);
                            //startActivity(i);
                        } catch (JSONException e) {
                            //Log.e("VITOR", e.toString());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error", error.toString());
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
                            if(response.getBoolean("is_manager")){
                                Log.e("credenciais", response.toString());
                                Me.login(user, id);
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
                        Log.e("error", error.toString());
                    }
                }
        );

        RequestQueueSingleton.getInstance(getBaseContext()).addToRequestQueue(req);
    }
}
