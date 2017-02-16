package ua.alexandre.masterpe;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by alex9 on 21/05/2016.
 */
public class Click extends Activity {
    public static long i = 0;
    private long dif = 0;
    Thread_Send ts;
    Thread_Control tc;
    long starttime = 0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.click_simulator);
        BluetoothService.sendAll("ClickSimulator" + Me.SC + "start");
        ts = new Thread_Send();
        ts.start();
        tc = new Thread_Control();
        tc.start();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.click_simulator);
        ImageView imageView = (ImageView)findViewById(R.id.image);
        assert imageView != null;
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageView view = (ImageView) v;
                        view.getDrawable().clearColorFilter();
                        view.invalidate();
                        long tmp = System.currentTimeMillis();
                        i = tmp - starttime;
                        starttime = tmp;
                        try {
                            i = 20000 / (i);
                        }
                        catch(Exception e){

                        }
                        if(i >= 200){
                            i = 200;
                       }
                        break;
                    }
                }
                return true;
            }
        });
    }
    @Override
    public void finish(){
        BluetoothService.sendAll("ClickSimulator" + Me.SC + "stop");
        ts.interrupt();
        tc.interrupt();
        super.finish();
    }
    class Thread_Send extends Thread{
        public Thread_Send(){

        }
        @Override
        public void run(){
            long ts = 0;
            while(!isInterrupted()){
                long ts2 = System.currentTimeMillis();
                if(ts2 > ts + 500){
                    ts = ts2;
                    Log.e("IIIIIIIIIII", Long.toString(i));
                    for(Client cl : BluetoothService.getClientList()){
                        if(!cl.getSensor("Video").isConnected){
                            BluetoothService.sendTo(cl.address, "Click" + Me.SC + Long.toString(i));

                        }
                    }
                }
            }
        }
        @Override
        public void interrupt(){
            super.interrupt();
        }
    }
    class Thread_Control extends Thread{
        public Thread_Control(){

        }
        @Override
        public void run(){
            long ts = 0;
            while(!isInterrupted()){
                long ts2 = System.currentTimeMillis();
                if(ts2 > starttime +1000){
                    i=0;
                }
            }
        }
        @Override
        public void interrupt(){
            super.interrupt();
        }
    }
}
