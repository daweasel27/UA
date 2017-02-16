package enemy.phobia.client;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
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
    Button button_serve_manager, button_dropbox;
    BluetoothAdapter mBluetoothAdapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //Turn on Bluetooth
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }

        //layout views
        textView_welcome = (TextView) findViewById(R.id.textView_welcome);
        button_serve_manager = (Button) findViewById(R.id.button_serve_manager);
        button_dropbox = (Button) findViewById(R.id.button_drop);

        //buttons listeners
        button_serve_manager.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(),ServingManager.class));
            }
        });

        button_dropbox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(),DropboxActivity.class));
            }
        });


        textView_welcome.setText("Welcome " + Me.getUsername());
    }
}
