package com.iAttend;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;

import java.util.Arrays;

public class ProfActivity extends AppCompatActivity {
private BeaconManager beaconManager;
private ToggleButton _toggleButton;
private EditText _sessionEdit;
private Button _printButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof);
        _toggleButton =(ToggleButton)findViewById(R.id.beaconToggle);
        _toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((ToggleButton) v).isChecked()){
                    Toast.makeText(ProfActivity.this,"Bluetooth Started",Toast.LENGTH_SHORT);
                    startBeaconActivity();
                }
                else{

                }
            }
        });

        new GetClassList(this).execute("2");
        checkAttendance(this);
    }

    public void checkAttendance(final Context context){
        final Spinner _classSpinner = (Spinner) findViewById(R.id.classSpinner);
        _sessionEdit = (EditText)findViewById(R.id.sessionEdit);
        _printButton = (Button) findViewById(R.id.printResult);


        _printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selected_item=_classSpinner.getSelectedItem().toString();
                //char obracket ='(';
                //char cbracket =')';
                //String[] splitted=selected_item.split(Character.toString(obracket));
                //System.out.println((splitted[1].split(Character.toString(cbracket))[0]));
                //final String course_id = (splitted[1].split(Character.toString(cbracket))[0]);
                String course_id="1";
                String session=_sessionEdit.getText().toString();
                new PrintResult(context).execute(session,course_id);
            }
        });

    }

    public void startBeaconActivity(){
        Beacon beacon = new Beacon.Builder()
                .setId1("F0018B9B-7509-4C31-A905-1A27D39C003C")
                .setId2("1")
                .setId3("2")
                //.setManufacturer(0x0118)
                .setManufacturer(0x004c)
                .setTxPower(-59)
                .setDataFields(Arrays.asList(new Long[]{0l}))
                .build();
        //BeaconParser beaconParser = new BeaconParser()
        //        .setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");
        BeaconParser beaconParser = new BeaconParser()
                .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24");
        BeaconTransmitter beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);
        beaconTransmitter.startAdvertising(beacon);
        System.out.println("Bluetooth started");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_prof, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
