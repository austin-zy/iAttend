package com.iAttend;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by erza on 15/11/29.
 */
public class GetClassList extends AsyncTask<String,Void,ArrayList> {
    Context context;

    public GetClassList(Context _context){
        context=_context;
    }

    @Override
    protected ArrayList<String[]> doInBackground(String... params) {
        String lines[];
        ArrayList classList = new ArrayList(10);
        try {
            String id = (String) params[0];

            String link = "http://noirdev.xyz/profCourseList.php?profID=" + id;
            URL url = new URL(link);
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(link);
            HttpResponse response = client.execute(request);

            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            String line = "";

            line = in.readLine();
            lines = new String[line.split("<br/>").length];
            lines = line.split("<br/>");


            for (int i = 1; i < lines.length; i++) {
                String [] word = new String[3];
                word = lines[i].split("\t");
                for(int j=0;j<word.length;j++){
                    System.out.println(word[j]);
                }
                classList.add(word);
            }



            in.close();
            return classList;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList arrayList) {
        super.onPostExecute(arrayList);
        String [] string_list= new String[arrayList.size()-1];
        for (int i =0;i<arrayList.size()-1;i++){
            String[] item=(String[])arrayList.get(i);
            string_list[i] = item[1]+"("+item[0]+")";
            System.out.println(string_list[i]);
        }
        Spinner _classSpinner =(Spinner)((Activity)context).findViewById(R.id.classSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1,string_list );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _classSpinner.setAdapter(adapter);

    }
}
