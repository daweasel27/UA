package ua.alexandre.masterpe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by alex9 on 21/04/2016.
 */
public class Menu extends Activity{
    TextView textView_welcome;
    Button button_be_manager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        //layout views
        textView_welcome = (TextView) findViewById(R.id.textView_welcome_menu);
        button_be_manager = (Button) findViewById(R.id.button_be_manager);

        //buttons listeners
        button_be_manager.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(),Manager.class));
            }
        });

        textView_welcome.setText("Welcome " + Me.getUsername());
    }
}
