package enemy.phobia.client.JSON.Send;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import enemy.phobia.client.JSON.JsonAuthArrayRequest;
import enemy.phobia.client.JSON.JsonAuthObjectRequest;
import enemy.phobia.client.JSON.RequestQueueSingleton;
import enemy.phobia.client.Me;

/**
 * Created by Jota on 20/04/2016.
 */
public class SendValues {
    public static void send(long timestamp,int ID_Session, int ID_sensor, int Value, Context ct){
        JSONObject obj = new JSONObject();
        try {
            obj.put("session", ID_Session);
            obj.put("time", timestamp);
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

    public static void askId(long ts, Context ct){
       Log.e("TIMESTAMP",Long.toString(ts));
        JsonAuthArrayRequest req = new JsonAuthArrayRequest(
                "/api/sessions?time=" + Long.toString(ts),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Me.sessionId = response.getJSONObject(0).getInt("id");
                            Me.idid = response.getJSONObject(0).getInt("id");
                        } catch (JSONException e) {
                            Me.sessionId = -1;
                            Me.idid = -1;
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Me.sessionId = -1;
                        Me.idid = -1;
                    }
                }
        );

        RequestQueueSingleton.getInstance(ct).addToRequestQueue(req);
    }
    public static void sendURL(int sessionId,String url, Context ct){
        JSONObject obj = new JSONObject();
        try {
            obj.put("video_url", url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonAuthObjectRequest req = new JsonAuthObjectRequest(
                Request.Method.PATCH,
                "/api/sessions/" + Integer.toString(sessionId)+"/",
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
