package enemy.phobia.client;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import Bio.Library.namespace.BioLib;
import java.util.ArrayList;

/**
 * Created by alex9 on 20/04/2016.
 */
public class VjListActivity extends Activity {
    private BioLib lib = null;
    private ListView mListView;
    private VjListAdapter mAdapter;
    private ArrayList<BluetoothDevice> mDeviceList;
    public static  BluetoothDevice deviceSelected;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paired_devices);
        mDeviceList	= getIntent().getExtras().getParcelableArrayList("vj.list");
        mListView = (ListView) findViewById(R.id.lv_paired);
        mAdapter = new VjListAdapter(this);
        mAdapter.setData(mDeviceList);
        mAdapter.setListener(   new VjListAdapter.OnPairButtonClickListener() {
            @Override
            public void onItemClick(int  position) {
                deviceSelected = mDeviceList.get(position);
                Intent i = new Intent(getBaseContext(),VitalJacket_Service.class);
                startService(i);
            }

        });

        mListView.setAdapter(mAdapter);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
