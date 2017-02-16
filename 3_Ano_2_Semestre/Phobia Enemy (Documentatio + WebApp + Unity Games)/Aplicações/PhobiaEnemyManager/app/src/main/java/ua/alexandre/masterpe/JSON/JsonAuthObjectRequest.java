package ua.alexandre.masterpe.JSON;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Map;


/**
 * Created by Jota on 20/04/2016.
 */
public class JsonAuthObjectRequest extends JsonObjectRequest {
    public JsonAuthObjectRequest(int method,
                                 String url,
                                 JSONObject jsonRequest,
                                 Response.Listener listener,
                                 Response.ErrorListener errorListener) {
        super(method, Configuration.apiPrefix + url, jsonRequest, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return TokenAuthentication.getHeaders();
    }
}
