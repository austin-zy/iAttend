package com.iAttend;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @InjectView(R.id.input_name) EditText _nameText;
    @InjectView(R.id.input_specialization) EditText _specializationText;
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.input_id) EditText _idText;
    @InjectView(R.id.menu_category)Spinner _menuCategory;
    @InjectView(R.id.menu_school) Spinner _menuSchool;
    @InjectView(R.id.btn_signup)
    Button _signupButton;
    @InjectView(R.id.link_login)
    TextView _loginLink;

    private static final String REGISTER_URL = "http://noirdev.xyz/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);
        //For Spinner Adapter
        ArrayAdapter<CharSequence> category_adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> school_adapter = ArrayAdapter.createFromResource(this,
                R.array.school_array, android.R.layout.simple_spinner_item);
        category_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        school_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        _menuCategory.setAdapter(category_adapter);
        _menuSchool.setAdapter(school_adapter);
        //On Click Function
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }
    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme);
        //progressDialog.setIndeterminate(true);
        //progressDialog.setMessage("Creating Account...");
        //progressDialog.show();

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String id = _idText.getText().toString();
        String specialization = _specializationText.getText().toString();
        String role = _menuCategory.getSelectedItem().toString();
        WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String macAdd = info.getMacAddress();

        if(register(name,specialization,email,password,id,macAdd)) {
            finish();
            Intent intent = new Intent(this, LoginActivity.class);
            this.startActivity(intent);
            Toast.makeText(getApplicationContext(),"Registered Successful",Toast.LENGTH_SHORT).show();
        }
        }
        // TODO: Implement your own signup logic here.

    private boolean register(String name, String specialization, String email, String password, String id,String macAdd) {
        String urlSuffix = "?name="+name+"&specialization="+specialization+"&email="+email+"&password="+password+"&id="+id+"&macAddress="+macAdd;
        class signup extends AsyncTask<String, Void, String> {

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
               super.onPreExecute();
              //loading = ProgressDialog.show(SignupActivity.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), "Sign up successful", Toast.LENGTH_SHORT).show();

            }

            protected String doInBackground(String... params) {
                String s = params[0];
                String result="";
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(REGISTER_URL+s);
                   // HttpURLConnection con = (HttpURLConnection) usdrl.openConnection();
                  //  bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    HttpClient client = new DefaultHttpClient();
                    HttpGet request = new HttpGet(url.toString());
                    HttpResponse response = client.execute(request);

                    BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));


                    String line="";

                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                        System.out.println(line);
                        break;
                    }
                    in.close();


                   // result = bufferedReader.readLine();
                    result= sb.toString();
                    System.out.println(result);

                }catch(Exception e){
                    e.printStackTrace();
                }
                return result;
            }
        }
        signup su = new signup();
        su.execute(urlSuffix);
        return true;


    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        //finish();

    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Sign up failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
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
        getMenuInflater().inflate(R.menu.menu_signup, menu);
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
