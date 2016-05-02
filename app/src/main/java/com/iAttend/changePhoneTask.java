package com.iAttend;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by erza on 15/12/05.
 */
public class changePhoneTask extends AsyncTask<String,Void, String>{
    Context mContext;
    public changePhoneTask(Context context){
        mContext = context;

    }

    @Override
    protected String doInBackground(String... params) {
        String username = (String)params[0];
        String password = (String)params[1];
        String macAdd = (String) params[2];
        try {

            String link = "http://noirdev.xyz/UPDATE_MAC.php?username=" + username + "&password=" + password+"&macAddress="+ macAdd;
            URL url = new URL(link);
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(link);
            HttpResponse response = client.execute(request);

            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer sb = new StringBuffer("");
            String line = "";
            String status = "";

            while ((line = in.readLine()) != null) {



                break;

            }
            in.close();
            return status;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
