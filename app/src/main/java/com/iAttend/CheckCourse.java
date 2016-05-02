package com.iAttend;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by erza on 15/11/22.
 */
public class CheckCourse extends AsyncTask<String,Void,String[]> {
    Context context;
    LocationPreference locationPreference;
    CurrentClass currentClass;



    public CheckCourse(Context context,int flag){
        this.context= context;
        locationPreference = new LocationPreference(context);
        currentClass = new CurrentClass(context);
    }

    @Override
    protected String[] doInBackground(String... params) {
        String word[] = new String[9];
        try {
            String email = (String) params[0];


                String link = "http://noirdev.xyz/coba.php?username=" + email;
                URL url = new URL(link);
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(link);
                HttpResponse response = client.execute(request);

                BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                String line = "";

                line = in.readLine();
                word = line.split("<br/>");


                    for (int i = 0; i < word.length; i++) {
                        System.out.println(word[i]);
                    }



                in.close();
                return word;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected void onPostExecute(final String[] strings) {

        final TextView _timeText = (TextView)((Activity)context).findViewById(R.id.timeTextView);
        TextView _dayText = (TextView)((Activity)context).findViewById(R.id.dayText);
        TextView _classText = (TextView)((Activity)context).findViewById(R.id.classText);
        TextView _sessionText = (TextView)((Activity)context).findViewById(R.id.sessionText);
        TextView _locationText=(TextView)((Activity)context).findViewById(R.id.locationText);

        Thread t = new Thread(){
            @Override
            public void run(){
            try{
                while(!isInterrupted()){
                    Thread.sleep(1000);
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                            _timeText.setText("Time: " + sdf.format(new Date()));
                        }
                    });
                }

            }
            catch(InterruptedException e){
                e.printStackTrace();

            }
            }
        };
        t.start();

        if (strings.length==9) {
            _dayText.setText("Day : " + strings[2]);
            _classText.setText("Class : " + strings[3]);
            _locationText.setText("Location : " + strings[5]);
            _sessionText.setText("Session : "+ strings[8]);
            String uuid = strings[4];
            String locationName = strings[5];
            locationPreference.createLocationPreference(uuid, locationName);
            currentClass.createCurrentClass(strings[6],strings[7]);
        }
        else{
            String simpledate =new SimpleDateFormat("EEEE").format(new Date());
            _dayText.setText("Day : "+ simpledate );
            _classText.setText("Class : No Class" );
            _locationText.setText("Location : No Class " );

        }
            }


    }

