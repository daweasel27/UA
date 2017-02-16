package ua.alexandre.masterpe.JSON;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jota on 20/04/2016.
 */
public class TokenAuthentication {
    private static Map headers = new HashMap();

    public static void setToken(String token) {
        headers.put("Authorization", "Token " + token);
    }

    public static Map<String, String> getHeaders() {
        return headers;
    }
}
