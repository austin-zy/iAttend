package com.iAttend;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by erza on 15/11/28.
 */
public class GetCurrentClass extends AsyncTask<String,Void,String[]> {
    Context context;

    public GetCurrentClass(Context _context){
            context = _context;
    }

    @Override
    protected String[] doInBackground(String... params) {
        String word[] = new String[3];
        try {
            String email = (String) params[0];


            String link = "http://noirdev.xyz/currentCourse.php?username=" + email;
            URL url = new URL(link);
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(link);
            HttpResponse response = client.execute(request);

            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            String line = "";

            line = in.readLine();
            word = line.split("</br>");


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
        super.onPostExecute(strings);
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
                                String currentTime=sdf.format(new Date());
                                try {
                                    if (strings.length==2){
                                        Date _currentTIme = sdf.parse(currentTime);
                                        Date _endTime = sdf.parse(strings[2]);
                                        if (_currentTIme.after(_endTime)){
                                            Toast.makeText(context,"Class Finished",Toast.LENGTH_SHORT).show();
                                            toggleButton();
                                    }


                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


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
    }

    private void toggleButton(){
        Button _button = (Button)((Activity)context).findViewById(R.id.attendClick);
        _button.setEnabled(true);
    }
}
