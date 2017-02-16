package ua.alexandre.masterpe.JSON.Send;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import ua.alexandre.masterpe.Client;
import ua.alexandre.masterpe.JSON.JsonAuthObjectRequest;
import ua.alexandre.masterpe.JSON.RequestQueueSingleton;
import ua.alexandre.masterpe.Manager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Jota on 20/04/2016.
 */
public class SendValues {
    public static void send(int ID_Session, int ID_sensor, int Value, Context ct){
        JSONObject obj = new JSONObject();
        try {
            obj.put("session", ID_Session);

            Calendar now = Calendar.getInstance();
            int hour = now.get(Calendar.HOUR_OF_DAY);
            int min = now.get(Calendar.MINUTE);
            int sec = now.get(Calendar.SECOND);

            obj.put("time", hour + ":" + min + ":" + sec);
            obj.put("value", Value);
            obj.put("sensor_id", ID_sensor);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonAuthObjectRequest req = new JsonAuthObjectRequest(
                Request.Method.POST,
                "/api/sensor-data/",
                obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("vitor", response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("vitor", error.toString());
                    }
                }
        );

        RequestQueueSingleton.getInstance(ct).addToRequestQueue(req);
    }

    public static void createSession(final int ID_Manager, final ArrayList<Integer> members,final long ts, final Context ct){
        JSONObject obj = new JSONObject();
        try {
            obj.put("manager", ID_Manager);
            JSONArray ja = new JSONArray();
            for(Integer c :members){
                ja.put(c);
            }
            obj.put("members", ja);
            obj.put("start_time",ts);
            Log.e("vitor",obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonAuthObjectRequest req = new JsonAuthObjectRequest(
                Request.Method.POST,
                "/api/sessions/",
                obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("vitor", response.toString());
                        try {
                            Manager.sessionId = response.getInt("id");
                        } catch (JSONException e) {
                            Manager.sessionId = -1;
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
        RequestQueueSingleton.getInstance(ct).addToRequestQueue(req);
    }
    public static void event(int session,String note,long ts,Context ct){
        JSONObject obj = new JSONObject();
        try {
            obj.put("session", session);
            obj.put("time", ts);
            obj.put("notes", note);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonAuthObjectRequest req = new JsonAuthObjectRequest(
                Request.Method.POST,
                "/api/events/",
                obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("vitor", response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("vitor", error.toString());
                    }
                }
        );

        RequestQueueSingleton.getInstance(ct).addToRequestQueue(req);
    }
}
