package enemy.phobia.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.Toast;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import com.dropbox.client2.session.TokenPair;

public class DropboxActivity extends Activity implements View.OnClickListener {

    public static DropboxAPI<AndroidAuthSession> dropbox;
    public final static String DROPBOX_NAME = "dropbox_prefs";
    public final static String ACCESS_KEY ="fpbjn9ly2dt9nka";
    public final static String ACCESS_SECRET ="ledh5egkbstl3ds";
    public static boolean isLoggedIn;
    private Button logIn;
    private Button go_to;
    private Button connect_to_manager;
    private LinearLayout container;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dropbox);

        logIn = (Button) findViewById(R.id.dropbox_login);
        logIn.setOnClickListener(this);

        loggedIn(false);
        AndroidAuthSession session;
        AppKeyPair pair = new AppKeyPair(ACCESS_KEY, ACCESS_SECRET);

        SharedPreferences prefs = getSharedPreferences(DROPBOX_NAME, 0);
        String key = prefs.getString(ACCESS_KEY, null);
        String secret = prefs.getString(ACCESS_SECRET, null);

        if (key != null && secret != null) {
            AccessTokenPair token = new AccessTokenPair(key, secret);
            session = new AndroidAuthSession(pair, AccessType.APP_FOLDER, token);
            loggedIn(true);
        } else {
            session = new AndroidAuthSession(pair, AccessType.APP_FOLDER);
            loggedIn(false);
        }
        dropbox = new DropboxAPI<>(session);
    }

    @Override
    protected void onResume() {
        super.onResume();

        AndroidAuthSession session = dropbox.getSession();
        if (session.authenticationSuccessful()) {
            try {
                session.finishAuthentication();
                TokenPair tokens = session.getAccessTokenPair();
                SharedPreferences prefs = getSharedPreferences(DROPBOX_NAME, 0);
                Editor editor = prefs.edit();
                editor.putString(ACCESS_KEY, tokens.key);
                editor.putString(ACCESS_SECRET, tokens.secret);
                editor.commit();
                loggedIn(true);
            } catch (IllegalStateException e) {
                Toast.makeText(this, "Error during Dropbox authentication",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void loggedIn(boolean isLogged) {
        isLoggedIn = isLogged;
        logIn.setText(isLogged ? "Log out" : "Log in");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dropbox_login:
                if (isLoggedIn) {
                    dropbox.getSession().unlink();
                    Log.e("x","nao deu");
                    loggedIn(false);
                } else {
                    Log.e("x","nao deu");
                    dropbox.getSession().startAuthentication(DropboxActivity.this);
                }
                break;
            default:
                break;
        }
    }
}

