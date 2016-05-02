package com.iAttend;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    //private EditText usernameField,passwordField; -> _idText & _passwordText
   // private TextView status,role,method;

    @InjectView(R.id.input_id) EditText _idText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_login) Button _loginButton;
    @InjectView(R.id.link_signup) TextView _signupLink;
    @InjectView(R.id.category_spinner) Spinner _categorySpinner;
    @InjectView(R.id.changePhone) TextView _changePhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        //LOGIN BUTTON
        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        //SIGN UP BUTTON
        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
        _changePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

    }


    public void login() {
        Log.d(TAG, "Login");

        if(!validate()){
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String id = _idText.getText().toString();
        String password = _passwordText.getText().toString();
        String role=_categorySpinner.getSelectedItem().toString();

        WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String macAddress = info.getMacAddress();

        //Toast.makeText(this,"The role is: "+role,Toast.LENGTH_LONG);


        new SigninActivity(this,0).execute(id, password,role,macAddress);
        progressDialog.dismiss();
/*
        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        onLoginSuccess();
                        progressDialog.dismiss();

                    }
                }, 3000);*/
    }

    /*public void loginPost(View view){
        //String username = usernameField.getText().toString();
        //String password = passwordField.getText().toString();
       // method.setText("Post Method");
        String id = _idText.getText().toString();
        String password = _passwordText.getText().toString();
        new SigninActivity(this,status,role,1).execute(id,password);
    }*/

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_SIGNUP){
            if(resultCode == RESULT_OK){
                this.finish();
            }
        }
    }

    public void onBackPressed(){
        moveTaskToBack(true);
    }

    public void onLoginSuccess(){
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed(){
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_SHORT);
        _loginButton.setEnabled(true);
    }

    public boolean validate(){
        boolean valid = true;

        String id = _idText.getText().toString();
        String password = _passwordText.getText().toString();

        if(id.isEmpty()){
            _idText.setError("Please enter a valid ID");
            valid=false;
        }
        else{
            _idText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }


        return valid;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
