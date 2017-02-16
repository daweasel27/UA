package enemy.phobia.client;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by alex9 on 21/04/2016.
 */
public class ServingManager extends Activity {  Button button_login;
    TextView textView_welcome;
    Button button_startStop;
    BluetoothAdapter mBluetoothAdapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serving_manager);
        //layout views
        textView_welcome = (TextView) findViewById(R.id.textView_welcome);
        button_startStop = (Button) findViewById(R.id.button_startStop);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //Check if device supports bluetooth
        if(mBluetoothAdapter == null){
            Toast.makeText(getBaseContext(),"This device doesn't support Bluetooth",Toast.LENGTH_LONG).show();
            startActivity(new Intent(getBaseContext(), Menu.class));
        }

        //Turn on Bluetooth
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }

        //Bluetooth Visible
        if(mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
            startActivity(discoverableIntent);
        }
        //buttons listeners
        button_startStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(button_startStop.getText().equals("Start")) {
                    Intent i = new Intent(getBaseContext(), BluetoothService.class);
                    startService(i);
                    button_startStop.setText("Stop");
                }
                else if(button_startStop.getText().equals("Stop")){
                    Intent i = new Intent(getBaseContext(), BluetoothService.class);
                    stopService(i);
                    button_startStop.setText("Start");
                }
            }
        });

        textView_welcome.setText("Welcome " + Me.getUsername());
    }
    @Override
    public void finish(){
        Intent i = new Intent(getBaseContext(), BluetoothService.class);
        stopService(i);
        button_startStop.setText("Start");
        super.finish();
    }
}
