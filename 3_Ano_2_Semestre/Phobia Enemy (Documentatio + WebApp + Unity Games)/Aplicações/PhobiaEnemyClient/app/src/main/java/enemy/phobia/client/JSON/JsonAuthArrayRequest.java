package enemy.phobia.client.JSON;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.Map;

/**
 * Created by Jota on 20/04/2016.
 */
public class JsonAuthArrayRequest extends JsonArrayRequest {
    public JsonAuthArrayRequest(String url,
                                Response.Listener<JSONArray> listener,
                                Response.ErrorListener errorListener) {
        super(Configuration.apiPrefix + url, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return TokenAuthentication.getHeaders();
    }
}
