package com.iAttend;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.net.URL;


public class AttendClass extends AsyncTask<String, Void, String>{

    @Override
    protected String doInBackground(String... params) {
        String email = (String) params[0];
        String classType = (String) params[1];

    try {
        String link = "http://noirdev.xyz/attend.php?email=" + email+"&class_type="+ classType;
        URL url = new URL(link);
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(link);
        HttpResponse response = client.execute(request);
    }
    catch(Exception e){
        e.printStackTrace();

    }
        return null;
    }
}
