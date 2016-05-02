package com.iAttend;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.util.HashMap;

public class HomeActivity extends Activity implements BeaconConsumer{
    private BeaconManager beaconManager;
    //protected static final String TAG = "MonitoringActivity";

    private TextView _welcomeText;
    private TextView _timeText;
    private TextView _classText;
    private TextView _sessionText;
    private TextView _locationText;

    private Button _buttonClicker;
    private Button _clickAttend;
    private Button _refresh;
    private TextView _detectedLocation;

    private AudioManager myAudioManager;

    SessionManager session;
    LocationPreference locationPreference;
    CurrentClass currentClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        setContentView(R.layout.activity_home);
        final Context context = this;

        session = new SessionManager(getApplicationContext());


        HashMap<String,String> user = session.getUserDetails();


        String user_name = user.get(SessionManager.KEY_NAME);
        //String id =user.get(SessionManager.KEY_ID);
        final String email = user.get(SessionManager.KEY_EMAIL);
        //String role = user.get(SessionManager.KEY_ROLE);



        _welcomeText=(TextView)findViewById(R.id.welcomeText);
        _welcomeText.setText("Welcome, " + user_name);

        _clickAttend=(Button)findViewById(R.id.attendClick);
        _refresh =(Button) findViewById(R.id.refresh);
        new CheckCourse(this,0).execute(email);
        _refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CheckCourse(context, 0).execute(email);
            }
        });



        beaconManager = BeaconManager.getInstanceForApplication(HomeActivity.this);
        beaconManager
                .getBeaconParsers()
                .add(new BeaconParser()
                        .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(HomeActivity.this);
        _buttonClicker =(Button)findViewById(R.id.getLocation);
        _buttonClicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("Start Bluetooth");
                onBeaconServiceConnect();

            }
        });

        Thread t = new Thread(){
            @Override
            public void run(){
                try{
                    while(!isInterrupted()){

                        new GetCurrentClass(context).execute(email);
                        currentClass = new CurrentClass(getApplicationContext());
                        currentClass.END_CLASS();
                        Thread.sleep(600000);
                    }

                }
                catch(InterruptedException e){
                    e.printStackTrace();

                }
            }
        };
        t.start();
    }
        @Override
        protected void onDestroy() {
            super.onDestroy();
            beaconManager.unbind(this);

        }


    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(final Region region) {
                Runnable changeText = new Runnable()
                {
                    public void run()
                    {
                        _detectedLocation = (TextView)findViewById(R.id.detectLocation);
                        _detectedLocation.setText("Detected Location: "+region.getUniqueId());
                        locationPreference = new LocationPreference(getApplicationContext());
                        HashMap<String,String> location = locationPreference.getlocationDetails();

                        String locationUUID=location.get(locationPreference.KEY_UUID);
                        String locationName=location.get(locationPreference.KEY_NAME);
                        System.out.println(locationUUID);
                        System.out.println(region.getId1());

                        if (locationUUID.equals(region.getId1().toString().toUpperCase())){
                            _clickAttend.setEnabled(true);
                            _clickAttend.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(getApplicationContext(),"You attended the class!",Toast.LENGTH_SHORT).show();
                                    _clickAttend.setEnabled(false);
                                    Toast.makeText(getApplicationContext(),"Silent mode activated",Toast.LENGTH_LONG).show();
                                    myAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                                    currentClass = new CurrentClass(getApplicationContext());
                                    currentClass.START_CLASS();
                                    HashMap<String,String> class_detail = currentClass.getClassDetails();
                                    String class_id = class_detail.get(CurrentClass.CLASS_CODE);


                                    session = new SessionManager(getApplicationContext());

                                    HashMap<String,String> user = session.getUserDetails();

                                    String user_name = user.get(SessionManager.KEY_NAME);
                                    //String id =user.get(SessionManager.KEY_ID);
                                    final String email = user.get(SessionManager.KEY_EMAIL);
                                    new AttendClass().execute(email,class_id);

                                }
                            });
                        }
                        Toast.makeText(getApplicationContext(),"You are now in "+region.getUniqueId(),Toast.LENGTH_LONG).show();
                    }
                };
                runOnUiThread(changeText);
                System.out.println("You are in "+region.getUniqueId());
            }

            @Override
            public void didExitRegion(final Region region) {
                System.out.println("This is not " + region.getUniqueId());
                Runnable changeText = new Runnable()
                {
                    public void run()
                    {
                        _detectedLocation = (TextView)findViewById(R.id.detectLocation);

                        _clickAttend.setEnabled(false);
                        _detectedLocation.setText("Detected Location: ");
                        Toast.makeText(getApplicationContext(),"You left "+region.getUniqueId(),Toast.LENGTH_LONG).show();

                    }
                };
                runOnUiThread(changeText);
                //System.out.println("This is "+region.getUniqueId());
            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {

               // Toast.makeText(context, "change ", Toast.LENGTH_SHORT).show();
            }

           });

        try
        {
            Identifier identifier = Identifier.parse("2F234454-CF6D-4A0F-ADF2-F4911BA9FFA6"); //beacon 1

            beaconManager.startMonitoringBeaconsInRegion(new Region("Classroom", Identifier.parse("2F234454-CF6D-4A0F-ADF2-F4911BA9FFA6"), null, null));
            }
        catch (RemoteException e) {
                e.printStackTrace();
        }
    }
}

