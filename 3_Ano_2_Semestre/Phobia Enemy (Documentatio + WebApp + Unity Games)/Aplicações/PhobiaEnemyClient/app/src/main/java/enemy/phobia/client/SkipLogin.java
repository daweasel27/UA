package enemy.phobia.client;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by alex9 on 16/05/2016.
 */
public class SkipLogin extends Activity{
    Button button_yes,button_logout;
    TextView textView_user;
    SharedPreferences settings;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skip_login);

        //layout views
        button_yes = (Button) findViewById(R.id.button_yes);
        button_logout = (Button) findViewById(R.id.button_logout);
        textView_user = (TextView) findViewById(R.id.textView_user);
        settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        final String user = settings.getString("user","");
        final int id = settings.getInt("id",-1);
        textView_user.setText("Are you "+ user+"?");
        try {
            Me.login(user, id,Login.readSensors(getBaseContext()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        button_yes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), Menu.class));
            }
        });
        button_logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("user","");
                editor.putInt("id",-1);
                editor.commit();
                Me.logout();
                startActivity(new Intent(getBaseContext(), Login.class));
            }
        });


    }
}
