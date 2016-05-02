package com.iAttend;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class changePhoneActivity extends AppCompatActivity {
    Button _agreeChange;
    EditText _emailForm;
    EditText _pwForm;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);

        _emailForm = (EditText)findViewById(R.id.email_changePhone);
        _pwForm = (EditText)findViewById(R.id.pw_changePhone);
        _agreeChange = (Button)findViewById(R.id.btn_agree);
        context = this;


        _agreeChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = _emailForm.getText().toString();
                String password = _pwForm.getText().toString();

                WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = manager.getConnectionInfo();
                String macAddress = info.getMacAddress();

                new changePhoneTask(context).execute(email, password,macAddress);
                finish();
                Toast.makeText(context,"Successful",Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_change_phone, menu);
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
