package com.iAttend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by erza on 15/11/29.
 */
public class PrintResult extends AsyncTask<String, Void, String[]> {
    Context context;
    public PrintResult(Context _context){
        context=_context;
    }
    @Override
    protected String[] doInBackground(String... params) {
        String [] student_NameList;
        try {
            String session_id = (String) params[0];
            String course_id = (String) params[1];

            String link = "http://noirdev.xyz/attendanceList.php?sessionID="+session_id+"&courseID="+course_id;
            URL url = new URL(link);
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(link);
            HttpResponse response = client.execute(request);

            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            String line = "";

            line = in.readLine();

            student_NameList=line.split("<br />");



            in.close();
            return student_NameList;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected void onPostExecute(String[] strings) {
        super.onPostExecute(strings);

        ((Activity) context).finish();
        Intent intent = new Intent((Activity)context, attendanceList.class);
        intent.putExtra("listArray",strings);
        ((Activity)context).startActivity(intent);

    }
}
