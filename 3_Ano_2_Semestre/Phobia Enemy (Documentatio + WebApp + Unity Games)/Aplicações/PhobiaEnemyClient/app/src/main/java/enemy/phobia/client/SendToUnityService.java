package enemy.phobia.client;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class SendToUnityService extends Service {

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String m = "";
        try {
            m = intent.getExtras().getString("message");
        }catch (Exception e){}
        int HR = Integer.parseInt(m);

        Intent sendIntent = new Intent();
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_FROM_BACKGROUND | Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendIntent.setAction("sendtounityservice.sendintent.test.com.myapplication.IntentToUnity");
        sendIntent.putExtra("Joao", Integer.toString(HR));
        sendBroadcast(sendIntent);
        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}